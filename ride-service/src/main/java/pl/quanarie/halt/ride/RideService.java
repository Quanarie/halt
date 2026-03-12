package pl.quanarie.halt.ride;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.quanarie.halt.ride.exception.DuplicateRideRequestException;
import pl.quanarie.halt.ride.exception.IllegalRideStatusTransitionException;
import pl.quanarie.halt.ride.exception.RideNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.quanarie.halt.common.kafka.KafkaTopics.RIDE_REQUESTED;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideService {
	private final RideRepository rideRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	private static final List<RideStatus> ACTIVE_STATUSES = List.of(
		RideStatus.ACCEPTED,
		RideStatus.ARRIVED,
		RideStatus.IN_PROGRESS
	);

	@Transactional
	public Ride createRide(RideRequestDto request) {
		Ride ride = Ride.builder()
			.passengerId(request.passengerId())
			.startLat(request.startLat())
			.startLon(request.startLon())
			.endLat(request.endLat())
			.endLon(request.endLon())
			.status(RideStatus.REQUESTED)
			.price(request.price())
			.idempotencyKey(request.idempotencyKey())
			.build();

		Ride savedRide;
		try {
			savedRide = rideRepository.saveAndFlush(ride);
			log.info("Created ride request with ID: {}", savedRide.getId());
		} catch (DataIntegrityViolationException e) {
			log.warn("Duplicate ride request detected for idempotency key: {}", request.idempotencyKey());
			throw new DuplicateRideRequestException();
		}

		// TODO: outbox instead of saveAndFlush, cuz there is still non atomic window
		//  between commit to db and send to kafka.
		kafkaTemplate.send(
			RIDE_REQUESTED,
			savedRide.getId().toString(),
			RideEventMapper.toRideRequested(savedRide)
		);

		return savedRide;
	}

	@Transactional
	public void updateStatus(UUID rideId, RideStatus newStatus) {
		Ride ride = rideRepository.findById(rideId)
			.orElseThrow(() -> new RideNotFoundException(rideId));

		if (!canTransition(ride.getStatus(), newStatus)) {
			log.error("Nieprawidłowe przejście stanu dla przejazdu {}: {} -> {}",
				rideId, ride.getStatus(), newStatus);
			throw new IllegalRideStatusTransitionException(ride.getStatus(), newStatus);
		}

		ride.setStatus(newStatus);
		rideRepository.save(ride);
		log.info("Status przejazdu {} zmieniony na {}", rideId, newStatus);
	}

	private boolean canTransition(RideStatus current, RideStatus next) {
		return switch (current) {
			case REQUESTED -> next == RideStatus.MATCHING || next == RideStatus.CANCELLED;
			case MATCHING -> next == RideStatus.ACCEPTED || next == RideStatus.CANCELLED;
			case ACCEPTED -> next == RideStatus.ARRIVED || next == RideStatus.CANCELLED;
			case ARRIVED -> next == RideStatus.IN_PROGRESS || next == RideStatus.CANCELLED;
			case IN_PROGRESS -> next == RideStatus.COMPLETED;
			case COMPLETED, CANCELLED -> false;
		};
	}

	public Optional<Ride> tryGetActiveRideForDriver(UUID driverId) {
		return rideRepository.findFirstByDriverIdAndStatusIn(driverId, ACTIVE_STATUSES);
	}
}
