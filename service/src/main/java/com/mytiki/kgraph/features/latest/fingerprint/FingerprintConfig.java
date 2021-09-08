/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.fingerprint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class FingerprintConfig {
    @Bean
    public FingerprintService fingerprintService(
            @Autowired GraphService graphService,
            @Autowired ObjectMapper objectMapper){
        return new FingerprintService(graphService, objectMapper);
    }

    @Bean
    public FingerprintController fingerprintController(@Autowired FingerprintService fingerprintService){
        return new FingerprintController(fingerprintService);
    }
}
