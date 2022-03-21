/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.sync;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface SyncRepository extends ArangoRepository<SyncDO<?>, String> {
    @Query("LET now = DATE_ISO8601(DATE_NOW()) " +
            "UPSERT { _key: @id } " +
            "INSERT { _key: @id, value: @value, created: now, modified: now } " +
            "UPDATE { value: @value, modified: now } " +
            "IN #collection " +
            "RETURN NEW")
    <T> SyncDO<T> upsert(@Param("id") String id, @Param("value") T value);
}
