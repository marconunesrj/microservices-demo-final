package com.microservices.demo.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// RepresentationModel -> implements HATEOS links
public class ElasticQueryServiceResponseModel extends RepresentationModel<ElasticQueryServiceResponseModel> {
    private String id;
    private Long userId;
    private String text;
    private LocalDateTime createdAt;
}
