/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph;

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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
public class VertexTests {

    @Autowired
    private VertexService vertexService;

    @Test
    public void Test_FromType_Success()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        VertexCompanyDO company = new VertexCompanyDO();
        assertEquals(vertexService.fromType(company.getCollection()).getCollection(), company.getCollection());
    }

    @Test
    public void Test_FromType_Fail() {
        assertThrows(NoSuchMethodException.class, () -> vertexService.fromType("0"));
    }

    @Test
    public void Test_Schema_Success() {
        List<?> schema = vertexService.schema();
        assertNotNull(schema);
        assertTrue(schema.size() > 0);
    }

    @Test
    public void Test_Insert_Success() {
        VertexCompanyDO company = new VertexCompanyDO();
        company.setId(UUID.randomUUID().toString());
        List<? extends VertexDO> inserted = vertexService.insert(List.of(company));
        assertEquals(1, inserted.size());
        assertEquals(company.getCollection(), inserted.get(0).getCollection());
        assertEquals(company.getId(), inserted.get(0).getId());
    }

    @Test
    public void Test_Upsert_Success() {
        VertexCompanyDO company = new VertexCompanyDO();
        company.setId(UUID.randomUUID().toString());
        VertexCompanyDO inserted = vertexService.upsert(company);

        assertEquals(company.getCollection(), inserted.getCollection());
        assertEquals(company.getId(), inserted.getId());

        String description = "test description";
        inserted.setDescription(description);
        VertexCompanyDO updated = vertexService.upsert(inserted);

        assertEquals(company.getCollection(), updated.getCollection());
        assertEquals(company.getId(), updated.getId());
        assertEquals(description, updated.getDescription());
    }

    @Test
    public void Test_Get_Success() {
        VertexCompanyDO company = new VertexCompanyDO();
        company.setId(UUID.randomUUID().toString());
        vertexService.upsert(company);

        Optional<VertexCompanyDO> optional =
                vertexService.get(company.getCollection(), company.getId());

        assertTrue(optional.isPresent());
        assertEquals(company.getCollection(), optional.get().getCollection());
        assertEquals(company.getId(), optional.get().getId());
    }

    @Test
    public void Test_Get_Fail_NoVertex() {
        Optional<VertexCompanyDO> optional =
                vertexService.get("company", UUID.randomUUID().toString());
        assertTrue(optional.isEmpty());
    }

    @Test
    public void Test_Get_Fail_NoType() {
        Optional<VertexCompanyDO> optional =
                vertexService.get(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        assertTrue(optional.isEmpty());
    }
}
