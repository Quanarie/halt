package pl.quanarie.halt.tracking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import pl.quanarie.halt.common.UserLocationUpdateRequest;
import pl.quanarie.halt.common.event.UserLocationUpdatedEvent;
import reactor.core.publisher.Mono;

import static pl.quanarie.halt.common.kafka.KafkaTopics.USER_LOCATION_UPDATED;

@Slf4j
@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class TrackingController {

	private final ReactiveStringRedisTemplate redisTemplate;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public static String USER_LOCATIONS_REDIS_KEY = "user:locations";

	@PostMapping("/location")
	public Mono<Void> updateLocation(@RequestBody UserLocationUpdateRequest request) {
		Point point = new Point(request.longitude(), request.latitude());

		log.info("Aktualizuje pozycję użytkownika {}", request.userId());

		return redisTemplate.opsForGeo()
			.add(USER_LOCATIONS_REDIS_KEY, point, request.userId())
			.then(Mono.fromRunnable(() -> {
				kafkaTemplate.send(
					USER_LOCATION_UPDATED,
					request.userId(),
					new UserLocationUpdatedEvent(request.userId(), request.latitude(), request.longitude())
				);
			})).then();
	}
}
