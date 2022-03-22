/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.config;

import com.arangodb.ArangoDatabase;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.GraphEntity;
import com.arangodb.model.GraphCreateOptions;
import com.arangodb.springframework.core.ArangoOperations;
import com.mytiki.kgraph.features.latest.vertex.VertexLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

public class ConfigGraph {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${arangodb.replication:1}")
    Integer replication;

    @Autowired
    private ArangoOperations operations;

    @Autowired
    private ConfigProperties properties;

    @Autowired
    private VertexLookup vertexLookup;

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
        edgeDefinition.from(vertexLookup.getVertices());
        edgeDefinition.to(vertexLookup.getVertices());

        if(graph.isEmpty()){
            logger.debug("No graph " + properties.getGraphName() + ". Trying to create it.");
            db.graph(properties.getGraphName()).create(List.of(edgeDefinition),
                    new GraphCreateOptions().replicationFactor(replication));
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
