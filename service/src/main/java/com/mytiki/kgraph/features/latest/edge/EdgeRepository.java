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

public interface EdgeRepository extends
        ArangoRepository<EdgeDO<? extends VertexDO, ? extends VertexDO>, String> {
    @Query("FOR e IN #collection FILTER e._from == @from AND e._to == @to RETURN e")
    <F extends VertexDO, T extends VertexDO> Optional<EdgeDO<F,T>>
    findByVertices(@Param("from") String from, @Param("to") String to);

    List<EdgeDO<? extends VertexDO, ? extends VertexDO>> findByFingerprintsContains(String fingerprint);

    @Query("FOR v,e,p IN 1..@depth INBOUND @start GRAPH kgraph FILTER p.edges[*].weight ALL <= @epsilon RETURN e")
    List<EdgeDO<? extends VertexDO, ? extends VertexDO>> traverse(String start, int depth, Integer epsilon);

    @Query("FOR v,e,p IN ANY SHORTEST_PATH @start TO @end GRAPH kgraph OPTIONS {weightAttribute: 'weight'} FILTER e != null AND e.weight <= @epsilon RETURN e")
    List<EdgeDO<? extends VertexDO, ? extends VertexDO>> shortestPath(String start, String end, Integer epsilon);

    @Query("LET now = DATE_ISO8601(DATE_NOW()) " +
            "UPSERT { _from: \"#{#edge.from.id}\", _to: \"#{#edge.to.id}\" } " +
            "INSERT { " +
                "_from: \"#{#edge.from.id}\", " +
                "_to: \"#{#edge.to.id}\", " +
                "created: now, " +
                "modified: now, " +
                "fingerprints: [ \"#{#edge.fingerprints}\" ], " +
                "weight: 1 } " +
            "UPDATE { " +
                "fingerprints: APPEND(OLD.fingerprints, [ \"#{#edge.fingerprints}\" ], true), " +
                "weight: 1.0/LENGTH(APPEND(OLD.fingerprints, [ \"#{#edge.fingerprints}\" ], true)), " +
                "modified: now } " +
            "IN #collection " +
            "RETURN NEW")
    EdgeDO<? extends VertexDO, ? extends VertexDO> upsert(
            @SpelParam("edge") EdgeDO<? extends VertexDO, ? extends VertexDO> edge);

    @Query("LET now = DATE_ISO8601(DATE_NOW()) #{#upsertAQL} IN #collection RETURN NEW")
    List<EdgeDO<? extends VertexDO, ? extends VertexDO>> _rawUpsert(@SpelParam("upsertAQL") String upsertAQL);


    default List<EdgeDO<? extends VertexDO, ? extends VertexDO>> upsertAll(
            List<EdgeDO<? extends VertexDO, ? extends VertexDO>> edges){

        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder edgeArrayBuilder = new StringBuilder("[");

        for(EdgeDO<? extends VertexDO, ? extends VertexDO> edge : edges) {
            StringBuilder fpListBuilder = new StringBuilder("[");
            for(String fingerprint : edge.getFingerprints()){
                fpListBuilder.append("\"").append(fingerprint).append("\"").append(",");
            }
            fpListBuilder.deleteCharAt(fpListBuilder.lastIndexOf(","));
            fpListBuilder.append("]");

            edgeArrayBuilder.append("{")
                    .append("from: \"")
                    .append(edge.getFrom().getId())
                    .append("\", to: \"")
                    .append(edge.getTo().getId())
                    .append("\", fingerprints: ")
                    .append(fpListBuilder)
                    .append(",weight: ")
                    .append(1.0/edge.getFingerprints().size())
                    .append("},");
        }
        edgeArrayBuilder.deleteCharAt(edgeArrayBuilder.lastIndexOf(","));
        edgeArrayBuilder.append("]");

        queryBuilder
                .append("FOR i IN ")
                .append(edgeArrayBuilder)
                .append("\nUPSERT { _from: i.from, _to: i.to } ")
                .append("INSERT { _from: i.from, _to: i.to, created: now, modified: now, fingerprints: i.fingerprints, weight: i.weight } ")
                .append("UPDATE { fingerprints: APPEND(OLD.fingerprints, i.fingerprints, true), weight: 1.0/LENGTH(APPEND(OLD.fingerprints, i.fingerprints, true)), modified: now } ");

        return this._rawUpsert(queryBuilder.toString());
    }
}
