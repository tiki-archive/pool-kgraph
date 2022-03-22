/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.config;

import com.mytiki.kgraph.features.latest.big_picture.BigPictureConfig;
import com.mytiki.kgraph.features.latest.company.CompanyConfig;
import com.mytiki.kgraph.features.latest.edge.EdgeConfig;
import com.mytiki.kgraph.features.latest.hibp.HibpConfig;
import com.mytiki.kgraph.features.latest.sync.SyncConfig;
import com.mytiki.kgraph.features.latest.vertex.VertexConfig;
import org.springframework.context.annotation.Import;

@Import({
        EdgeConfig.class,
        VertexConfig.class,
        SyncConfig.class,
        CompanyConfig.class,
        HibpConfig.class,
        BigPictureConfig.class
})
public class ConfigFeatures {}
