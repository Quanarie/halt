package pl.quanarie.halt.common.event;

import java.util.UUID;

public record DriverLocationForPassengerEvent(
    UUID passengerId,
    UUID driverId,
    Double latitude,
    Double longitude
) {}
