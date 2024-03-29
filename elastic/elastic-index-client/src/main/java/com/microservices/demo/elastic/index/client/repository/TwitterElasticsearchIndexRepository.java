package com.microservices.demo.elastic.index.client.repository;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// Pode usar esta forma ou como estão anotadas as classes: TwitterElasticIndexClient e TwitterElasticRepositoryIndexClient
// com @ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "false") e
// @ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true), respectivamente
//@Primary  // Give higher preference to a Bean
@Repository
public interface TwitterElasticsearchIndexRepository extends ElasticsearchRepository<TwitterIndexModel, String> {
}
