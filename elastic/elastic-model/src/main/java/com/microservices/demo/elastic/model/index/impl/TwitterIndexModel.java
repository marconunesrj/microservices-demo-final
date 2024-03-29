package com.microservices.demo.elastic.model.index.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microservices.demo.elastic.model.index.IndexModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Builder
// Pega o valor da classe ElasticConfigData atributo indexName no módulo: app-config-data
@Document(indexName = "#{elasticConfigData.indexName}")  // Nome do indexador a ser utilizado
public class TwitterIndexModel implements IndexModel {

    // Veja a criação via Postman na collection: Elastic Twitter Index
    @JsonProperty
    private String id;
    @JsonProperty
    private Long userId;
    @JsonProperty
    private String text;

    // https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/object-mapping.html
    // Custom Date -> Use uuuu instead of yyyy
    // the purpose of using @Field annotation on temporal type like LocalDateTime
    // To convert it to elasticsearch field during index, format the date by using a pattern, to set the field type
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ssZZ")
    // Define o padrão de retorno do atributo: createdAt
    // Formats the Field when converting object to Json by using the pattern specified
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ssZZ")
    @JsonProperty
    private LocalDateTime createdAt;
}
