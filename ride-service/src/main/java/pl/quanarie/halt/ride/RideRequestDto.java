package pl.quanarie.halt.ride;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record RideRequestDto(
	@NotNull
	UUID passengerId,

	@NotNull
	Double startLat,
	@NotNull
	Double startLon,
	@NotNull
	Double endLat,
	@NotNull
	Double endLon,

	@Positive
	BigDecimal price
) {
}
