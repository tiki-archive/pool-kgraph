/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;


import com.mytiki.common.exception.ApiExceptionFactory;
import com.mytiki.kgraph.features.latest.graph.GraphEdgeDO;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import com.mytiki.kgraph.features.latest.graph.GraphVertexDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EdgeService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final GraphService graphService;

    public EdgeService(GraphService graphService) {
        this.graphService = graphService;
    }

    @Async
    public Future<List<EdgeAO>> add(List<EdgeAO> body){
       try {
           logger.info("Edges Added Request: " + body.size());
           for(EdgeAO edge : body) {
               graphService.upsertEdge(
                       edge.getFrom().getType(),
                       edge.getFrom().getValue(),
                       edge.getTo().getType(),
                       edge.getTo().getValue(),
                       edge.getFingerprint());
           }
           logger.info("Edges Added: " + body.size());
           return new AsyncResult<>(body);
       }catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
           throw ApiExceptionFactory.exception(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
       }
    }

    public List<GraphEdgeDO<GraphVertexDO,GraphVertexDO>> compress(List<EdgeAO> req)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Map<String, GraphEdgeDO<GraphVertexDO, GraphVertexDO>> edges = new HashMap<>();
        for(EdgeAO edge : req){
            String fKey = edge.getFrom().getType() + ":" + edge.getFrom().getValue();
            String tKey = edge.getTo().getType() + ":" + edge.getTo().getValue();
            String key = Stream.of(fKey, tKey).sorted().collect(Collectors.joining(":"));
            if(edges.containsKey(key)){
                Set<String> fps = new HashSet<>(edges.get(key).getFingerprints());
                fps.add(edge.getFingerprint());
                edges.get(key).setFingerprints(fps);
            }else {
                GraphEdgeDO<GraphVertexDO, GraphVertexDO> graphEdge = new GraphEdgeDO<>();
                GraphVertexDO toVertex = graphService.vertexFromType(edge.getTo().getType());
                toVertex.setValue(edge.getTo().getValue());
                GraphVertexDO fromVertex = graphService.vertexFromType(edge.getFrom().getType());
                fromVertex.setValue(edge.getFrom().getValue());
                graphEdge.setFrom(fromVertex);
                graphEdge.setTo(toVertex);
                graphEdge.setFingerprints(Set.of(edge.getFingerprint()));
                edges.put(key, graphEdge);
            }
        }
        return new ArrayList<>(edges.values());
    }

}
