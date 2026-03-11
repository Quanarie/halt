package pl.quanarie.halt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.quanarie.halt.ride.RideRepository;
import pl.quanarie.halt.ride.RideRequestDto;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainersConfig.class)
class RideIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RideRepository rideRepository;

	@Autowired
	private TestKafkaConsumer testKafkaConsumer;

	@BeforeEach
	void setUp() {
		rideRepository.deleteAll();
		testKafkaConsumer.reset();
	}

	@Test
	void shouldPersistRideAndPublishEvent() {
		// GIVEN:
		RideRequestDto request = new RideRequestDto(
			UUID.randomUUID(),
			52.22, 21.01,
			52.40, 16.92,
			new BigDecimal("25.50")
		);

		// WHEN:
		ResponseEntity<Void> response = restTemplate.postForEntity("/rides", request, Void.class);

		// THEN:
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// DB:
		var rides = rideRepository.findAll();
		assertThat(rides).hasSize(1);
		assertThat(rides.getFirst().getPassengerId()).isEqualTo(request.passengerId());

		// KAFKA:
		await()
			.atMost(10, TimeUnit.SECONDS)
			.pollInterval(100, TimeUnit.MILLISECONDS)
			.untilAsserted(() -> {
				var publishedEvents = testKafkaConsumer.getConsumedEvents();
				assertThat(publishedEvents).hasSize(1);
				var event = publishedEvents.getFirst();
				assertThat(event.passengerId()).isEqualTo(request.passengerId());
				assertThat(event.price()).isEqualByComparingTo(request.price());
			});
	}
}
