package com.microservices.demo.kafka.admin.config;

import com.microservices.demo.config.KafkaConfigData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;

@EnableRetry  // Habilita a lógica de repetição (Retry)
@Configuration
public class KafkaAdminConfig {

    private final KafkaConfigData kafkaConfigData;  // Estas propriedades vem do arquivo application.yml

    public KafkaAdminConfig(KafkaConfigData configData) {
        this.kafkaConfigData = configData;
    }

    // Kafka AdminClient -> Manage and inspect brokers, topics and configurations
    @Bean
    public AdminClient adminClient() {
        // Criar e Listar os tópicos Kafka programaticamente
        return AdminClient.create(Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
                kafkaConfigData.getBootstrapServers()));
    }
}
