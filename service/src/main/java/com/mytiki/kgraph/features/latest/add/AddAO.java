/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddAO {
    private AddAOVertex from;
    private AddAOVertex to;
    private String fingerprint;

    public AddAO() {
    }

    @JsonCreator
    public AddAO(
            @JsonProperty(required = true) AddAOVertex from,
            @JsonProperty(required = true) AddAOVertex to,
            @JsonProperty(required = true) String fingerprint) {
        this.from = from;
        this.to = to;
        this.fingerprint = fingerprint;
    }

    public AddAOVertex getFrom() {
        return from;
    }

    public void setFrom(AddAOVertex from) {
        this.from = from;
    }

    public AddAOVertex getTo() {
        return to;
    }

    public void setTo(AddAOVertex to) {
        this.to = to;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
