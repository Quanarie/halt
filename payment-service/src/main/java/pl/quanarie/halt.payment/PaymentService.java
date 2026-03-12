package pl.quanarie.halt.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.quanarie.halt.common.event.PaymentOnHoldEvent;

import java.math.BigDecimal;
import java.util.UUID;

import static pl.quanarie.halt.common.kafka.KafkaTopics.PAYMENT_ON_HOLD;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void processPaymentRequest(UUID rideId, UUID passengerId, BigDecimal price) {
        if (paymentRepository.existsByRideId(rideId)) {
            log.warn("Płatność dla przejazdu {} już istnieje. Pomijam.", rideId);
            return;
        }

        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .rideId(rideId)
                .passengerId(passengerId)
                .price(price)
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);
        log.info("Zainicjowano płatność dla przejazdu: {}", rideId);

        // TODO: Integracja z bramką płatniczą (Stripe/PayU)
        kafkaTemplate.send(
          PAYMENT_ON_HOLD,
          rideId.toString(),
          new PaymentOnHoldEvent(rideId, payment.getId())
        );
    }
}
