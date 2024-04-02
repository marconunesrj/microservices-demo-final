package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
// para ler as propriedades do arquivo config-client-elastic_query.yml no módulo (Github): config-server-repository
@ConfigurationProperties(prefix = "elastic-query-config")
public class ElasticQueryConfigData {
    private String textField;  // Nome do campo na classe TwitterIndexModel do módulo: elastic-model
}
