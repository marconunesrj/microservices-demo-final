package com.microservices.demo.reactive.elastic.query.service.repository;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ElasticQueryRepository extends ReactiveCrudRepository<TwitterIndexModel, String> {

    // Flux -> é simplesmente um fluxo que pode emitir de zero a n elementos.
    // E em geral, Mono e Flux são elementos reativos onde Mono emite 0 a 1 elemento,
    // enquanto Flux emite de zero a n elementos.
    Flux<TwitterIndexModel> findByText(String text);
}
