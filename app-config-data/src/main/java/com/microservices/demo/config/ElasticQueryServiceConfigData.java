package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
// para ler as propriedades do arquivo config-client-elastic_query.yml e config-client-reactive_elastic_query.yml no módulo (Github): config-server-repository
@ConfigurationProperties(prefix = "elastic-query-service")
public class ElasticQueryServiceConfigData {
    private String version;
    private String customAudience;
    private Long backPressureDelayMs;
    private WebClient webClient;
    private Query queryFromKafkaStateStore;
    private Query queryFromAnalyticsDatabase;

    @Data
    public static class WebClient {
        private Integer connectTimeoutMs;
        private Integer readTimeoutMs;
        private Integer writeTimeoutMs;
        private Integer maxInMemorySize;
        private String contentType;
        private String acceptType;
        private String queryType;
    }

    @Data
    public static class Query {
        private String method;
        private String accept;
        private String uri;
    }
}
