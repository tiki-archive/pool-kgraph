/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchAOVertex {
    private String type;
    private String value;

    public SearchAOVertex() {}

    @JsonCreator
    public SearchAOVertex(
            @JsonProperty(required = true) String type,
            @JsonProperty(required = true) String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}