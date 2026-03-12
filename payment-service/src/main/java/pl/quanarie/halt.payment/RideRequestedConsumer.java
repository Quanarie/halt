package pl.quanarie.halt.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.RideRequestedEvent;

import static pl.quanarie.halt.common.kafka.KafkaTopics.RIDE_REQUESTED;

@Slf4j
@Component
@RequiredArgsConstructor
public class RideRequestedConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = RIDE_REQUESTED)
    public void handle(RideRequestedEvent event) {
        log.info("Odebrano żądanie płatności dla przejazdu: {}", event.rideId());
        
        paymentService.processPaymentRequest(
            event.rideId(), 
            event.passengerId(), 
            event.price()
        );
    }
}
