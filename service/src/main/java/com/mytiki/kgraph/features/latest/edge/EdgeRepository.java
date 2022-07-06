/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.edge;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.annotation.SpelParam;
import com.arangodb.springframework.repository.ArangoRepository;
import com.mytiki.kgraph.features.latest.vertex.VertexDO;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EdgeRepository extends
        ArangoRepository<EdgeDO<? extends VertexDO, ? extends VertexDO>, String> {

    @Query("FOR e IN #collection FILTER e._from == @from AND e._to == @to RETURN e")
    <F extends VertexDO, T extends VertexDO> Optional<EdgeDO<F,T>> findByVertices(
            @Param("from") String from, @Param("to") String to);

    @Query("FOR v,e,p IN 1..@depth ANY @start GRAPH kgraph RETURN e")
    List<EdgeDO<? extends VertexDO, ? extends VertexDO>> traverse(String start, int depth);

    @Query("FOR v,e,p IN ANY SHORTEST_PATH @start TO @end GRAPH kgraph OPTIONS {weightAttribute: 'weight'} FILTER e != null RETURN e")
    List<EdgeDO<? extends VertexDO, ? extends VertexDO>> shortestPath(String start, String end);


    //TODO generify search
    @Query("FOR v, e, p IN 2..2 ANY @company GRAPH kgraph " +
            "FILTER (FOR v2, e2 IN 1..1 ANY p.vertices[1] GRAPH kgraph FILTER v2._id == 'action/email_received' RETURN 1) == [1] " +
            "RETURN (FOR v2, e2 IN 1..1 ANY p.vertices[1] GRAPH kgraph FILTER IS_SAME_COLLECTION('subject', v2) RETURN {'subject': v2.text, 'occurrences': 1/(e2.weight)})")
    List<EdgeDO<? extends VertexDO, ? extends VertexDO>> searchSubject(String company);

    @Query("LET now = DATE_ISO8601(DATE_NOW()) " +
            "UPSERT { _from: @from, _to: @to } " +
            "INSERT { " +
                "_from: @from, " +
                "_to: @to, " +
                "created: now, " +
                "modified: now, " +
                "fingerprints: @fingerprints, " +
                "weight: 1 } " +
            "UPDATE { " +
                "fingerprints: APPEND(OLD.fingerprints, @fingerprints, true), " +
                "weight: 1.0/LENGTH(APPEND(OLD.fingerprints, @fingerprints, true)), " +
                "modified: now } " +
            "IN #collection " +
            "RETURN NEW")
    <F extends VertexDO, T extends VertexDO> EdgeDO<F,T> upsert(
            @Param("from") String from, @Param("to") String to, @Param("fingerprints") Set<String> fingerprints);

    @Query("LET now = DATE_ISO8601(DATE_NOW()) " +
            "FOR i IN #{#edgeList}" +
            "UPSERT { _from: i.from, _to: i.to } " +
            "INSERT { _from: i.from, _to: i.to, created: now, modified: now, fingerprints: i.fingerprints, weight: i.weight } " +
            "UPDATE { fingerprints: APPEND(OLD.fingerprints, i.fingerprints, true), weight: 1.0/LENGTH(APPEND(OLD.fingerprints, i.fingerprints, true)), modified: now } " +
            "IN #collection " +
            "RETURN NEW")
    List<EdgeDO<? extends VertexDO, ? extends VertexDO>> _upsertAll(@SpelParam("edgeList") String edgeList);

    default List<EdgeDO<? extends VertexDO, ? extends VertexDO>> upsertAll(
            List<EdgeDO<? extends VertexDO, ? extends VertexDO>> edges){
        StringBuilder edgeListBuilder = new StringBuilder("[");
        for(EdgeDO<? extends VertexDO, ? extends VertexDO> edge : edges) {
            StringBuilder fpListBuilder = new StringBuilder("[");
            for(String fingerprint : edge.getFingerprints())
                fpListBuilder.append("\"").append(fingerprint).append("\"").append(",");
            fpListBuilder.deleteCharAt(fpListBuilder.lastIndexOf(","));
            fpListBuilder.append("]");
            edgeListBuilder.append("{")
                    .append("from: \"")
                    .append(edge.getFrom().getDbId())
                    .append("\", to: \"")
                    .append(edge.getTo().getDbId())
                    .append("\", fingerprints: ")
                    .append(fpListBuilder)
                    .append(",weight: ")
                    .append(1.0/edge.getFingerprints().size())
                    .append("},");
        }
        edgeListBuilder.deleteCharAt(edgeListBuilder.lastIndexOf(","));
        edgeListBuilder.append("]");
        return this._upsertAll(edgeListBuilder.toString());
    }
}
