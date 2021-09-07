/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddAOVertex {
    private String type;
    private String value;

    public AddAOVertex() {}

    @JsonCreator
    public AddAOVertex(
            @JsonProperty(required = true) String type,
            @JsonProperty(required = true) String name) {
        this.type = type;
        this.value = name;
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