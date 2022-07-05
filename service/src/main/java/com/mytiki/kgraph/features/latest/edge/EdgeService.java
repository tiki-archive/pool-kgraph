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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    public List<EdgeAO> add(List<EdgeAO> body) {
        List<EdgeDO<? extends VertexDO, ? extends VertexDO>> edges = compress(body);
        if(edges.size() > 0) {
            Set<String> collections = new HashSet<>();
            for (EdgeDO<? extends VertexDO, ? extends VertexDO> edge : edges) {
                collections.add(edge.getFrom().getCollection());
                collections.add(edge.getTo().getCollection());
            }
            Map<String, List<? extends VertexDO>> vmap = new HashMap<>();
            collections.forEach(collection -> {
                List<? extends VertexDO> vlist = Stream.concat(
                        edges.stream()
                                .filter(e -> e.getFrom().getCollection().equals(collection))
                                .map(EdgeDO::getFrom),
                        edges.stream()
                                .filter(e -> e.getTo().getCollection().equals(collection))
                                .map(EdgeDO::getTo))
                        .collect(Collectors.toList());
                vmap.put(collection, vlist);
            });
            vmap.forEach((k,v) -> vertexService.insert(v));
            return edgeRepository.upsertAll(edges).stream().map(e -> new EdgeAO(
                    new EdgeAOVertex(e.getFrom().getCollection(), e.getFrom().getId()),
                    new EdgeAOVertex(e.getTo().getCollection(), e.getTo().getId()),
                    null
            )).collect(Collectors.toList());
        }else
            return List.of();
    }

    public <F extends VertexDO, T extends VertexDO> EdgeDO<F, T> upsert(F from, T to, String fingerprint) {
        return edgeRepository.upsert(from.getDbId(), to.getDbId(), Set.of(fingerprint));
    }

    public List<EdgeDO<? extends VertexDO, ? extends VertexDO>>
    traverse(String type, String id, Integer depth){
        try {
            VertexDO vertex = vertexService.fromType(type);
            vertex.setId(id);
            return edgeRepository.traverse(vertex.getDbId(), depth);
        }catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            logger.warn("Bad vertex", e);
            return List.of();
        }
    }

    public List<EdgeAO> searchSubject(String company, ZonedDateTime start, ZonedDateTime end){
        List<EdgeDO<? extends VertexDO, ? extends VertexDO>> edges =
                edgeRepository.searchSubject(company,
                        start.format(DateTimeFormatter.ISO_LOCAL_DATE),
                        end.format(DateTimeFormatter.ISO_LOCAL_DATE));
        return edges.stream().map(e -> new EdgeAO(
                new EdgeAOVertex(e.getFrom().getCollection(), e.getFrom().getId()),
                new EdgeAOVertex(e.getTo().getCollection(), e.getTo().getId()),
                null))
                .collect(Collectors.toList());
    }

    private List<EdgeDO<? extends VertexDO,? extends VertexDO>> compress(List<EdgeAO> req){
        Map<String, EdgeDO<? extends VertexDO, ? extends VertexDO>> edges = new HashMap<>();
        for(EdgeAO edge : req){
            String fKey = edge.getFrom().getType() + ":" + edge.getFrom().getId();
            String tKey = edge.getTo().getType() + ":" + edge.getTo().getId();
            String key = Stream.of(fKey, tKey).sorted().collect(Collectors.joining(":"));
            if(edges.containsKey(key)){
                Set<String> fps = new HashSet<>(edges.get(key).getFingerprints());
                fps.add(edge.getFingerprint());
                edges.get(key).setFingerprints(fps);
            }else {
                EdgeDO<? extends VertexDO, ? extends VertexDO> graphEdge = new EdgeDO<>();
                try {
                    graphEdge.setFrom(vertexService.fromType(edge.getFrom().getType()));
                    graphEdge.getFrom().setId(edge.getFrom().getId());
                    graphEdge.setTo(vertexService.fromType(edge.getTo().getType()));
                    graphEdge.getTo().setId(edge.getTo().getId());
                    graphEdge.setFingerprints(Set.of(edge.getFingerprint()));
                    edges.put(key, graphEdge);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    logger.warn("Bad vertex", e);
                }
            }
        }
        return new ArrayList<>(edges.values());
    }
}
