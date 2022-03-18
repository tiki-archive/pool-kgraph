/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.main;

import com.mytiki.common.reply.ApiReplyHandlerDefault;
import com.mytiki.kgraph.config.*;
import com.mytiki.kgraph.utilities.ApiExceptionHandler;
import com.mytiki.kgraph.utilities.UtilitiesConfig;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Import({
        ConfigProperties.class,
        ApiExceptionHandler.class,
        ApiReplyHandlerDefault.class,
        UtilitiesConfig.class,
        ConfigArangodb.class,
        ConfigFeatures.class,
        ConfigSecurity.class,
        ConfigGraph.class,
})
@EnableScheduling
@EnableAsync
public class KGraphConfig {

    @PostConstruct
    void starter(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
