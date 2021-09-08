/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.fingerprint;

import java.util.Map;
import java.util.Set;

public class FingerprintAORspEdge {
    private Set<String> fingerprints;
    private Map<String, Object> from;
    private Map<String, Object> to;

    public Set<String> getFingerprints() {
        return fingerprints;
    }

    public void setFingerprints(Set<String> fingerprints) {
        this.fingerprints = fingerprints;
    }

    public Map<String, Object> getFrom() {
        return from;
    }

    public void setFrom(Map<String, Object> from) {
        this.from = from;
    }

    public Map<String, Object> getTo() {
        return to;
    }

    public void setTo(Map<String, Object> to) {
        this.to = to;
    }
}
