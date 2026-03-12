package pl.quanarie.halt.ride;

import pl.quanarie.halt.common.event.DriverLocationForPassengerEvent;
import pl.quanarie.halt.common.event.RideRequestedEvent;

public class RideEventMapper {
    public static RideRequestedEvent toRideRequested(Ride ride) {
        return new RideRequestedEvent(ride.getId(), ride.getPassengerId(), ride.getPrice());
    }

    public static DriverLocationForPassengerEvent toDriverLocationUpdated(Ride ride, double latitude, double longitude) {
        return new DriverLocationForPassengerEvent(ride.getPassengerId(), ride.getDriverId(), latitude, longitude);
    }
}
