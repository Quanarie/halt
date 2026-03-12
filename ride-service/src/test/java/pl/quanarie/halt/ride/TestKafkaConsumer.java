package pl.quanarie.halt.ride;

import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.RideRequestedEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static pl.quanarie.halt.common.kafka.KafkaTopics.RIDE_REQUESTED;

@Getter
@Component
public class TestKafkaConsumer {

	private final List<RideRequestedEvent> consumedEvents = new CopyOnWriteArrayList<>();

	@KafkaListener(topics = RIDE_REQUESTED)
	public void listen(RideRequestedEvent event) {
		consumedEvents.add(event);
	}

	public void reset() {
		consumedEvents.clear();
	}

}
