package com.mytiki.kgraph.features.latest.company;

import com.mytiki.kgraph.features.latest.big_picture.BigPictureService;
import com.mytiki.kgraph.features.latest.graph.GraphService;
import com.mytiki.kgraph.features.latest.hibp.HibpService;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class CompanyConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".company";

    @Bean
    public CompanyController companyController(@Autowired CompanyService companyService){
        return new CompanyController(companyService);
    }

    @Bean
    public CompanyService companyService(
            @Autowired BigPictureService bigPictureService,
            @Autowired HibpService hibpService,
            @Autowired GraphService graphService){
        return new CompanyService(bigPictureService, hibpService, graphService);
    }
}
