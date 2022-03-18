/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;

import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.mytiki.kgraph.config.ConfigProperties;
import com.mytiki.kgraph.features.latest.vertex.VertexService;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@EnableArangoRepositories(EdgeConfig.PACKAGE_PATH)
public class EdgeConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".edge";

    @Bean
    public EdgeService edgeService(
            @Autowired EdgeRepository edgeRepository,
            @Autowired VertexService vertexService,
            @Autowired ConfigProperties configProperties){
        return new EdgeService(edgeRepository, vertexService, configProperties);
    }

    @Bean
    public EdgeController edgeController(@Autowired EdgeService addService){
        return new EdgeController(addService);
    }
}
