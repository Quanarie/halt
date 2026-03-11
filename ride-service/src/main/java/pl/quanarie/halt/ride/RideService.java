package pl.quanarie.halt.ride;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.quanarie.halt.config.KafkaConfig;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideService {
	private final RideRepository rideRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;

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
			.build();

		Ride savedRide = rideRepository.save(ride);
		log.info("Created ride request with ID: {}", savedRide.getId());

		// TODO: outbox
		kafkaTemplate.send(
			KafkaConfig.RIDE_ORDERED,
			savedRide.getId().toString(),
			RideRequestedEvent.fromRide(savedRide)
		);

		return savedRide;
	}
}
