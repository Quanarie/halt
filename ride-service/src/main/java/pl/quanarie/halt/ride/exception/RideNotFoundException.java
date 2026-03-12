package pl.quanarie.halt.ride.exception;

import java.util.UUID;

public class RideNotFoundException extends RuntimeException {
	public RideNotFoundException(UUID rideId) {
		super(String.format("Ride with id %s not found.", rideId));
	}
}
