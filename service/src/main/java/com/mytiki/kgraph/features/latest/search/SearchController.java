/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.search;

import com.mytiki.common.ApiConstants;
import com.mytiki.common.reply.ApiReplyAO;
import com.mytiki.common.reply.ApiReplyAOFactory;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(value = SearchController.PATH_CONTROLLER)
public class SearchController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "search";

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RolesAllowed(Constants.ROLE_CUSTOMER)
    @RequestMapping(method = RequestMethod.GET)
    public ApiReplyAO<List<SearchAO>> get(
            @RequestParam SearchAOVertex start,
            @RequestParam(required = false) SearchAOVertex end,
            @RequestParam(required = false) Integer depth) {
        if(end == null && depth == null) depth = 1;
        return ApiReplyAOFactory.ok(searchService.search(start, end, depth));
    }
}