/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;

import com.mytiki.kgraph.features.latest.graph.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class EdgeConfig {
    @Bean
    public EdgeService edgeService(@Autowired GraphService graphService){
        return new EdgeService(graphService);
    }

    @Bean
    public EdgeController edgeController(@Autowired EdgeService addService){
        return new EdgeController(addService);
    }
}
