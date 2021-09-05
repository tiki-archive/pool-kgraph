/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;


import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({GraphVertexLookup.class})
@EnableArangoRepositories(basePackages = {GraphConfig.PACKAGE_PATH})
public class GraphConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".graph";

    @Bean
    public GraphService graphService(
            @Autowired GraphVertexLookup graphVertexLookup,
            @Autowired GraphEdgeRepository graphEdgeRepository){
        return new GraphService(graphVertexLookup, graphEdgeRepository);
    }
}
