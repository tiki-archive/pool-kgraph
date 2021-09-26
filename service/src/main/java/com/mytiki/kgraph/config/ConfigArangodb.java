/*
 * Copyright (c) My Tiki, Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.config;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.config.ArangoConfiguration;
import com.mytiki.kgraph.features.latest.sync.SyncEnumConverterToSyncEnum;
import com.mytiki.kgraph.features.latest.sync.SyncEnumConverterToVPackSlice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;

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

    @Override
    public Collection<Converter<?, ?>> customConverters() {
        Collection<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new SyncEnumConverterToSyncEnum());
        converters.add(new SyncEnumConverterToVPackSlice());
        return converters;
    }
}
