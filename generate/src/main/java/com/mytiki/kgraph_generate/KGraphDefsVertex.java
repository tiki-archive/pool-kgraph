/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph_generate;

import java.util.List;

public class KGraphDefsVertex {
   private String type;
   private List<KGraphDefsField> fields;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<KGraphDefsField> getFields() {
        return fields;
    }

    public void setFields(List<KGraphDefsField> fields) {
        this.fields = fields;
    }
}
