//package pl.quanarie.halt.ride.config;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//import static pl.quanarie.halt.common.kafka.KafkaTopics.RIDE_REQUESTED;
//
//@Configuration
//public class KafkaConfig {
//	@Bean
//	public NewTopic rideRequestedTopic() {
//		return TopicBuilder.name(RIDE_REQUESTED)
//			.partitions(3)
//			.build();
//	}
//}
