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
            "FOR i IN #{#vertexList} " +
            "INSERT { _id: i.id, _key: i.key, _class: i.class, created: now, modified: now } " +
            "INTO #collection OPTIONS #{#options} " +
            "RETURN NEW")
    List<T> _insertAll(@SpelParam("vertexList") String vertexList, @SpelParam("options") String options);

    default List<T> insertAll(List<T> vertices){
        StringBuilder vertexListBuilder = new StringBuilder("[");
        for(VertexDO vertex : vertices) {
            vertexListBuilder.append("{")
                    .append("id: \"")
                    .append(vertex.getDbId())
                    .append("\", key: \"")
                    .append(vertex.getId())
                    .append("\", class: \"")
                    .append(vertex.getClass().getName())
                    .append("\"},");
        }
        vertexListBuilder.deleteCharAt(vertexListBuilder.lastIndexOf(","));
        vertexListBuilder.append("]");
        return this._insertAll(vertexListBuilder.toString(), "{ overwriteMode: \"ignore\" }" );
    }
}
