package pl.quanarie.halt.ride.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.UserLocationUpdatedEvent;
import pl.quanarie.halt.ride.Ride;
import pl.quanarie.halt.ride.RideEventMapper;
import pl.quanarie.halt.ride.RideService;

import java.util.Optional;

import static pl.quanarie.halt.common.kafka.KafkaTopics.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLocationUpdatedConsumer {
	private final RideService rideService;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@KafkaListener(topics = USER_LOCATION_UPDATED)
	public void handleUserLocationUpdate(UserLocationUpdatedEvent event) {
		// TODO: move to redis, postgres not manage if lots of data,
		//  also throw mb, add case for driver to see passengers location
		log.info("Odebrano pozycję użytkownika {}", event.userId());
		Optional<Ride> ride = rideService.tryGetActiveRideForDriver(event.userId());
		if (ride.isEmpty()){
			log.warn("Odebrano event z pozycją usera {}, który nie ma aktywnego przejazdu", event.userId());
			return;
		}

		kafkaTemplate.send(
			DRIVER_LOCATION_FOR_PASSENGER,
			event.userId().toString(),
			RideEventMapper.toDriverLocationUpdated(ride.get(), event.latitude(), event.longitude())
		);
	}
}
