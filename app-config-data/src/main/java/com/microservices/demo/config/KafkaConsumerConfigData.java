package com.microservices.demo.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
// para ler as informações do arquivo config-client-kafka_to_elastic.yml no módulo (Github): config-server-repository
@ConfigurationProperties(prefix = "kafka-consumer-config")
// Propriedades de configuração do KafkaConsumerConfig class no módulo: kafka-consumer
public class KafkaConsumerConfigData {
    private String keyDeserializer;
    private String valueDeserializer;
    private String consumerGroupId;
    private String autoOffsetReset;
    private String specificAvroReaderKey;
    private String specificAvroReader;
    private Boolean batchListener;
    private Boolean autoStartup;
    private Integer concurrencyLevel;
    private Integer sessionTimeoutMs;
    private Integer heartbeatIntervalMs;
    private Integer maxPollIntervalMs;
    private Integer maxPollRecords;
    private Integer maxPartitionFetchBytesDefault;
    private Integer maxPartitionFetchBytesBoostFactor;
    private Long pollTimeoutMs;
}
