package pl.quanarie.halt.common.event;

import java.util.List;
import java.util.UUID;

public record NearbyDriversFoundEvent (
	UUID rideId,
	List<String> driverIds,
	int retryCount
) {
}
