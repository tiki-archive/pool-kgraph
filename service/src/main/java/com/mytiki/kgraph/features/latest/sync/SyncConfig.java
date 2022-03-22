/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.sync;

import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.mytiki.kgraph.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@EnableArangoRepositories(SyncConfig.PACKAGE_PATH)
public class SyncConfig {
    public static final String PACKAGE_PATH = Constants.PACKAGE_FEATURES_LATEST_DOT_PATH + ".sync";

    @Bean
    public SyncService syncService(@Autowired SyncRepository syncRepository){
        return new SyncService(syncRepository);
    }
}
