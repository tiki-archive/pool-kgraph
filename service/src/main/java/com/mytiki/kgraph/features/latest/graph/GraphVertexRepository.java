/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;

import com.arangodb.springframework.repository.ArangoRepository;

import java.util.Optional;

public interface GraphVertexRepository<T extends GraphVertexDO> extends ArangoRepository<T, String> {
    Optional<T> findByValue(String value);
}
