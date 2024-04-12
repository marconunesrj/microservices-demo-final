package com.microservices.demo.elastic.query.web.client.config;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

// elastic-query-service -> Mesmo valor atribuído a propriedade elastic-query-web-client.service-id
// no arquivo config-client-elastic_query_web -> Veja a classe ElasticQueryServiceListSupplierConfig
@Configuration
@LoadBalancerClient(name = "elastic-query-service", configuration = ElasticQueryServiceInstanceListSupplierConfig.class)
public class WebClientConfig {

    // Utilizando a inner class WebClient da classe ElasticQueryWebClientConfigData
    private final ElasticQueryWebClientConfigData.WebClient elasticQueryWebClientConfigData;

    // Este valor vem do arquivo config-client-elastic_query_web.yml
    @Value("${security.default-client-registration-id}")
    private String defaultClientRegistrationId;

    public WebClientConfig(ElasticQueryWebClientConfigData webClientConfigData) {
        this.elasticQueryWebClientConfigData = webClientConfigData.getWebClient();
    }

    // @LoadBalanced -> Veja a classe ElasticQueryServiceListSupplierConfig
    // Funciona com Builder
    // Utilizando uma anotação com qualificador "webClientBuilder" utilizado na classe TwitterElasticQueryWebClient
    @LoadBalanced
    @Bean("webClientBuilder")
    WebClient.Builder webClientBuilder(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository
    ) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                        clientRegistrationRepository,
                        oAuth2AuthorizedClientRepository);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        oauth2.setDefaultClientRegistrationId(defaultClientRegistrationId);

        return WebClient.builder()
                .baseUrl(elasticQueryWebClientConfigData.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, elasticQueryWebClientConfigData.getContentType())
                .defaultHeader(HttpHeaders.ACCEPT, elasticQueryWebClientConfigData.getAcceptType())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTcpClient())))
                .apply(oauth2.oauth2Configuration())
                // Definindo o tamanho máximo de memória para evitar erros ao navegar por dados grandes no navegador
                .codecs(clientCodecConfigurer ->
                        clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(elasticQueryWebClientConfigData.getMaxInMemorySize()));
    }

    // Definindo os Timeouts explicitamente
    private TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, elasticQueryWebClientConfigData.getConnectTimeoutMs())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(
                            new ReadTimeoutHandler(elasticQueryWebClientConfigData.getReadTimeoutMs(),
                                    TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(
                            new WriteTimeoutHandler(elasticQueryWebClientConfigData.getWriteTimeoutMs(),
                                    TimeUnit.MILLISECONDS));
                });
    }
}

