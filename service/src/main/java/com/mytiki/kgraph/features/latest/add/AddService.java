/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.add;


import com.mytiki.common.exception.ApiExceptionFactory;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;

public class AddService {

    private final GraphService graphService;

    public AddService(GraphService graphService) {
        this.graphService = graphService;
    }

    public AddAO execute(AddAO body){
        try {
            graphService.upsert(
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
