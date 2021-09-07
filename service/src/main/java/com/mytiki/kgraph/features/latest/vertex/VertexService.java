/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.vertex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytiki.common.exception.ApiExceptionBuilder;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import org.springframework.http.HttpStatus;

import java.util.List;

public class VertexService {

    private final ObjectMapper objectMapper;
    private final GraphService graphService;

    public VertexService(GraphService graphService, ObjectMapper objectMapper) {
        this.graphService = graphService;
        this.objectMapper = objectMapper;
    }

    public List<?> getVertexTypes(){
        String schema = graphService.getSchema();
        try {
            return objectMapper.readValue(schema, List.class);
        } catch (JsonProcessingException e) {
            throw new ApiExceptionBuilder()
                    .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        }
    }
}
