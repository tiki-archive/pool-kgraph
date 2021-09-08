/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.fingerprint;

import java.util.Set;

public class FingerprintAORsp {
    private Set<String> fingerprints;
    private Set<FingerprintAORspEdge> edges;

    public Set<String> getFingerprints() {
        return fingerprints;
    }

    public void setFingerprints(Set<String> fingerprints) {
        this.fingerprints = fingerprints;
    }

    public Set<FingerprintAORspEdge> getEdges() {
        return edges;
    }

    public void setEdges(Set<FingerprintAORspEdge> edges) {
        this.edges = edges;
    }
}
