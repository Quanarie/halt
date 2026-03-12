package pl.quanarie.halt.common.event;

import java.math.BigDecimal;
import java.util.UUID;

public record RideMatchingRequestedEvent(
	UUID rideId,
	BigDecimal price,
	// TODO: client info: rating
	double startLan,
	double startLon,
	double endLan,
	double endLon,
	int retryCount
) {
}
