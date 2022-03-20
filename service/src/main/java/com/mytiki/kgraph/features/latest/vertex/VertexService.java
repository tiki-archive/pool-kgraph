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
import java.util.*;

public class VertexService {
    private final ObjectMapper objectMapper;
    private final VertexLookup vertexLookup;

    public VertexService(VertexLookup vertexLookup, ObjectMapper objectMapper) {
        this.vertexLookup = vertexLookup;
        this.objectMapper = objectMapper;
    }

    public List<?> schema(){
        String schema = vertexLookup.getSchema();
        try {
            return objectMapper.readValue(schema, List.class);
        } catch (JsonProcessingException e) {
            throw new ApiExceptionBuilder()
                    .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        }
    }

    public <T extends VertexDO> Optional<T> get(String type, String id){
        VertexRepository<T> repository = getRepository(type);
        try {
            return repository.findById(id);
        }catch (ArangoDBException ex){
            if(ex.getErrorNum() == 1203) return Optional.empty();
            else throw ex;
        }
    }

    public <T extends VertexDO> T upsert(T vertex){
        VertexRepository<T> repository = getRepository(vertex.getCollection());
        return repository.upsert(vertex);
    }

    public List<VertexDO> insert(List<VertexDO> vertices){
        Map<String, Set<VertexDO>> vertexMap = new HashMap<>();
        for (VertexDO vertex : vertices) {
            String collection = vertex.getCollection();
            if (vertexMap.containsKey(collection)) {
                Set<VertexDO> vset = new HashSet<>(vertexMap.get(collection));
                vset.add(vertex);
                vertexMap.replace(collection, vset);
            }else
                vertexMap.put(collection, Set.of(vertex));
        }
        List<VertexDO> inserted = new ArrayList<>();
        vertexMap.forEach((k,v) -> inserted.addAll(getRepository(k).insertAll(new ArrayList<>(v))));
        return inserted;
    }

    @SuppressWarnings("unchecked")
    public <T extends VertexDO> T fromType(String type)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<T> doClass = (Class<T>) vertexLookup.getDOClass(type);
        return doClass.getConstructor().newInstance();
    }

    @SuppressWarnings("unchecked")
    private <T extends VertexDO> VertexRepository<T> getRepository(String type){
        return (VertexRepository<T>) vertexLookup.getRepository(type);
    }
}
