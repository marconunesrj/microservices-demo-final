package com.microservices.demo.kafka.to.elastic.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication  // Torná-la um iniciador de inicialização do Spring
@ComponentScan(basePackages = "com.microservices.demo")  // Necessário para encontrar os Beans em outros Módulos
public class KafkaToElasticServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaToElasticServiceApplication.class, args);
    }
}

