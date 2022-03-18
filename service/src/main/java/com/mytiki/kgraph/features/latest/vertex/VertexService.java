/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.vertex;

import com.arangodb.ArangoDBException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytiki.common.exception.ApiExceptionBuilder;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public class VertexService {
    private final ObjectMapper objectMapper;
    private final VertexLookup vertexLookup;

    public VertexService(VertexLookup vertexLookup, ObjectMapper objectMapper) {
        this.vertexLookup = vertexLookup;
        this.objectMapper = objectMapper;
    }

    public List<?> getVertexTypes(){
        String schema = vertexLookup.getSchema();
        try {
            return objectMapper.readValue(schema, List.class);
        } catch (JsonProcessingException e) {
            throw new ApiExceptionBuilder()
                    .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        }
    }

    public <T extends VertexDO> Optional<T> getVertex(String type, String value){
        VertexRepository<T> repository = getRepository(type);
        try {
            return repository.findByValue(value);
        }catch (ArangoDBException ex){
            if(ex.getErrorNum() == 1203) return Optional.empty();
            else throw ex;
        }
    }

    public <T extends VertexDO> T upsertVertex(T vertex){
        VertexRepository<T> repository = getRepository(vertex.getCollection());
        return repository.upsert(vertex);
    }

    @SuppressWarnings("unchecked")
    public <T extends VertexDO> T vertexFromType(String type)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<T> doClass = (Class<T>) vertexLookup.getDOClass(type);
        return doClass.getConstructor().newInstance();
    }

    @SuppressWarnings("unchecked")
    private <T extends VertexDO> VertexRepository<T> getRepository(String type){
        return (VertexRepository<T>) vertexLookup.getRepository(type);
    }
}
