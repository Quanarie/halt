package pl.quanarie.halt.ride.exception;

public class DuplicateRideRequestException extends RuntimeException {
	public DuplicateRideRequestException() {
		super("Duplicate ride request");
	}
}
