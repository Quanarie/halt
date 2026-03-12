package pl.quanarie.halt.ride;

import pl.quanarie.halt.common.event.DriverLocationForPassengerEvent;
import pl.quanarie.halt.common.event.RideAcceptedEvent;
import pl.quanarie.halt.common.event.RideMatchingRequestedEvent;
import pl.quanarie.halt.common.event.RideRequestedEvent;

public class RideEventMapper {
    public static RideRequestedEvent toRideRequested(Ride ride) {
        return new RideRequestedEvent(ride.getId(), ride.getPassengerId(), ride.getPrice());
    }

    public static DriverLocationForPassengerEvent toDriverLocationUpdated(Ride ride, Double latitude, Double longitude) {
        return new DriverLocationForPassengerEvent(ride.getPassengerId(), ride.getDriverId(), latitude, longitude);
    }

    public static RideMatchingRequestedEvent toRideReadyForMatching(Ride ride) {
        return new RideMatchingRequestedEvent(ride.getId(), ride.getPrice(), ride.getStartLat(), ride.getStartLon(),
          ride.getEndLat(), ride.getEndLon(), 1);
    }

    public static RideAcceptedEvent toRideAcceptedEvent(Ride ride) {
        return new RideAcceptedEvent(ride.getId(), ride.getPassengerId(), ride.getDriverId());
    }

    public static RideMatchingRequestedEvent toRideMatchingRequestedEvent(Ride ride, int nextRetryCount) {
        return new RideMatchingRequestedEvent(
          ride.getId(),
          ride.getPrice(),
          ride.getStartLat(),
          ride.getStartLon(),
          ride.getEndLat(),
          ride.getEndLon(),
          nextRetryCount
        );
    }
}
