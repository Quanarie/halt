package pl.quanarie.halt.tracking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.quanarie.halt.common.event.UserLocationUpdatedEvent;
import reactor.core.publisher.Mono;

import static pl.quanarie.halt.common.kafka.KafkaTopics.USER_LOCATION_UPDATED;

@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/location")
    public Mono<Void> updateLocation(@RequestBody UserLocationUpdatedEvent request) {
        String key = "user:locations";
        Point point = new Point(request.longitude(), request.latitude());
        
        return redisTemplate.opsForGeo()
                .add(key, point, request.userId().toString())
                .then(Mono.fromRunnable(() -> {
                    kafkaTemplate.send(USER_LOCATION_UPDATED, request.userId().toString(), request);
                })).then();
    }
}
