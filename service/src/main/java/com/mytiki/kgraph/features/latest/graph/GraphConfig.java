/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;


import com.arangodb.ArangoDatabase;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.GraphEntity;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.core.ArangoOperations;
import com.mytiki.kgraph.config.ConfigProperties;
import com.mytiki.kgraph.utilities.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Import({GraphVertexLookup.class})
@EnableArangoRepositories(basePackages = {GraphConfig.PACKAGE_PATH})
public class GraphConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".graph";
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ArangoOperations operations;

    @Autowired
    private ConfigProperties properties;

    @Autowired
    private GraphVertexLookup lookup;

    @Bean
    public GraphService graphService(
            @Autowired GraphVertexLookup graphVertexLookup,
            @Autowired GraphEdgeRepository graphEdgeRepository){
        return new GraphService(graphVertexLookup, graphEdgeRepository);
    }

    @PostConstruct
    public void defineGraph(){
        Optional<String> dbString = operations.driver().getDatabases()
                .stream()
                .filter(s -> s.equals(properties.getDbName()))
                .findFirst();

        if(dbString.isEmpty()){
            logger.debug("No database " + properties.getDbName() + ". Trying to create it.");
            operations.driver().createDatabase(properties.getDbName());
        }

        ArangoDatabase db = operations.driver().db(properties.getDbName());
        Optional<GraphEntity> graph = db.getGraphs()
                .stream()
                .filter(g -> g.getName().equals(properties.getGraphName()))
                .findFirst();

        EdgeDefinition edgeDefinition = new EdgeDefinition();
        edgeDefinition.collection("edge");
        edgeDefinition.from(lookup.getVertices());
        edgeDefinition.to(lookup.getVertices());

        if(graph.isEmpty()){
            logger.debug("No graph " + properties.getGraphName() + ". Trying to create it.");
            db.graph(properties.getGraphName()).create(List.of(edgeDefinition));
        }else{
            Optional<EdgeDefinition> currentEdgeDefinition = graph.get().getEdgeDefinitions()
                    .stream()
                    .filter(e -> e.getCollection().equals("edge"))
                    .findFirst();
            if(
                    currentEdgeDefinition.isEmpty() ||
                    !currentEdgeDefinition.get().getFrom().equals(edgeDefinition.getFrom()) ||
                    !currentEdgeDefinition.get().getTo().equals(edgeDefinition.getTo())
            ){
                logger.debug("Vertices outdated  " + properties.getGraphName() + ". Trying to replace them");
                db.graph(properties.getGraphName()).replaceEdgeDefinition(edgeDefinition);
            }
        }
    }
}
