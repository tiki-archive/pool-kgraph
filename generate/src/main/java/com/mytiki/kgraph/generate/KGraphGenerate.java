/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.generate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KGraphGenerate {
    public static void main(final String... args) {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache doMustache = mustacheFactory.compile("com/mytiki/kgraph/generate/GraphVertexDO.mustache");
        Mustache repositoryMustache = mustacheFactory.compile("com/mytiki/kgraph/generate/GraphVertexRepository.mustache");
        Mustache lookupMustache = mustacheFactory.compile("com/mytiki/kgraph/generate/GraphVertexLookup.mustache");
        try {
            KGraphDefs defs = load();
            Map<String, List<Map<String,String>>> lookupPlaceholders = new HashMap<>(1);
            List<Map<String,String>> lookupList = new ArrayList<>(defs.getVertices().size());
            defs.getVertices().forEach(vertex -> {
                Map<String, String> placeholders = new HashMap<>(2);
                String vertexPascal = camelToPascal(vertex);
                placeholders.put("name_pascal", vertexPascal);
                placeholders.put("name_camel", vertex);
                lookupList.add(placeholders);
                try {
                    String doString = render(doMustache, placeholders);
                    String repositoryString = render(repositoryMustache, placeholders);
                    write(doString, "GraphVertex" + vertexPascal + "DO");
                    write(repositoryString, "GraphVertex" + vertexPascal + "Repository");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            lookupList.get(lookupList.size()-1).put("last","true");
            lookupPlaceholders.put("vertices", lookupList);
            String lookupString = render(lookupMustache, lookupPlaceholders);
            write(lookupString, "GraphVertexLookup");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static KGraphDefs load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("com/mytiki/kgraph/generate/defs.json");
        return mapper.readValue(is, KGraphDefs.class);
    }

    private static String render(Mustache mustache, Map<String, ?> data) throws IOException {
        StringWriter writer = new StringWriter();
        mustache.execute(writer, data).flush();
        return writer.toString();
    }

    private static void write(String content, String filename) throws IOException {
        String sources = "service/target/generated-sources/";
        String dir = sources + "java/com/mytiki/kgraph/features/latest/graph/";
        if(!Files.isDirectory(Paths.get(dir)))
            Files.createDirectories(Paths.get(dir));
        Files.write(Paths.get(dir + filename + ".java"), content.getBytes());
    }

    private static String camelToPascal(String camel){
      return camel.substring(0,1).toUpperCase() + camel.substring(1);
    }
}
