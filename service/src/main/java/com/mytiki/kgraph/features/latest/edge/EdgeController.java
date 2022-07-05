/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;

import com.mytiki.common.ApiConstants;
import com.mytiki.common.reply.ApiReplyAO;
import com.mytiki.common.reply.ApiReplyAOFactory;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping(value = EdgeController.PATH_CONTROLLER)
public class EdgeController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "edge";

    private final EdgeService edgeService;

    public EdgeController(EdgeService edgeService) {
        this.edgeService = edgeService;
    }

    @RolesAllowed(Constants.ROLE_INGEST)
    @RequestMapping(method = RequestMethod.POST)
    public ApiReplyAO<List<EdgeAO>> post(@RequestBody List<EdgeAO> body) {
        return ApiReplyAOFactory.ok(edgeService.add(body));
    }


    @RolesAllowed(Constants.ROLE_ETL)
    @RequestMapping(method = RequestMethod.GET, path = "/search/subject")
    public ApiReplyAO<List<EdgeAO>> get(@RequestParam String company,
                                        @RequestParam ZonedDateTime start,
                                        @RequestParam ZonedDateTime end) {
        return ApiReplyAOFactory.ok(edgeService.searchSubject(company, start, end));
    }
}