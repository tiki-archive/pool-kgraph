/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.sync;

import com.arangodb.springframework.repository.ArangoRepository;

import java.util.Optional;

public interface SyncRepository extends ArangoRepository<SyncDO<?>, String> {
    <T> Optional<SyncDO<T>> findByName(SyncEnum name);
}
