package com.microservices.demo.elastic.query.client.repository;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// ElasticsearchRepository ->
//   Provides convenient methods to run against elasticsearch, but cannot be used to create complex Queries.
//   Can add method definitions to interface by setting correct name for property to search
@Repository
public interface TwitterElasticsearchQueryRepository extends ElasticsearchRepository<TwitterIndexModel, String> {

    List<TwitterIndexModel> findByText(String text);
}
