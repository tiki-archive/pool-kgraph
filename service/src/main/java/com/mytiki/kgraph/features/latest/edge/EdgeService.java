/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;


import com.mytiki.kgraph.config.ConfigProperties;
import com.mytiki.kgraph.features.latest.vertex.VertexDO;
import com.mytiki.kgraph.features.latest.vertex.VertexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EdgeService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EdgeRepository edgeRepository;
    private final VertexService vertexService;
    private final ConfigProperties configProperties;

    public EdgeService(
            EdgeRepository edgeRepository,
            VertexService vertexService,
            ConfigProperties configProperties) {
        this.edgeRepository = edgeRepository;
        this.vertexService = vertexService;
        this.configProperties = configProperties;
    }

    public Future<List<EdgeAO>> add(List<EdgeAO> body)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<EdgeDO<? extends VertexDO,? extends VertexDO>> edges = compress(body);
        return null;
    }

    public List<EdgeDO<? extends VertexDO,? extends VertexDO>> compress(List<EdgeAO> req)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Map<String, EdgeDO<? extends VertexDO, ? extends VertexDO>> edges = new HashMap<>();
        for(EdgeAO edge : req){
            String fKey = edge.getFrom().getType() + ":" + edge.getFrom().getValue();
            String tKey = edge.getTo().getType() + ":" + edge.getTo().getValue();
            String key = Stream.of(fKey, tKey).sorted().collect(Collectors.joining(":"));
            if(edges.containsKey(key)){
                Set<String> fps = new HashSet<>(edges.get(key).getFingerprints());
                fps.add(edge.getFingerprint());
                edges.get(key).setFingerprints(fps);
            }else {
                EdgeDO<? extends VertexDO, ? extends VertexDO> graphEdge = new EdgeDO<>();
                graphEdge.setFrom(vertexService.vertexFromType(edge.getFrom().getType()));
                graphEdge.getFrom().setValue(edge.getFrom().getValue());
                graphEdge.setTo(vertexService.vertexFromType(edge.getTo().getType()));
                graphEdge.getTo().setValue(edge.getTo().getValue());
                graphEdge.setFingerprints(Set.of(edge.getFingerprint()));
                edges.put(key, graphEdge);
            }
        }
        return new ArrayList<>(edges.values());
    }

    public EdgeDO<? extends VertexDO, ? extends VertexDO> upsertEdge(
            VertexDO from, VertexDO to, String fingerprint) {
        EdgeDO<VertexDO, VertexDO> edge = new EdgeDO<>();
        edge.setFingerprints(Set.of(fingerprint));
        edge.setFrom(from);
        edge.setTo(to);
        return edgeRepository.upsert(edge);
    }

    public List<EdgeDO<? extends VertexDO, ? extends VertexDO>>
    traverse(String type, String value, Integer depth){
        Optional<VertexDO> start = vertexService.getVertex(type, value);
        if(start.isPresent()) {
            return edgeRepository.traverse(start.get().getRawId(), depth, configProperties.getEpsilon());
        }else
            return List.of();
    }
}
