package pl.quanarie.halt.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateRideRequestException.class)
	public ResponseEntity<String> handleConflict(DuplicateRideRequestException ex) {
		return ResponseEntity
			.status(HttpStatus.CONFLICT)
			.body("Duplicate request: this ride is already being processed.");
	}
}
