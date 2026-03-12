package pl.quanarie.halt.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.DriverLocationForPassengerEvent;

import static pl.quanarie.halt.common.kafka.KafkaTopics.DRIVER_LOCATION_FOR_PASSENGER;
import static pl.quanarie.halt.notification.config.WebSocketConfig.CLIENT_COMMUNICATION_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverLocationForPassengerConsumer {

	private final SimpMessagingTemplate messagingTemplate;

	@KafkaListener(topics = DRIVER_LOCATION_FOR_PASSENGER)
	public void handleDriverLocationForPassenger(DriverLocationForPassengerEvent event) {
		log.info("Przesyłam pozycję kierowcy {} do pasażera {}", event.driverId(), event.passengerId());

		String destination = String.format("%s/user/%s",
			CLIENT_COMMUNICATION_TOPIC,
			event.passengerId()
		);

		messagingTemplate.convertAndSend(destination, event);
	}
}
