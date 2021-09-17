/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.search;

import com.mytiki.kgraph.features.latest.graph.GraphService;

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

    void search(){
        graphService.search();
    }
}
