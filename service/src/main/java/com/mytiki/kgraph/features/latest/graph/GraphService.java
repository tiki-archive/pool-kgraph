/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;

import com.arangodb.ArangoDBException;
import com.mytiki.kgraph.config.ConfigProperties;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;
import java.util.*;

public class GraphService {
    private final GraphVertexLookup lookup;
    private final GraphEdgeRepository edgeRepository;
    private final ConfigProperties configProperties;

    public GraphService(GraphVertexLookup lookup,
                        GraphEdgeRepository edgeRepository,
                        ConfigProperties configProperties) {
        this.lookup = lookup;
        this.edgeRepository = edgeRepository;
        this.configProperties = configProperties;
    }

    public GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO> upsertEdge(
            String fromType, String fromValue, String toType, String toValue, String fingerprint)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GraphVertexDO fromDO = vertexFromType(fromType);
        fromDO.setValue(fromValue);
        fromDO = insertVertex(fromDO);

        GraphVertexDO toDO = vertexFromType(toType);
        toDO.setValue(toValue);
        toDO = insertVertex(toDO);

        GraphEdgeDO<GraphVertexDO, GraphVertexDO> edge = new GraphEdgeDO();
        edge.setFingerprints(Set.of(fingerprint));
        edge.setFrom(fromDO);
        edge.setTo(toDO);

        return edgeRepository.upsert(edge);
    }

    public GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO> upsertEdgeAndVertex(
            String fromType, String fromValue, String toType, String toValue, String fingerprint)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GraphVertexDO fromDO = vertexFromType(fromType);
        fromDO.setValue(fromValue);
        fromDO = upsertVertex(fromDO);

        GraphVertexDO toDO = vertexFromType(toType);
        toDO.setValue(toValue);
        toDO = upsertVertex(toDO);

        GraphEdgeDO<GraphVertexDO, GraphVertexDO> edgeDO = new GraphEdgeDO<>();
        edgeDO.setFingerprints(Set.of(fingerprint));
        edgeDO.setFrom(fromDO);
        edgeDO.setTo(toDO);

        return edgeRepository.upsert(edgeDO);
    }

    public <T extends GraphVertexDO> Optional<T> getVertex(String type, String value){
        GraphVertexRepository<T> repository = getRepository(type);
        try {
            return repository.findByValue(value);
        }catch (ArangoDBException ex){
            if(ex.getErrorNum() == 1203) return Optional.empty();
            else throw ex;
        }
    }

    public <T extends GraphVertexDO> T upsertVertex(T vertex){
        GraphVertexRepository<T> repository = getRepository(vertex.getCollection());
        Optional<T> saved = getVertex(vertex.getCollection(), vertex.getValue());
        if(saved.isPresent()){
            vertex = merge(saved.get(), vertex);
            vertex.setModified(ZonedDateTime.now());
        }else{
            ZonedDateTime now = ZonedDateTime.now();
            vertex.setCreated(now);
            vertex.setModified(now);
        }
        return repository.save(vertex);
    }

    public <T extends GraphVertexDO> T insertVertex(T vertex){
        GraphVertexRepository<T> repository = getRepository(vertex.getCollection());
        Optional<T> saved = getVertex(vertex.getCollection(), vertex.getValue());
        if(saved.isEmpty()){
            ZonedDateTime now = ZonedDateTime.now();
            vertex.setCreated(now);
            vertex.setModified(now);
            return repository.save(vertex);
        }else
            return saved.get();
    }

    public String getSchema() {
        return lookup.getSchema();
    }

    public List<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>>
    findByFingerprint(String fingerprint){
        return edgeRepository.findByFingerprintsContains(fingerprint);
    }

    public List<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>>
    traverse(String type, String value, Integer depth){
        Optional<GraphVertexDO> start = getVertex(type, value);
        if(start.isPresent()) {
            return edgeRepository.traverse(start.get().getRawId(), depth, configProperties.getEpsilon());
        }else
            return List.of();
    }

    public List<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>>
    shortestPath(String startType, String startValue, String endType, String endValue){
        Optional<GraphVertexDO> start = getVertex(startType, startValue);
        Optional<GraphVertexDO> end = getVertex(endType, endValue);
        if(start.isPresent() && end.isPresent()) {
            return edgeRepository.shortestPath(
                    start.get().getRawId(), end.get().getRawId(), configProperties.getEpsilon());
        }else
            return List.of();
    }

    @SuppressWarnings("unchecked")
    public <T extends GraphVertexDO> T vertexFromType(String type)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<T> doClass = (Class<T>) lookup.getDOClass(type);
        return doClass.getConstructor().newInstance();
    }

    @SuppressWarnings("unchecked")
    private <T extends GraphVertexDO> GraphVertexRepository<T> getRepository(String type){
        return (GraphVertexRepository<T>) lookup.getRepository(type);
    }

    @SuppressWarnings("unchecked")
    private <T extends GraphVertexDO> T merge(T initial, T update){
        List<Field> fields = new ArrayList<>();
        if(initial.getClass().getSuperclass() != null)
            fields.addAll(Arrays.asList(initial.getClass().getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(initial.getClass().getDeclaredFields()));
        try {
            T merged = (T) initial.getClass().getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                Object initialField = field.get(initial);
                Object updateField = (update == null ? null : field.get(update));
                if(!field.getName().equals("collection") && !field.getName().equals("COLLECTION")) {
                    if (updateField != null) field.set(merged, updateField);
                    else field.set(merged, initialField);
                }
            }
            return merged;
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            return initial;
        }
    }
}
