/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.sync;

import com.arangodb.velocypack.VPackSlice;
import org.springframework.core.convert.converter.Converter;

public class SyncEnumConverterToSyncEnum implements Converter<VPackSlice, SyncEnum> {
    @Override
    public SyncEnum convert(VPackSlice source) {
        return source.getAsString() != null ?
                SyncEnum.forName(source.getAsString()) :
                null;
    }
}
