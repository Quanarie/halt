package pl.quanarie.halt.ride.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.NearbyDriversFoundEvent;
import pl.quanarie.halt.common.event.RideOfferEvent;
import pl.quanarie.halt.ride.RideMatchingScheduler;
import pl.quanarie.halt.ride.RideRepository;
import pl.quanarie.halt.ride.RideStatus;
import pl.quanarie.halt.ride.exception.RideNotFoundException;

import static pl.quanarie.halt.common.kafka.KafkaTopics.NEARBY_DRIVERS_FOUND;
import static pl.quanarie.halt.common.kafka.KafkaTopics.RIDE_OFFER;

@Slf4j
@Component
@RequiredArgsConstructor
public class NearbyDriversConsumer {

	private final RideRepository rideRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final RideMatchingScheduler matchingScheduler;

	@KafkaListener(topics = NEARBY_DRIVERS_FOUND)
	public void handleNearbyDrivers(NearbyDriversFoundEvent event) {
		log.info("Otrzymano listę {} potencjalnych kierowców dla przejazdu {}",
			event.driverIds().size(), event.rideId());

		rideRepository.findById(event.rideId()).ifPresent(ride -> {
			if (!ride.getStatus().equals(RideStatus.MATCHING)) {
				log.info("Przejazd {} nie jest już w stanie MATCHING. Przerywam matching.", event.rideId());
				return;
			}

			log.info(event.driverIds().toString());

			if (event.driverIds().isEmpty()) {
				int currentRetry = event.retryCount();
				if (currentRetry < 5) {
					log.warn("Brak kierowców dla {}. Planuję ponowienie (próba {})...", event.rideId(), currentRetry + 1);
					matchingScheduler.scheduleRetry(ride, currentRetry + 1);
				} else {
					log.error("Limit prób wyczerpany dla {}. Brak dostępnych aut.", event.rideId());
					// TODO: zmienić status na FAILED i wysłać event do pasażera
				}
				return;
			}

			for (String driverId : event.driverIds()) {
				log.info("Wysyłam ofertę przejazdu {} do kierowcy {}", event.rideId(), driverId);

				RideOfferEvent offer = new RideOfferEvent(
					event.rideId().toString(),
					driverId,
					ride.getStartLat(),
					ride.getStartLon()
				);

				kafkaTemplate.send(RIDE_OFFER, driverId, offer);
			}
		});
	}
}
