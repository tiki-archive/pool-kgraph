/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.sync;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SyncRepository extends ArangoRepository<SyncDO<?>, String> {
    @Query("LET now = DATE_ISO8601(DATE_NOW()) " +
            "LET sync = @sync" +
            "UPSERT { _key:  sync.id } " +
            "INSERT { _key: sync.id, value: sync.value, created: now, modified: now } " +
            "UPDATE { value: sync.value, modified: now } " +
            "IN #collection " +
            "RETURN NEW")
    <T> SyncDO<T> upsert(@Param("sync") SyncDO<T> syncDO);
}
