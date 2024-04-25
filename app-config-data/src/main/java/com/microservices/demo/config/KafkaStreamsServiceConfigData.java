package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
// Vem do arquivo config-client-kafka_streams.yml
@ConfigurationProperties(prefix = "kafka-streams-service")
public class KafkaStreamsServiceConfigData {
	private String version;
	private String customAudience;
}
