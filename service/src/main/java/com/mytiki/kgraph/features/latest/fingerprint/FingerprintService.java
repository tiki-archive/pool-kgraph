/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.fingerprint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytiki.kgraph.features.latest.graph.GraphEdgeDO;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import com.mytiki.kgraph.features.latest.graph.GraphVertexDO;

import java.util.*;

public class FingerprintService {
    private final ObjectMapper objectMapper;
    private final GraphService graphService;

    public FingerprintService(GraphService graphService, ObjectMapper objectMapper) {
        this.graphService = graphService;
        this.objectMapper = objectMapper;
    }

    public FingerprintAORsp find(FingerprintAOReq req){
        Map<String, Set<String>> edgeFpMap = new HashMap<>();
        Map<String, GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>> edgeDOMap = new HashMap<>();
        req.getFingerprints().forEach(fp -> {
           List<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>> edges =
                   graphService.findByFingerprint(fp);
           edges.forEach(edge -> {
               edgeDOMap.put(edge.getId(), edge);
               if(edgeFpMap.containsKey(edge.getId())) {
                   Set<String> fps = new HashSet<>(edgeFpMap.get(edge.getId()));
                   fps.add(fp);
                   edgeFpMap.put(edge.getId(), fps);
               }
               else
                   edgeFpMap.put(edge.getId(),Set.of(fp));
           });
        });

        FingerprintAORsp rsp = new FingerprintAORsp();
        rsp.setFingerprints(req.getFingerprints());
        Set<FingerprintAORspEdge> rspEdges = new HashSet<>(edgeDOMap.size());
        edgeDOMap.forEach((id,edge) -> {
            FingerprintAORspEdge rspEdge = new FingerprintAORspEdge();
            rspEdge.setFingerprints(edgeFpMap.get(id));
            TypeReference<Map<String, Object>> ref = new TypeReference<>() {};
            rspEdge.setFrom(sanitizeVertex(objectMapper.convertValue(edge.getFrom(), ref)));
            rspEdge.setTo(sanitizeVertex(objectMapper.convertValue(edge.getTo(), ref)));
            rspEdges.add(rspEdge);
        });
        rsp.setEdges(rspEdges);
        return rsp;
    }

    private Map<String, Object> sanitizeVertex(Map<String, Object> map){
        map.remove("id");
        map.remove("rawId");
        map.put("type", map.get("collection"));
        map.remove("collection");
        return map;
    }
}
