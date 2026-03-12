package pl.quanarie.halt.ride.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.PaymentOnHoldEvent;
import pl.quanarie.halt.ride.RideService;
import pl.quanarie.halt.ride.RideStatus;

import static pl.quanarie.halt.common.kafka.KafkaTopics.PAYMENT_ON_HOLD;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOnHoldConsumer {
	private final RideService rideService;

	@KafkaListener(topics = PAYMENT_ON_HOLD)
	public void handlePaymentSuccess(PaymentOnHoldEvent event) {
		log.info("Płatność zamrożona dla przejazdu {}. Zmieniam status na MATCHING.", event.rideId());
		rideService.updateStatus(event.rideId(), RideStatus.MATCHING);
		// TODO: send RideReadyForMatchingEvent to be consumed by MatchingService
	}
}
