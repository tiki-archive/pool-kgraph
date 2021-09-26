package com.mytiki.kgraph.features.latest.hibp;

import com.mytiki.kgraph.config.ConfigProperties;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import com.mytiki.kgraph.features.latest.sync.SyncService;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

public class HibpConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".hibp";
    public static final String ROOT_URI = "https://haveibeenpwned.com/api/v3/";

    @Bean
    public HibpService hibpService(
            @Autowired ConfigProperties configProperties,
            @Autowired RestTemplateBuilder restTemplateBuilder,
            @Autowired GraphService graphService,
            @Autowired SyncService syncService){
        return new HibpService(
                restTemplateBuilder
                        .rootUri(ROOT_URI)
                        .defaultHeader("hibp-api-key", configProperties.getApiKeyHibp())
                        .defaultHeader("user-agent", "TIKI")
                        .build(),
                graphService, syncService);
    }
}
