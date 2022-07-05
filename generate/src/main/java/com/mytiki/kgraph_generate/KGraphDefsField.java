/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph_generate;

public class KGraphDefsField {
   private String name;
   private String clazz;
   private String collection;

   private boolean insert;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getClazz() {
      return clazz;
   }

   public void setClazz(String clazz) {
      this.clazz = clazz;
   }

   public String getCollection() {
      return collection;
   }

   public void setCollection(String collection) {
      this.collection = collection;
   }

   public boolean getInsert() {
      return insert;
   }

   public void setInsert(boolean insert) {
      this.insert = insert;
   }
}
