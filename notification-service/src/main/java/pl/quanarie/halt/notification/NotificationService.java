package pl.quanarie.halt.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.quanarie.halt.common.event.UserLocationUpdatedEvent;
import pl.quanarie.halt.common.kafka.KafkaTopics;

import static pl.quanarie.halt.notification.config.WebSocketConfig.CLIENT_COMMUNICATION_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = KafkaTopics.USER_LOCATION_UPDATED)
    public void handleUserLocationUpdate(UserLocationUpdatedEvent event) {
        log.info("Przesyłam pozycję użytkownika {} do brokera websocket", event.userId());
        messagingTemplate.convertAndSend(CLIENT_COMMUNICATION_TOPIC + "/user/" + event.userId(), event);
    }
}
