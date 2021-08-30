/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.add;


import com.arangodb.springframework.repository.ArangoRepository;
import com.mytiki.kgraph.features.latest.graph.GraphEdgeDO;
import com.mytiki.kgraph.features.latest.graph.GraphEdgeRepository;
import com.mytiki.kgraph.features.latest.graph.GraphVertexDO;
import com.mytiki.kgraph.features.latest.graph.GraphVertexLookup;

import java.lang.reflect.InvocationTargetException;

public class AddService {
    private final GraphVertexLookup lookup;
    private final GraphEdgeRepository edgeRepository;

    public AddService(GraphVertexLookup lookup, GraphEdgeRepository edgeRepository) {
        this.lookup = lookup;
        this.edgeRepository = edgeRepository;
    }


    public AddAO process(AddAO req)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GraphVertexDO fromDO = upsertVertex(req.getFrom());
        GraphVertexDO toDO = upsertVertex(req.getTo());
        upsertEdge(fromDO, toDO, req.getFingerprint());
        return req;
    }

    //TODO implement update, insert works
    //TODO move this down into GraphService
    private GraphVertexDO upsertVertex(AddAOVertex vertex) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ArangoRepository repository = lookup.getRepository(vertex.getType());
        Class<?> doClass = lookup.getDOClass(vertex.getType());
        GraphVertexDO doObject = (GraphVertexDO) doClass.getConstructor().newInstance();
        doObject.setName(vertex.getName());
        return (GraphVertexDO) repository.save(doObject);
    }

    //TODO implement update, insert works
    //TODO add fingerprint list and qty
    private GraphEdgeDO upsertEdge(GraphVertexDO from, GraphVertexDO to, String fingerprint){
        GraphEdgeDO edgeDO = new GraphEdgeDO();
        edgeDO.setFrom(from);
        edgeDO.setTo(to);
        return edgeRepository.save(edgeDO);
    }

}
