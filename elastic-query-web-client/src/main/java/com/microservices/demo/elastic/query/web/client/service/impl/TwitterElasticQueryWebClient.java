package com.microservices.demo.elastic.query.web.client.service.impl;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.elastic.query.web.client.exception.ElasticQueryWebClientException;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.elastic.query.web.client.service.ElasticQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TwitterElasticQueryWebClient implements ElasticQueryWebClient {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryWebClient.class);

    private final WebClient.Builder webClientBuilder;

    // Vem do módulo app-config-data
    private final ElasticQueryWebClientConfigData elasticQueryWebClientConfigData;

    /* A anotação @Qualifier é utilizada para injetar uma implementação específica de uma interface.
       Precisamos disso aqui porque há a implementação padrão do construtor WebClient do construtor
       de cliente Web da biblioteca Spring.
       Mas aqui quero injetar nosso construtor personalizado
       Veja classe WebClientConfig
    */
    public TwitterElasticQueryWebClient(@Qualifier("webClientBuilder") WebClient.Builder clientBuilder,
                                        ElasticQueryWebClientConfigData webClientConfigData) {
        this.webClientBuilder = clientBuilder;
        this.elasticQueryWebClientConfigData = webClientConfigData;
    }

    @Override
    public List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel) {
        LOG.info("Querying by text {}", requestModel.getText());
        /* Como esta solicitação retornará mais de um dado, usarei o método bodyToFlux com parâmetro
         Modelo de resposta de cliente web de consulta elástica (ElasticQueryWebClientResponseModel).
         Depois colete-o em uma lista.
         No final, chamamos o método de bloqueio, pois desejamos obter a resposta imediatamente,
         ou seja, de forma síncrona neste cenário. Sem esta chamada de método de bloqueio, ele
         não retornará imediatamente.

         É preciso criar alguns métodos de retorno de chamada que serão chamados posteriormente de forma
         assíncrona.
         */
        return getWebClient(requestModel)
                .bodyToFlux(ElasticQueryWebClientResponseModel.class)
                .collectList()
                .block();
    }

    /*
    BodyInserters é uma interface responsável por preencher um corpo de mensagem de saída HTTP
    reativo com um dado de mensagem de saída e um contexto. E o publicador (Publisher) que é usado
    com o método fromPublisher é um componente reativo e fornece elementos potencialmente ilimitados.
    Como o cliente Web é um cliente reativo e trabalha com tipos reativos, neste cliente Web reativo eu
    vou precisar trabalhar com tipos reativos como Mono para um único dado e Flux para uma lista de dados.
     */
    private WebClient.ResponseSpec getWebClient(ElasticQueryWebClientRequestModel requestModel) {
        return webClientBuilder
                .build()
                .method(HttpMethod.valueOf(elasticQueryWebClientConfigData.getQueryByText().getMethod()))
                .uri(elasticQueryWebClientConfigData.getQueryByText().getUri())
                .accept(MediaType.valueOf(elasticQueryWebClientConfigData.getQueryByText().getAccept()))
                .body(BodyInserters.fromPublisher(Mono.just(requestModel), createParameterizedTypeReference()))
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated!")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        cr -> Mono.just(new ElasticQueryWebClientException(cr.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())));
    }


    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
