package com.microservices.demo.elastic.query.web.client.config;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

// Esta classe serve para o LoadBalancer
@Configuration
// Colocado para que o Spring utilize esta implementação ao invés de outra que ela possua
@Primary
public class ElasticQueryServiceInstanceListSupplierConfig implements ServiceInstanceListSupplier {

    private final ElasticQueryWebClientConfigData.WebClient webClientConfig;

    public ElasticQueryServiceInstanceListSupplierConfig(ElasticQueryWebClientConfigData webClientConfigData) {
        this.webClientConfig = webClientConfigData.getWebClient();
    }

    // Pega o id do microservice elastic-query-service definido no arquivo config-client-elastic_query_web.yml
    // para ser utilizado no LoadBalancer
    @Override
    public String getServiceId() {
        return webClientConfig.getServiceId();
    }

    // Pega as instâncias do microservice elastic-query-service definidas no arquivo config-client-elastic_query_web.yml
    // para serem utilizadas no LoadBalancer
    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(
                webClientConfig.getInstances().stream()
                        .map(instance ->
                                new DefaultServiceInstance(
                                        instance.getId(),
                                        getServiceId(),
                                        instance.getHost(),
                                        instance.getPort(),
                                        false
                                )).collect(Collectors.toList()));
    }
}
