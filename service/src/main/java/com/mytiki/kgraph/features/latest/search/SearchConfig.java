/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.search;

import com.mytiki.kgraph.features.latest.graph.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class SearchConfig {

    @Bean
    public SearchController searchController(@Autowired SearchService searchService){
        return new SearchController(searchService);
    }

    @Bean
    public SearchService searchService(@Autowired GraphService graphService){
        return new SearchService(graphService);
    }
}
