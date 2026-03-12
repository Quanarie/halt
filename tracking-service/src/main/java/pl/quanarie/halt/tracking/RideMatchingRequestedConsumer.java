package pl.quanarie.halt.tracking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.quanarie.halt.common.event.NearbyDriversFoundEvent;
import pl.quanarie.halt.common.event.RideMatchingRequestedEvent;

import static pl.quanarie.halt.common.kafka.KafkaTopics.NEARBY_DRIVERS_FOUND;
import static pl.quanarie.halt.common.kafka.KafkaTopics.RIDE_MATCHING_REQUESTED;
import static pl.quanarie.halt.tracking.TrackingController.USER_LOCATIONS_REDIS_KEY;

@Component
@RequiredArgsConstructor
public class RideMatchingRequestedConsumer {

	private final ReactiveStringRedisTemplate redisTemplate;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@KafkaListener(topics = RIDE_MATCHING_REQUESTED)
	public void handle(RideMatchingRequestedEvent event) {
		double radius = 2.0 + (event.retryCount() * 2.0);
		Circle area = new Circle(new Point(event.startLon(), event.startLan()), new Distance(radius, Metrics.KILOMETERS));

		redisTemplate.opsForGeo()
			.radius(USER_LOCATIONS_REDIS_KEY, area)
			.map(res -> res.getContent().getName())
			.collectList()
			.subscribe(driverIds -> {
				kafkaTemplate.send(NEARBY_DRIVERS_FOUND, new NearbyDriversFoundEvent(
					event.rideId(),
					driverIds,
					event.retryCount()
				));
			});
	}
}
