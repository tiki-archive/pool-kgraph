/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.add;

import com.mytiki.kgraph.features.latest.graph.GraphEdgeRepository;
import com.mytiki.kgraph.features.latest.graph.GraphVertexLookup;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class AddConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".add";

    @Bean
    public AddService addService(
            @Autowired GraphVertexLookup graphVertexLookup,
            @Autowired GraphEdgeRepository graphEdgeRepository){
        return new AddService(graphVertexLookup, graphEdgeRepository);
    }

    @Bean
    public AddController addController(@Autowired AddService addService){
        return new AddController(addService);
    }
}
