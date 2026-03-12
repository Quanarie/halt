package pl.quanarie.halt.common.event;

import java.util.UUID;

public record UserLocationUpdatedEvent(
    UUID userId,
    double latitude,
    double longitude
) {}
