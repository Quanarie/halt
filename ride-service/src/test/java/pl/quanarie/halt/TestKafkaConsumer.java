package pl.quanarie.halt;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.ride.RideRequestedEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class TestKafkaConsumer {

	private final List<RideRequestedEvent> consumedEvents = new CopyOnWriteArrayList<>();

	@KafkaListener(topics = "ride.ordered")
	public void listen(RideRequestedEvent event) {
		consumedEvents.add(event);
	}

	public List<RideRequestedEvent> getConsumedEvents() {
		return consumedEvents;
	}

	public void reset() {
		consumedEvents.clear();
	}

}
