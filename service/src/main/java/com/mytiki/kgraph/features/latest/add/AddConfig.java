/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.add;

import com.mytiki.kgraph.features.latest.graph.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class AddConfig {
    @Bean
    public AddService addService(@Autowired GraphService graphService){
        return new AddService(graphService);
    }

    @Bean
    public AddController addController(@Autowired AddService addService){
        return new AddController(addService);
    }
}
