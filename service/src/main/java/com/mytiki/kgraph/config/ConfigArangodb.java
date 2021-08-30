/*
 * Copyright (c) My Tiki, Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.config;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigArangodb implements ArangoConfiguration {
    @Autowired
    private ConfigProperties properties;

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder();
    }

    @Override
    public String database() {
        return properties.getDbName();
    }
}
