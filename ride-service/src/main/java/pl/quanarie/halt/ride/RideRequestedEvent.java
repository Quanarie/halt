package pl.quanarie.halt.ride;

import java.math.BigDecimal;
import java.util.UUID;

public record RideRequestedEvent(
	UUID rideId,
	UUID passengerId,
	BigDecimal price
) {
	public static RideRequestedEvent fromRide(Ride ride) {
		return new RideRequestedEvent(ride.getId(), ride.getPassengerId(), ride.getPrice());
	}
}
