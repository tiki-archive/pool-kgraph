/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.utilities;

public interface Constants {

    String MODULE_DOT_PATH = "com.mytiki.kgraph";
    String MODULE_SLASH_PATH = "com/mytiki/kgraph";

    String SLICE_FEATURES = "features";
    String SLICE_LATEST = "latest";

    String PACKAGE_FEATURES_LATEST_DOT_PATH = MODULE_DOT_PATH + "." + SLICE_FEATURES + "." + SLICE_LATEST;
    String PACKAGE_FEATURES_LATEST_SLASH_PATH = MODULE_SLASH_PATH + "/" + SLICE_FEATURES + "/" + SLICE_LATEST;

    String CLAIMS_ISS = "iss";
    String CLAIM_ISS_BOUNCER = "com.mytiki.bouncer";

    String ROLE_USER = "ROLE_USER";
    String ROLE_ETL = "ROLE_ETL";
    String ROLE_INGEST = "ROLE_INGEST";
}
