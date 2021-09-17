/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.graph;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GraphEdgeRepository extends
        ArangoRepository<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>, String> {
    @Query("FOR e IN #collection FILTER e._from == @from AND e._to == @to RETURN e")
    <F extends GraphVertexDO, T extends GraphVertexDO> Optional<GraphEdgeDO<F,T>>
    findByVertices(@Param("from") String from, @Param("to") String to);

    List<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>> findByFingerprintsContains(String fingerprint);

    @Query("FOR v,e IN 1..@depth ANY @start GRAPH kgraph RETURN e")
    List<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>> traverse(String start, int depth);

    @Query("FOR v,e IN ANY SHORTEST_PATH @start TO @end GRAPH kgraph OPTIONS {weightAttribute: 'weight'} FILTER e != null RETURN e")
    List<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>> shortestPath(String start, String end);
}
