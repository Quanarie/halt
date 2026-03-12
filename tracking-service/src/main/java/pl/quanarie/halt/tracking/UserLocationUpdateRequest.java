package pl.quanarie.halt.tracking;

import java.util.UUID;

public record UserLocationUpdateRequest(
	UUID userId,
	double latitude,
	double longitude
) {
}
