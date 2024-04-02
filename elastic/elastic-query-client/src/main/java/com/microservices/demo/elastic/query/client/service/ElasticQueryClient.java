package com.microservices.demo.elastic.query.client.service;

import com.microservices.demo.elastic.model.index.IndexModel;

import java.util.List;

// IndexModel -> vem do módulo elastic-model
public interface ElasticQueryClient<T extends IndexModel> {

    T getIndexModelById(String id);

    List<T> getIndexModelByText(String text);

    List<T> getAllIndexModels();
}
