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
        SyncDO<T> save = new SyncDO<T>();
        save.setName(sync);
        save.setValue(value);
        Optional<SyncDO<T>> existing = get(sync);
        existing.ifPresent(syncDO -> save.setId(syncDO.getId()));
        return repository.save(save);
    }

    public <T> Optional<SyncDO<T>> get(SyncEnum sync){
        try {
            return repository.findByName(sync);
        }catch (ArangoDBException ex){
            if(ex.getErrorNum() == 1203) return Optional.empty();
            else throw ex;
        }

    }
}
