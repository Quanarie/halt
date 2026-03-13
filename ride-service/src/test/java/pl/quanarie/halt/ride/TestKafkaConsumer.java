package pl.quanarie.halt.ride;

import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.NearbyDriversFoundEvent;
import pl.quanarie.halt.common.event.RideRequestedEvent;
import pl.quanarie.halt.common.event.UserLocationUpdatedEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static pl.quanarie.halt.common.kafka.KafkaTopics.*;

@Getter
@Component
public class TestKafkaConsumer {

	private final List<RideRequestedEvent> rideRequestedEvents = new CopyOnWriteArrayList<>();

	@KafkaListener(topics = RIDE_REQUESTED)
	public void listenRequested(RideRequestedEvent event) { rideRequestedEvents.add(event); }

	public void reset() {
		rideRequestedEvents.clear();
	}
}
