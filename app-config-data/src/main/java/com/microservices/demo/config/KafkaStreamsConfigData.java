package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
// Vem do arquivo config-client-kafka_streams.yml
@ConfigurationProperties(prefix = "kafka-streams-config")
public class KafkaStreamsConfigData {
	private String applicationID;
	private String inputTopicName;
	private String outputTopicName;
	private String stateFileLocation;
	private String wordCountStoreName;
}
