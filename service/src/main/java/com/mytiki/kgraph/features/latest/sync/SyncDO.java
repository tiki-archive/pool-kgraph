/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.sync;

import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

@Document("kgraph_sync")
public class SyncDO<T> {
    @Id
    private String id;
    private SyncEnum name;
    private T value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SyncEnum getName() {
        return name;
    }

    public void setName(SyncEnum name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
