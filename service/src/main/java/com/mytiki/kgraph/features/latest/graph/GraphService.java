/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;

import com.arangodb.ArangoDBException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;
import java.util.*;

public class GraphService {
    private final GraphVertexLookup lookup;
    private final GraphEdgeRepository edgeRepository;

    public GraphService(GraphVertexLookup lookup, GraphEdgeRepository edgeRepository) {
        this.lookup = lookup;
        this.edgeRepository = edgeRepository;
    }

    public GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO> upsertEdge(
            String fromType, String fromValue, String toType, String toValue, String fingerprint)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GraphVertexDO fromDO = newVertex(fromType);
        fromDO.setValue(fromValue);
        fromDO = upsertVertex(fromDO);

        GraphVertexDO toDO = newVertex(toType);
        toDO.setValue(toValue);
        toDO = upsertVertex(toDO);

        return upsertEdge(fromDO, toDO, fingerprint);
    }

    public <T extends GraphVertexDO> Optional<T> getVertex(String type, String value){
        GraphVertexRepository<T> repository = getRepository(type);
        return repository.findByValue(value);
    }

    public <T extends GraphVertexDO> T upsertVertex(T vertex){
        Optional<T> saved;
        GraphVertexRepository<T> repository = getRepository(vertex.getCollection());
        try {
            saved = repository.findByValue(vertex.getValue());
        }catch (ArangoDBException ex){
            if(ex.getErrorNum() == 1203) saved = Optional.empty();
            else throw ex;
        }
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

    public String getSchema() {
        return GraphVertexLookup.schema;
    }

    private <F extends GraphVertexDO, T extends GraphVertexDO> GraphEdgeDO<F,T> upsertEdge(
            F from, T to, String fingerprint){
        Optional<GraphEdgeDO<F,T>> saved = edgeRepository.findByVertices(from.getRawId(), to.getRawId());
        GraphEdgeDO<F,T> edgeDO;
        if (saved.isPresent()) {
            edgeDO = saved.get();
            Set<String> fingerprintList = edgeDO.getFingerprints();
            fingerprintList.add(fingerprint);
            edgeDO.setFingerprints(fingerprintList);
            edgeDO.setModified(ZonedDateTime.now());
        } else {
            edgeDO = new GraphEdgeDO<>();
            edgeDO.setFrom(from);
            edgeDO.setTo(to);
            edgeDO.setFingerprints(new HashSet<>(List.of(fingerprint)));
            ZonedDateTime now = ZonedDateTime.now();
            edgeDO.setCreated(now);
            edgeDO.setModified(now);
        }
        return edgeRepository.save(edgeDO);
    }

    @SuppressWarnings("unchecked")
    private <T extends GraphVertexDO> GraphVertexRepository<T> getRepository(String type){
        return (GraphVertexRepository<T>) lookup.getRepository(type);
    }

    @SuppressWarnings("unchecked")
    private <T extends GraphVertexDO> T newVertex(String type)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<T> doClass = (Class<T>) lookup.getDOClass(type);
        return doClass.getConstructor().newInstance();
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
                if(updateField != null) field.set(merged, updateField);
                else field.set(merged, initialField);
            }
            return merged;
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            return initial;
        }
    }
}