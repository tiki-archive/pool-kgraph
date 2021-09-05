/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GraphEdgeRepository extends
        ArangoRepository<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>, String> {
    @Query("FOR e IN #collection FILTER e._from == @from AND e._to == @to RETURN e")
    <F extends GraphVertexDO, T extends GraphVertexDO> Optional<GraphEdgeDO<F,T>>
    findByVertices(@Param("from") String from, @Param("to") String to);
}
