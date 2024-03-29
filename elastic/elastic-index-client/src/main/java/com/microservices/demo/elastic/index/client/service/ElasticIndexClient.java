package com.microservices.demo.elastic.index.client.service;

import com.microservices.demo.elastic.model.index.IndexModel;

import java.util.List;

// IndexModel -> vem do módulo elastic-model
public interface ElasticIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
