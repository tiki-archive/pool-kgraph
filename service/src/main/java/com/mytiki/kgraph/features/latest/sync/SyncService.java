/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.sync;

import com.arangodb.ArangoDBException;

import java.util.Optional;

public class SyncService {

    private final SyncRepository repository;

    public SyncService(SyncRepository repository) {
        this.repository = repository;
    }

    public <T> SyncDO<T> upsert(SyncEnum sync, T value){
        return repository.upsert(sync.toString(), value);
    }

    public <T> Optional<SyncDO<T>> get(SyncEnum sync){
        try {
            Optional<SyncDO<?>> optional = repository.findById(sync.toString());
            return optional.map(syncDO -> (SyncDO<T>) syncDO);
        }catch (ArangoDBException ex){
            if(ex.getErrorNum() == 1203) return Optional.empty();
            else throw ex;
        }
    }
}
