/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;


import com.mytiki.common.exception.ApiExceptionFactory;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.Future;

public class EdgeService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final GraphService graphService;

    public EdgeService(GraphService graphService) {
        this.graphService = graphService;
    }

    @Async
    public Future<List<EdgeAO>> add(List<EdgeAO> body){
       try {
           logger.info("Edges Added Request: " + body.size());
           for(EdgeAO edge : body) {
               graphService.upsertEdge(
                       edge.getFrom().getType(),
                       edge.getFrom().getValue(),
                       edge.getTo().getType(),
                       edge.getTo().getValue(),
                       edge.getFingerprint());
           }
           logger.info("Edges Added: " + body.size());
           return new AsyncResult<>(body);
       }catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
           throw ApiExceptionFactory.exception(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
       }
    }
}
