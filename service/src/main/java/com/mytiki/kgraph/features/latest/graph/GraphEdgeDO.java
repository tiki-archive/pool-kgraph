/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

@Edge(GraphEdgeDO.COLLECTION_NAME)
public class GraphEdgeDO {
    public static final String COLLECTION_NAME = "edge";

    @Id
    private String id;

    @From
    private GraphVertexDO from;

    @To
    private GraphVertexDO to;

    public GraphEdgeDO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GraphVertexDO getFrom() {
        return from;
    }

    public void setFrom(GraphVertexDO from) {
        this.from = from;
    }

    public GraphVertexDO getTo() {
        return to;
    }

    public void setTo(GraphVertexDO to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", from='" + from.toString() + '\'' +
                ", to='" + to.toString() + '\'' +
                "}";
    }
}
