/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import com.mytiki.kgraph.features.latest.vertex.VertexDO;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@Edge(EdgeDO.COLLECTION_NAME)
public class EdgeDO<F extends VertexDO, T extends VertexDO> implements Serializable {
    public static final String COLLECTION_NAME = "edge";

    @Id
    private String id;

    @From
    private F from;

    @To
    private T to;

    private ZonedDateTime created;

    private ZonedDateTime modified;

    private Set<String> fingerprints;

    private Double weight;

    public EdgeDO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public F getFrom() {
        return from;
    }

    public void setFrom(F from) {
        this.from = from;
    }

    public T getTo() {
        return to;
    }

    public void setTo(T to) {
        this.to = to;
    }

    public Set<String> getFingerprints() {
        return fingerprints;
    }

    public void setFingerprints(Set<String> fingerprints) {
        this.fingerprints = fingerprints;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public void setModified(ZonedDateTime modified) {
        this.modified = modified;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", collection='" + COLLECTION_NAME + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", weight='" + weight + '\'' +
                ", fingerprints='" + fingerprints + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified+ '\'' +
                "}";
    }
}
