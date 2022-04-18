/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph;

import com.mytiki.kgraph.features.latest.edge.*;
import com.mytiki.kgraph.features.latest.vertex.VertexCompanyDO;
import com.mytiki.kgraph.features.latest.vertex.VertexDO;
import com.mytiki.kgraph.features.latest.vertex.VertexService;
import com.mytiki.kgraph.main.KGraphApp;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {KGraphApp.class}
)
@ActiveProfiles(profiles = {"test", "local"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EdgeTests {

    @Autowired
    private EdgeService edgeService;

    @Autowired
    private VertexService vertexService;

    @Autowired
    private EdgeRepository edgeRepository;

    @Test
    public void Test_Add_NewVertices_Success() {
        EdgeAO edge1 = new EdgeAO(
                new EdgeAOVertex("company", UUID.randomUUID().toString()),
                new EdgeAOVertex("company", UUID.randomUUID().toString()),
                UUID.randomUUID().toString()
        );

        EdgeAO edge2 = new EdgeAO(
                new EdgeAOVertex("company", UUID.randomUUID().toString()),
                new EdgeAOVertex("company", UUID.randomUUID().toString()),
                UUID.randomUUID().toString()
        );

        List<EdgeAO> added = edgeService.add(List.of(edge1,edge2));
        assertEquals(2, added.size());
        assertTrue(added.stream().anyMatch(e ->
                e.getTo().getId().equals(edge1.getTo().getId()) &&
                e.getFrom().getId().equals(edge1.getFrom().getId())));
        assertTrue(added.stream().anyMatch(e ->
                e.getTo().getId().equals(edge2.getTo().getId()) &&
                e.getFrom().getId().equals(edge2.getFrom().getId())));
    }

    @Test
    public void Test_Add_ExistingVertices_Success() {
        VertexCompanyDO company1 = new VertexCompanyDO();
        company1.setId(UUID.randomUUID().toString());

        VertexCompanyDO company2 = new VertexCompanyDO();
        company2.setId(UUID.randomUUID().toString());

        vertexService.insert(List.of(company1, company2));

        EdgeAO edge1 = new EdgeAO(
                new EdgeAOVertex("company", company1.getId()),
                new EdgeAOVertex("company", company2.getId()),
                UUID.randomUUID().toString()
        );

        EdgeAO edge2 = new EdgeAO(
                new EdgeAOVertex("company", company1.getId()),
                new EdgeAOVertex("company", company2.getId()),
                UUID.randomUUID().toString()
        );

        List<EdgeAO> added = edgeService.add(List.of(edge1,edge2));

        assertEquals(1, added.size());
        assertTrue(added.stream().anyMatch(e ->
                e.getTo().getId().equals(edge1.getTo().getId()) &&
                e.getFrom().getId().equals(edge1.getFrom().getId())));

        Optional<EdgeDO<VertexCompanyDO, VertexCompanyDO>> optional =
                edgeRepository.findByVertices(company1.getDbId(), company2.getDbId());

        assertTrue(optional.isPresent());
        assertEquals(2, optional.get().getFingerprints().size());
    }

    @Test
    public void Test_Upsert_Success() {
        VertexCompanyDO company1 = new VertexCompanyDO();
        company1.setId(UUID.randomUUID().toString());

        VertexCompanyDO company2 = new VertexCompanyDO();
        company2.setId(UUID.randomUUID().toString());

        vertexService.insert(List.of(company1, company2));
        EdgeDO<VertexCompanyDO, VertexCompanyDO> saved =
                edgeService.upsert(company1, company2, UUID.randomUUID().toString());

        assertEquals(saved.getFrom().getCollection(), company1.getCollection());
        assertEquals(saved.getFrom().getId(), company1.getId());
        assertEquals(saved.getTo().getCollection(), company2.getCollection());
        assertEquals(saved.getTo().getId(), company2.getId());

       saved = edgeService.upsert(company1, company2, UUID.randomUUID().toString());
       assertEquals(2, saved.getFingerprints().size());
    }

    @Test
    public void Test_Traverse_Success() {
        String companyId = UUID.randomUUID().toString();
        String actionId = UUID.randomUUID().toString();
        String dateId = UUID.randomUUID().toString();

        EdgeAO edge1 = new EdgeAO(
                new EdgeAOVertex("company", companyId),
                new EdgeAOVertex("action", actionId),
                UUID.randomUUID().toString()
        );
        EdgeAO edge2 = new EdgeAO(
                new EdgeAOVertex("action", actionId),
                new EdgeAOVertex("date", dateId),
                UUID.randomUUID().toString()
        );
        edgeService.add(List.of(edge1, edge2));
        List<EdgeDO<? extends VertexDO, ? extends VertexDO>> edges =
                edgeService.traverse(edge1.getFrom().getType(), edge1.getFrom().getId(), 2);

        assertEquals(2, edges.size());
        assertTrue(edges.stream().anyMatch(e ->
                e.getTo().getId().equals(edge1.getTo().getId()) &&
                e.getFrom().getId().equals(edge1.getFrom().getId())));
        assertTrue(edges.stream().anyMatch(e ->
                e.getTo().getId().equals(edge2.getTo().getId()) &&
                e.getFrom().getId().equals(edge2.getFrom().getId())));
    }
}
