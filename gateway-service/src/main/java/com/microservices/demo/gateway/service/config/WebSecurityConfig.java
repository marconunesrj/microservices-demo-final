package com.microservices.demo.gateway.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class WebSecurityConfig {

    // Permitiremos acesso a todos os microserviços, pois a segurança estará em cada um deles, visto
    // que o serviço pode ser chamado de outro lugar em vez de Gateway.
    // desabilitamos o csrf, pois temos apenas uma iteração de back-end com o serviço de Gateway.
    @Bean
    public SecurityWebFilterChain webFluxSecurityConfig(ServerHttpSecurity httpSecurity) {
        httpSecurity.authorizeExchange()
                .anyExchange()
                .permitAll();
        httpSecurity.csrf().disable();
        return httpSecurity.build();
    }
}
