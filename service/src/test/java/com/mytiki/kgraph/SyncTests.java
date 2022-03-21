/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph;

import com.mytiki.kgraph.features.latest.sync.SyncDO;
import com.mytiki.kgraph.features.latest.sync.SyncEnum;
import com.mytiki.kgraph.features.latest.sync.SyncRepository;
import com.mytiki.kgraph.features.latest.sync.SyncService;
import com.mytiki.kgraph.main.KGraphApp;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {KGraphApp.class}
)
@ActiveProfiles(profiles = {"local", "test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SyncTests {

    @Autowired
    private SyncService syncService;

    @Autowired
    private SyncRepository syncRepository;

    @Test
    public void Test_RepositoryUpsert_Success() {
        String testId = UUID.randomUUID().toString();
        Long testVal = 1L;

        SyncDO<Long> inserted = syncRepository.upsert(testId, testVal);

        assertEquals(testId, inserted.getId());
        assertEquals(testVal, inserted.getValue());

        testVal = 2L;
        SyncDO<Long> updated = syncRepository.upsert(testId, testVal);

        assertEquals(inserted.getId(), updated.getId());
        assertEquals(testVal, updated.getValue());
    }


    @Test
    public void Test_ServiceUpsert_Success() {
        Long testVal = 1L;
        SyncDO<Long> saved = syncService.upsert(SyncEnum.HIBP_CACHED, testVal);
        assertNotNull(saved.getId());
        assertEquals(testVal, saved.getValue());
    }

    @Test
    public void Test_Get_Success() {
        Long testVal = 1L;
        syncService.upsert(SyncEnum.HIBP_CACHED, testVal);
        Optional<SyncDO<Long>> sync = syncService.get(SyncEnum.HIBP_CACHED);
        assertTrue(sync.isPresent());
        assertNotNull(sync.get().getId());
        assertEquals(testVal, sync.get().getValue());
    }
}
