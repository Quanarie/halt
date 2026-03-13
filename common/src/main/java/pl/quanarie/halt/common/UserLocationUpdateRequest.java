package pl.quanarie.halt.common;

public record UserLocationUpdateRequest(
	String userId,
	double latitude,
	double longitude
) {
}
