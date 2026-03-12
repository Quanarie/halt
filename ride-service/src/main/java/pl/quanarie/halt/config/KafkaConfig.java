package pl.quanarie.halt.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// TODO: extract to common-lib
@Configuration
public class KafkaConfig {

	public static String RIDE_REQUESTED = "ride.requested";

	@Bean
	public NewTopic rideRequestedTopic() {
		return TopicBuilder.name(RIDE_REQUESTED)
			.partitions(3)
			.build();
	}
}
