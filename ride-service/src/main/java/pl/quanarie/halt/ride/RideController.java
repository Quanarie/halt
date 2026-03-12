package pl.quanarie.halt.ride;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rides")
public class RideController {
	private final RideService rideService;

	@PostMapping
	public ResponseEntity<Ride> createRide(@Valid @RequestBody RideRequestDto request) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(rideService.createRide(request));
	}

	@PostMapping("/{rideId}/accept")
	public ResponseEntity<Void> acceptRide(@PathVariable UUID rideId, @RequestParam UUID driverId) {
		rideService.acceptRide(rideId, driverId);
		return ResponseEntity.ok().build();
	}
}
