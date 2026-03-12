package pl.quanarie.halt.common.event;

import java.util.UUID;

public record PaymentOnHoldEvent(
    UUID rideId,
    UUID paymentId
) {}
