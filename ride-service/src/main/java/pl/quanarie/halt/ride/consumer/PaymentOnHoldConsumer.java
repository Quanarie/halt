package pl.quanarie.halt.ride.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.PaymentOnHoldEvent;
import pl.quanarie.halt.ride.RideEventMapper;
import pl.quanarie.halt.ride.RideService;
import pl.quanarie.halt.ride.RideStatus;

import static pl.quanarie.halt.common.kafka.KafkaTopics.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOnHoldConsumer {
	private final RideService rideService;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@KafkaListener(topics = PAYMENT_ON_HOLD)
	public void handlePaymentSuccess(PaymentOnHoldEvent event) {
		log.info("Płatność zamrożona dla przejazdu {}. Zmieniam status na MATCHING.", event.rideId());
		rideService.updateStatus(event.rideId(), RideStatus.MATCHING);

		var ride = rideService.findRideById(event.rideId());

		// TODO: outbox
		kafkaTemplate.send(
			RIDE_MATCHING_REQUESTED,
			event.rideId().toString(),
			RideEventMapper.toRideReadyForMatching(ride)
		);
	}
}
