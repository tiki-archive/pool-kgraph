/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;

import com.arangodb.ArangoDBException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GraphService {
    private final GraphVertexLookup lookup;
    private final GraphEdgeRepository edgeRepository;

    public GraphService(GraphVertexLookup lookup, GraphEdgeRepository edgeRepository) {
        this.lookup = lookup;
        this.edgeRepository = edgeRepository;
    }

    public GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO> upsert(
            String fromName, String fromValue, String toName, String toValue, String fingerprint)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GraphVertexDO fromDO = newVertex(fromName);
        fromDO.setValue(fromValue);
        fromDO = upsertVertex(fromDO, getRepository(fromName));

        GraphVertexDO toDO = newVertex(toName);
        toDO.setValue(toValue);
        toDO = upsertVertex(toDO, getRepository(toName));

        return upsertEdge(fromDO, toDO, fingerprint);
    }

    //TODO flexible body params.
    private <T extends GraphVertexDO> T upsertVertex(T vertex, GraphVertexRepository<T> repository) {
        Optional<T> saved;
        try {
             saved = repository.findByValue(vertex.getValue());
        }catch (ArangoDBException ex){
            if(ex.getErrorNum() == 1203) saved = Optional.empty();
            else throw ex;
        }
        saved.ifPresent(v -> vertex.setId(v.getId()));
        return repository.save(vertex);
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
        } else {
            edgeDO = new GraphEdgeDO<>();
            edgeDO.setFrom(from);
            edgeDO.setTo(to);
            edgeDO.setFingerprints(new HashSet<>(List.of(fingerprint)));
        }
        return edgeRepository.save(edgeDO);
    }

    private <T extends GraphVertexDO> GraphVertexRepository<T> getRepository(String name){
        return (GraphVertexRepository<T>) lookup.getRepository(name);
    }

    private <T extends GraphVertexDO> T newVertex(String name)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<T> doClass = (Class<T>) lookup.getDOClass(name);
        return doClass.getConstructor().newInstance();
    }
}
