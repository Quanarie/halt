package pl.quanarie.halt.common.event;

public record UserLocationUpdatedEvent(
    String userId,
    double latitude,
    double longitude
) {}
