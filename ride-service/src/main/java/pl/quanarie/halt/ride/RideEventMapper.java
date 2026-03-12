package pl.quanarie.halt.ride;

import pl.quanarie.halt.common.event.RideRequestedEvent;

public class RideEventMapper {
    public static RideRequestedEvent toRideRequested(Ride ride) {
        return new RideRequestedEvent(ride.getId(), ride.getPassengerId(), ride.getPrice());
    }
}
