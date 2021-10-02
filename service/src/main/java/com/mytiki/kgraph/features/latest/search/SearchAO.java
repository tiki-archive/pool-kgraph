/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.search;

public class SearchAO {
    private SearchAOVertex from;
    private SearchAOVertex to;

    public SearchAO() {}

    public SearchAOVertex getFrom() {
        return from;
    }

    public void setFrom(SearchAOVertex from) {
        this.from = from;
    }

    public SearchAOVertex getTo() {
        return to;
    }

    public void setTo(SearchAOVertex to) {
        this.to = to;
    }
}
