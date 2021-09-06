/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.features.latest.add;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class TestFetch implements DataFetcher<String> {
    @Override
    public String get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        return "hello";
    }
}
