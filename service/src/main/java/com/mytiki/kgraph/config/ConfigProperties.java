/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.config;

import org.springframework.beans.factory.annotation.Value;

public class ConfigProperties {

    @Value("${spring.profiles.active:}")
    private String springProfilesActive;

    @Value("${com.mytiki.kgraph.jwt.public_key}")
    private String jwtPublicKey;

    @Value("${com.mytiki.kgraph.db.name}")
    private String dbName;

    @Value("${com.mytiki.kgraph.graph.name}")
    private String graphName;

    @Value("${com.mytiki.kgraph.epsilon}")
    private Integer epsilon;

    public String getSpringProfilesActive() {
        return springProfilesActive;
    }

    public void setSpringProfilesActive(String springProfilesActive) {
        this.springProfilesActive = springProfilesActive;
    }

    public String getJwtPublicKey() {
        return jwtPublicKey;
    }

    public void setJwtPublicKey(String jwtPublicKey) {
        this.jwtPublicKey = jwtPublicKey;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public Integer getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(Integer epsilon) {
        this.epsilon = epsilon;
    }
}
