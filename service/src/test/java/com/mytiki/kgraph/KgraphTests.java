/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph;

import com.mytiki.kgraph.features.latest.edge.*;
import com.mytiki.kgraph.features.latest.vertex.*;
import com.mytiki.kgraph.main.KGraphApp;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {KGraphApp.class}
)
@ActiveProfiles(profiles = {"local", "test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KgraphTests {

    @Autowired
    EdgeRepository edgeRepository;

    @Autowired
    EdgeService edgeService;

    @Autowired
    VertexCompanyRepository companyRepository;

    @Autowired
    VertexDataBreachRepository dataBreachRepository;
    
    @Test
    public void Test_UpsertEdge_Success(){
        VertexCompanyDO vertex1 = new VertexCompanyDO();
        vertex1.setId("company/1235951");
        VertexDataBreachDO vertex2 = new VertexDataBreachDO();
        vertex2.setId("dataBreach/1235938");

        EdgeDO<VertexDO, VertexDataBreachDO> edge1 = new EdgeDO<>();
        edge1.setFingerprints(Set.of("12r8095hr3"));
        edge1.setFrom(vertex1);
        edge1.setTo(vertex2);
        
        EdgeDO<?,?> saved = edgeRepository.upsert(edge1);

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void Test_UpsertEdges_Success(){
        VertexCompanyDO vertex1 = new VertexCompanyDO();
        vertex1.setId("company/1235951");
        VertexDataBreachDO vertex2 = new VertexDataBreachDO();
        vertex2.setId("dataBreach/1235938");

        EdgeDO<VertexDO, VertexDataBreachDO> edge1 = new EdgeDO<>();
        edge1.setFingerprints(Set.of("12r8095hr3"));
        edge1.setFrom(vertex1);
        edge1.setTo(vertex2);

        EdgeDO<VertexDO, VertexDataBreachDO> edge2 = new EdgeDO<>();
        edge2.setFingerprints(Set.of("eafafeaf1"));
        edge2.setFrom(vertex1);
        edge2.setTo(vertex2);

        List<EdgeDO<?,?>> saved = edgeRepository.upsertAll(List.of(edge1, edge2));

        assertNotNull(saved);
    }

    @Test
    public void Test_Compress_Success() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        EdgeAO edge1 = new EdgeAO(
                new EdgeAOVertex("company", "microsoft"),
                new EdgeAOVertex("dataBreach", "dummy"),
                "abc12345");

        EdgeAO edge2 = new EdgeAO(
                new EdgeAOVertex("company", "microsoft"),
                new EdgeAOVertex("dataBreach", "dummy"),
                "abc12345");

        EdgeAO edge3 = new EdgeAO(
                new EdgeAOVertex("dataBreach", "dummy"),
                new EdgeAOVertex("company", "microsoft"),
                "edf12345");

        EdgeAO edge4 = new EdgeAO(
                new EdgeAOVertex("company", "amazon"),
                new EdgeAOVertex("dataBreach", "dummy"),
                "fahoifhioaf2");

        EdgeAO edge5 = new EdgeAO(
                new EdgeAOVertex("company", "amazon"),
                new EdgeAOVertex("dataBreach", "dummy"),
                "abc12345");

        List<EdgeDO<? extends VertexDO, ? extends VertexDO>> cmp =
                edgeService.compress(List.of(edge1,edge2,edge3,edge4,edge5));

        assertEquals(cmp.size(), 2);
    }

    @Test
    public void Test_VertexUpsert_Success() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        VertexCompanyDO vertexCompanyDO = new VertexCompanyDO();
        vertexCompanyDO.setId("facebook.com");
        VertexCompanyDO saved = companyRepository.upsert(vertexCompanyDO);
        assertNotNull(saved);
    }

    @Test
    public void Test_Graph_Success(){

        EdgeAO edge1 = new EdgeAO(
                new EdgeAOVertex("company","facebook.com"),
                new EdgeAOVertex("occurrence","1"),
                "abfouaeihafb");

        EdgeAO edge2 = new EdgeAO(
                new EdgeAOVertex("occurrence","1"),
                new EdgeAOVertex("action","email"),
                "tigeagohaeoi");

        EdgeAO edge3 = new EdgeAO(
                new EdgeAOVertex("occurrence","1"),
                new EdgeAOVertex("date","03-19-2022"),
                "1330tuhfnes");


        List<EdgeAO> saved = edgeService.add(List.of(edge1, edge2, edge3));

        assertNotNull(saved);
    }
}
