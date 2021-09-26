/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.sync;

import com.arangodb.velocypack.VPackBuilder;
import com.arangodb.velocypack.VPackSlice;
import org.springframework.core.convert.converter.Converter;

public class SyncEnumConverterToVPackSlice implements Converter<SyncEnum, VPackSlice> {
    @Override
    public VPackSlice convert(SyncEnum source) {
        VPackBuilder builder = new VPackBuilder();
        builder.add(source.getName());
        return builder.slice();
    }
}
