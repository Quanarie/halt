package pl.quanarie.halt.ride.exception;

import pl.quanarie.halt.ride.RideStatus;

public class IllegalRideStatusTransitionException extends RuntimeException {
	public IllegalRideStatusTransitionException(RideStatus currentStatus, RideStatus newStatus) {
		super(String.format("Illegal ride status transition: %s -> %s", currentStatus, newStatus));
	}
}
