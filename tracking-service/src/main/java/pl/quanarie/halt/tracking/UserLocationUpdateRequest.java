package pl.quanarie.halt.tracking;

public record UserLocationUpdateRequest(
	String userId,
	double latitude,
	double longitude
) {
}
