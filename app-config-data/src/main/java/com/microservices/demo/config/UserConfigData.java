package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
// para ler as propriedades do arquivo config-client-elastic_query.yml no módulo (Github): config-server-repository
@ConfigurationProperties(prefix = "user-config")
public class UserConfigData {
    private String username;
    private String password;
    private String[] roles;
}
