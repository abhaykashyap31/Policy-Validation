package Booking.GDS.service.Corporate.Travel.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration 
public class KafkaTopicConfig {
    
    @Bean 
    public NewTopic travelRequestTopic() {
        // Example: 24 hours in milliseconds (24 * 60 * 60 * 1000 = 86400000 ms)
        long retentionTimeMs = 86_400_000L; 

        return TopicBuilder.name("travel-validation-requests")
                .partitions(3)
                .replicas(1)
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(retentionTimeMs))
                // Optional: Force segments to roll over every 4 hours for smaller volumes
                .config(TopicConfig.SEGMENT_MS_CONFIG, String.valueOf(14_400_000L))
                .build();
    }
}
