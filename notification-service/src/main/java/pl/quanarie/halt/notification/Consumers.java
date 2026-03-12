package pl.quanarie.halt.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.DriverLocationForPassengerEvent;
import pl.quanarie.halt.common.event.RideAcceptedEvent;
import pl.quanarie.halt.common.event.RideOfferEvent;

import static pl.quanarie.halt.common.kafka.KafkaTopics.*;
import static pl.quanarie.halt.notification.config.WebSocketConfig.CLIENT_COMMUNICATION_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumers {

	private final SimpMessagingTemplate messagingTemplate;

	@KafkaListener(topics = DRIVER_LOCATION_FOR_PASSENGER)
	public void handleDriverLocationForPassenger(DriverLocationForPassengerEvent event) {
		log.info("Przesyłam pozycję kierowcy {} do pasażera {}", event.driverId(), event.passengerId());

		String destination = String.format("%s/user/%s/update",
			CLIENT_COMMUNICATION_TOPIC,
			event.passengerId()
		);

		messagingTemplate.convertAndSend(destination, event);
	}

	@KafkaListener(topics = RIDE_OFFER)
	public void handleRideOffer(RideOfferEvent event) {
		log.info("Powiadamiam kierowcę {} o nowej ofercie przejazdu {}", event.driverId(), event.rideId());

		String destination = String.format("%s/user/%s/offers",
			CLIENT_COMMUNICATION_TOPIC,
			event.driverId()
		);

		messagingTemplate.convertAndSend(destination, event);
	}

	@KafkaListener(topics = RIDE_ACCEPTED)
	public void handleRideAccepted(RideAcceptedEvent event) {
		log.info("Powiadamiam pasażera {} o zaakceptowaniu przejazdu {}", event.passengerId(), event.rideId());

		String destination = String.format("%s/user/%s/update",
			CLIENT_COMMUNICATION_TOPIC,
			event.passengerId()
		);

		messagingTemplate.convertAndSend(destination, event);
	}
}
