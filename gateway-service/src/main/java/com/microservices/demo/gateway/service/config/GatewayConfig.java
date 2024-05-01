package com.microservices.demo.gateway.service.config;

import com.microservices.demo.config.GatewayServiceConfigData;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Configuration
public class GatewayConfig {

    // Os valores para esta classe vem do arquivo config-client-gateway.yml
    private final GatewayServiceConfigData gatewayServiceConfigData;

    private static final String HEADER_FOR_KEY_RESOLVER = "Authorization";

    public GatewayConfig(GatewayServiceConfigData configData) {
        this.gatewayServiceConfigData = configData;
    }

    // Será utilizado pelo filtro limitador de taxa. Se retornarmos uma constante aqui,
    // ele aplicará o limite de taxa a todo o aplicativo, não por usuário ou cliente.
    // Portanto, usaremos o cabeçalho Authorization aqui, para aplicar a filtragem, para
    // que clientes diferentes não usem tokens uns dos outros. Definimos o nome do Bean aqui,
    // e lembre-se, nós usamos na configuração do limitador de taxa, ver arquivo config-client-gateway.yml.
    @Bean(name = "authHeaderResolver")
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(Objects.requireNonNull(exchange
                .getRequest().getHeaders().getFirst(HEADER_FOR_KEY_RESOLVER)));
    }

    /*
    Circuit breaker
    Definida a configuração necessária a ser usada para decidir abrir o estado de um serviço,
    que fará com que seja redirecionado para o controlador de fallback.
     */
    @Bean
    Customizer<ReactiveResilience4JCircuitBreakerFactory> circuitBreakerFactoryCustomizer() {
        return reactiveResilience4JCircuitBreakerFactory ->
                reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                // para evitar esperas indefinidas.
                                .timeoutDuration(Duration.ofMillis(gatewayServiceConfigData.getTimeoutMs()))
                                .build()
                        )
                        // Definimos o limite de falha, limite de taxa de chamada lenta,
                        // limite de duração de chamada lenta, número permitido de chamadas em
                        // estado semi-aberto, tamanho da janela deslizante, número mínimo de
                        // chamadas e tempo de espera em estado aberto.
                        .circuitBreakerConfig(CircuitBreakerConfig.custom()
                                .failureRateThreshold(gatewayServiceConfigData.getFailureRateThreshold())
                                .slowCallRateThreshold(gatewayServiceConfigData.getSlowCallRateThreshold())
                                .slowCallDurationThreshold(Duration.ofMillis(gatewayServiceConfigData
                                        .getSlowCallDurationThreshold()))
                                .permittedNumberOfCallsInHalfOpenState(gatewayServiceConfigData
                                        .getPermittedNumOfCallsInHalfOpenState())
                                .slidingWindowSize(gatewayServiceConfigData.getSlidingWindowSize())
                                .minimumNumberOfCalls(gatewayServiceConfigData.getMinNumberOfCalls())
                                .waitDurationInOpenState(Duration.ofMillis(gatewayServiceConfigData
                                        .getWaitDurationInOpenState()))
                                .build()
                        )
                        .build()
                );
    }
}
