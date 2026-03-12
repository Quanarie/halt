package pl.quanarie.halt.exception;

public class DuplicateRideRequestException extends RuntimeException {
	public DuplicateRideRequestException(String message) {
		super(message);
	}
}
