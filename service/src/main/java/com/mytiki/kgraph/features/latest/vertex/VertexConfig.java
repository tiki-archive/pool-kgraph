/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.vertex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class VertexConfig {
    @Bean
    public VertexService vertexService(
            @Autowired GraphService graphService,
            @Autowired ObjectMapper objectMapper){
        return new VertexService(graphService, objectMapper);
    }

    @Bean
    public VertexController vertexController(@Autowired VertexService vertexService){
        return new VertexController(vertexService);
    }
}
