/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.sync;

import java.util.EnumSet;

public enum SyncEnum {
    HIBP_CACHED("hibpCached");

    private static final EnumSet<SyncEnum> allOf = EnumSet.allOf(SyncEnum.class);

    private final String name;

    SyncEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SyncEnum forName(String name) {
        for (SyncEnum e : allOf) {
            if (e.getName().equals(name))
                return e;
        }
        return null;
    }
}
