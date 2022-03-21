/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.vertex;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.annotation.SpelParam;
import com.arangodb.springframework.repository.ArangoRepository;
import com.mytiki.kgraph.features.latest.edge.EdgeDO;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public interface VertexRepository<T extends VertexDO> extends ArangoRepository<T, String> {
    @Query("LET now = DATE_ISO8601(DATE_NOW()) " +
            "LET vertex = @vertex " +
            "UPSERT { value: vertex.value } " +
            "INSERT MERGE(vertex, { created: now, modified: now }) " +
            "UPDATE MERGE(vertex, { modified: now }) " +
            "IN #collection " +
            "RETURN NEW")
    T upsert(@Param("vertex") T vertex);

    @Query("LET now = DATE_ISO8601(DATE_NOW()) " +
            "FOR i IN #{#idArray} " +
            "INSERT { _key: i, _class: \"#{#clazz}\", created: now, modified: now } " +
            "INTO #collection OPTIONS #{#options} " +
            "RETURN NEW")
    List<T> _insertAll(
            @SpelParam("idArray") String idArray,
            @SpelParam("clazz") String clazz,
            @SpelParam("options") String options);

    default List<T> insertAll(List<T> vertices){
        return this._insertAll(
                "[" + vertices.stream().map(v -> "\"" + v.getId() + "\"").collect(Collectors.joining(",")) + "]",
                vertices.get(0).getClass().getName(),
                "{ overwriteMode: \"ignore\" }" );
    }
}
