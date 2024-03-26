package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration  // Transforma esta classe em um Bean
// Pega o valor das propriedades no arquivo application.yml do Módulo twitter-to-kafka-service
@ConfigurationProperties(prefix = "twitter-to-kafka-service") // prefixo utilizado no arquivo application.yml
public class TwitterToKafkaServiceConfigData {
    private List<String> twitterKeywords;
    private String welcomeMessage;
    private Boolean enableMockTweets;
    private Long mockSleepMs;
    private Integer mockMinTweetLength;
    private Integer mockMaxTweetLength;
}
