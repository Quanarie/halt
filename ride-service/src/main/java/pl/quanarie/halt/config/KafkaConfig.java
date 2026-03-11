package pl.quanarie.halt.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// TODO: extract to common-lib
@Configuration
public class KafkaConfig {

	public static String RIDE_ORDERED = "ride.ordered";

	@Bean
	public NewTopic rideOrderedTopic() {
		return TopicBuilder.name(RIDE_ORDERED)
			.partitions(3)
			.build();
	}
}
