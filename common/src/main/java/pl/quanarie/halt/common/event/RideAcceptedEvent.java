package pl.quanarie.halt.common.event;

import java.util.UUID;

public record RideAcceptedEvent(
    UUID rideId,
    UUID passengerId,
    UUID driverId
) {}
