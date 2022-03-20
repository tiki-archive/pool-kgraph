/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.vertex;

import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(VertexLookup.class)
@EnableArangoRepositories(VertexConfig.PACKAGE_PATH)
public class VertexConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".vertex";

    @Bean
    public VertexService vertexService(
            @Autowired VertexLookup vertexLookup,
            @Autowired ObjectMapper objectMapper){
        return new VertexService(vertexLookup, objectMapper);
    }

    @Bean
    public VertexController vertexController(@Autowired VertexService vertexService){
        return new VertexController(vertexService);
    }
}
