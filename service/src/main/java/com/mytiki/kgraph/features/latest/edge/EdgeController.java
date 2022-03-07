/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;

import com.mytiki.common.ApiConstants;
import com.mytiki.common.reply.ApiReplyAO;
import com.mytiki.common.reply.ApiReplyAOBuilder;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(value = EdgeController.PATH_CONTROLLER)
public class EdgeController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "edge";

    private final EdgeService edgeService;

    public EdgeController(EdgeService edgeService) {
        this.edgeService = edgeService;
    }

    @RolesAllowed(Constants.ROLE_USER)
    @RequestMapping(method = RequestMethod.POST)
    public ApiReplyAO<?> post(@RequestBody List<EdgeAO> body) {
        //edgeService.add(body);
        return new ApiReplyAOBuilder<>()
                .httpStatus(HttpStatus.ACCEPTED)
                .build();
    }
}