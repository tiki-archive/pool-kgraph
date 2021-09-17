/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.search;

import com.mytiki.common.ApiConstants;
import com.mytiki.common.reply.ApiReplyAO;
import com.mytiki.common.reply.ApiReplyAOFactory;
import com.mytiki.kgraph.features.latest.edge.EdgeAO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = SearchController.PATH_CONTROLLER)
public class SearchController {
    public static final String PATH_CONTROLLER = ApiConstants.API_LATEST_ROUTE + "search";

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiReplyAO<EdgeAO> post() {
        searchService.search();
        return ApiReplyAOFactory.ok();
    }
}