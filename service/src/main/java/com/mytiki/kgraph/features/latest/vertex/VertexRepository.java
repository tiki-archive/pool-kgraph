/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.vertex;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VertexRepository<T extends VertexDO> extends ArangoRepository<T, String> {
    Optional<T> findByValue(String value);

    @Query("LET now = DATE_ISO8601(DATE_NOW()) " +
            "LET vertex = @vertex " +
            "UPSERT { value: vertex.value } " +
            "INSERT MERGE(vertex, { created: now, modified: now }) " +
            "UPDATE MERGE(MERGE(OLD, vertex), { modified: now }) " +
            "IN #collection " +
            "RETURN NEW")
    T upsert(@Param("vertex") T vertex);
}
