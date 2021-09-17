/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;

import com.mytiki.common.ApiConstants;
import com.mytiki.common.reply.ApiReplyAO;
import com.mytiki.common.reply.ApiReplyAOFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = EdgeController.PATH_CONTROLLER)
public class EdgeController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "edge";

    private final EdgeService edgeService;

    public EdgeController(EdgeService edgeService) {
        this.edgeService = edgeService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiReplyAO<EdgeAO> post(@RequestBody EdgeAO body) {
        return ApiReplyAOFactory.ok(edgeService.add(body));
    }
}