/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.search;

import com.mytiki.kgraph.features.latest.graph.GraphEdgeDO;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import com.mytiki.kgraph.features.latest.graph.GraphVertexDO;

import java.util.List;
import java.util.stream.Collectors;

public class SearchService {


    /*
        1. we need to dynamically define the graph, if graph exists, check vertex collections, if missing add. use weightAttribute

        2. search given a start vertex and a depth -> standard traversal
        3. search by start, end, and prune (weighted) -> shortest path (we need a weight which is 1/qty)
        4. filter weight over 1/10


        add 2 new query methods to graph edge repo (may need path POJOs)
     */

    private final GraphService graphService;

    public SearchService(GraphService graphService) {
        this.graphService = graphService;
    }

    List<SearchAO> search(SearchAOVertex start, SearchAOVertex end, Integer depth){
        List<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>> edges;
        if(end != null)
            edges = graphService.shortestPath(start.getType(), start.getType(), end.getType(), end.getValue());
        else
            edges = graphService.traverse(start.getType(), start.getValue(), depth);
        return edges.stream().map(e -> {
            SearchAO ao = new SearchAO();
            SearchAOVertex aoFrom = new SearchAOVertex();
            aoFrom.setType(e.getFrom().getCollection());
            aoFrom.setValue(e.getFrom().getValue());
            ao.setFrom(aoFrom);
            SearchAOVertex aoTo = new SearchAOVertex();
            aoTo.setType(e.getTo().getCollection());
            aoTo.setValue(e.getTo().getValue());
            ao.setTo(aoTo);
            return ao;
        }).collect(Collectors.toList());
    }
}
