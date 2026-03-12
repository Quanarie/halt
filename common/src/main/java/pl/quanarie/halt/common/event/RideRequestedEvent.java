package pl.quanarie.halt.common.event;

import java.math.BigDecimal;
import java.util.UUID;

public record RideRequestedEvent(
	UUID rideId,
	UUID passengerId,
	BigDecimal price
) {
}
