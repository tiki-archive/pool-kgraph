/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;


import com.mytiki.common.exception.ApiExceptionFactory;
import com.mytiki.kgraph.features.latest.graph.GraphEdgeDO;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import com.mytiki.kgraph.features.latest.graph.GraphVertexDO;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;

public class EdgeService {

    private final GraphService graphService;

    public EdgeService(GraphService graphService) {
        this.graphService = graphService;
    }

    public EdgeAO add(EdgeAO body){
        try {
            GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO> edge = graphService.upsertEdge(
                    body.getFrom().getType(),
                    body.getFrom().getValue(),
                    body.getTo().getType(),
                    body.getTo().getValue(),
                    body.getFingerprint());
            return body;
        }catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw ApiExceptionFactory.exception(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
