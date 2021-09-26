package com.mytiki.kgraph.features.latest.big_picture;

import com.mytiki.kgraph.config.ConfigProperties;
import com.mytiki.kgraph.features.latest.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

public class BigPictureConfig {
    public static final String ROOT_URI = "https://company.bigpicture.io/v1/companies/";

    @Bean
    public BigPictureService bigPictureService(
            @Autowired ConfigProperties configProperties,
            @Autowired RestTemplateBuilder restTemplateBuilder,
            @Autowired @Lazy CompanyService companyService){
        return new BigPictureService(
                restTemplateBuilder
                        .rootUri(ROOT_URI)
                        .defaultHeader("Authorization", configProperties.getApiKeyBigPicture())
                        .build(),
                companyService);
    }

    @Bean
    public BigPictureController bigPictureController(@Autowired BigPictureService bigPictureService){
        return new BigPictureController(bigPictureService);
    }
}
