/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph_generate;

import java.util.List;

public class KGraphDefs {
    private List<KGraphDefsVertex> vertices;

    public List<KGraphDefsVertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<KGraphDefsVertex> vertices) {
        this.vertices = vertices;
    }
}
