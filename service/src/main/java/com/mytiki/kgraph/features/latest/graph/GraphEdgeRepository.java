/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;

import com.arangodb.springframework.repository.ArangoRepository;

public interface GraphEdgeRepository extends ArangoRepository<GraphEdgeDO, String> {
}
