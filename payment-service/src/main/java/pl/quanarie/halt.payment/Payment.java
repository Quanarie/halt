package pl.quanarie.halt.payment;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
	@Id
	private UUID id;

	private UUID rideId;
	private UUID passengerId;
	private BigDecimal price;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
}
