package pl.quanarie.halt.common.event;

public record RideOfferEvent(
	String rideId,
	String driverId,
	double startLat,
	double startLon
) {
}
