package com.microservices.demo.elastic.query.client.util;

import com.microservices.demo.elastic.model.index.IndexModel;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component  // Torna esta classe um Bean gerenciado pelo Spring
public class ElasticQueryUtil<T extends IndexModel> {

    public Query getSearchQueryById(String id) {
        // NativeSearchQueryBuilder -> Provides methods like withIds, withQuery
        // to create a spring data elasticsearch Query object
        return new NativeSearchQueryBuilder()
                .withIds(Collections.singleton(id))
                .build();
    }

    public Query getSearchQueryByFieldText(String field, String text) {
        return new NativeSearchQueryBuilder()
                // BoolQueryBuilder & QueryBuilders -> Classes in elastic core library to create queries
                // like match, term, matchAll etc.
                .withQuery(new BoolQueryBuilder()
                        .must(QueryBuilders.matchQuery(field, text)))
                .build();
    }

    public Query getSearchQueryForAll() {
        return new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder()
                        .must(QueryBuilders.matchAllQuery()))
                .build();
    }
}
