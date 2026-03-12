package pl.quanarie.halt.ride;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.quanarie.halt.common.event.RideMatchingRequestedEvent;
import pl.quanarie.halt.common.kafka.KafkaTopics;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideMatchingScheduler {

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void scheduleRetry(Ride ride, int nextRetryCount) {
		log.info("Zaplanowano ponowienie matchingu dla {} za 5 sekund (próba {})", ride.getId(), nextRetryCount);

		scheduler.schedule(() -> {
			var event = RideEventMapper.toRideMatchingRequestedEvent(ride, nextRetryCount);
			kafkaTemplate.send(KafkaTopics.RIDE_MATCHING_REQUESTED, ride.getId().toString(), event);
		}, 5, TimeUnit.SECONDS);
	}
}
