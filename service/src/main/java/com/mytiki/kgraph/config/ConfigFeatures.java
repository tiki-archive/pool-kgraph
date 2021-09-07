/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.config;

import com.mytiki.kgraph.features.latest.add.AddConfig;
import com.mytiki.kgraph.features.latest.graph.GraphConfig;
import com.mytiki.kgraph.features.latest.vertex.VertexConfig;
import org.springframework.context.annotation.Import;

@Import({
        AddConfig.class,
        GraphConfig.class,
        VertexConfig.class
})
public class ConfigFeatures {}
