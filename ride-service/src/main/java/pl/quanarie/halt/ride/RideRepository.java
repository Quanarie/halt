package pl.quanarie.halt.ride;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {
	Optional<Ride> findFirstByDriverIdAndStatusIn(UUID driverId, Collection<RideStatus> statuses);
}
