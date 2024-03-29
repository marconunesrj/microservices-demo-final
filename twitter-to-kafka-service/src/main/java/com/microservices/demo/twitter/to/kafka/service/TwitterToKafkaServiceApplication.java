package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.twitter.to.kafka.service.init.StreamInitializer;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication  // Torná-la um iniciador de inicialização do Spring
@ComponentScan(basePackages = "com.microservices.demo")  // Necessário para encontrar os Beans em outros Módulos
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

    // Vai pegar a implementação Mockada, pois no arquivo application.yml a propriedade enable-mock-tweets: true
    private final StreamRunner streamRunner;

    // Utilizar a interface para que o spring utilize a implementação correta
    private final StreamInitializer streamInitializer;

    // OBS: É melhor utilizar injeção de dependências via Construtor,
    // porque pode utilizar de objetos imutáveis (final), tread safe e mais rápido pois não utiliza reflexão
    public TwitterToKafkaServiceApplication(StreamRunner runner, StreamInitializer initializer) {
        this.streamRunner = runner;
        this.streamInitializer = initializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("App starts...");
        streamInitializer.init();
        streamRunner.start();
    }
}
