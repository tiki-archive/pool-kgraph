/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph_generate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class KGraphGenerate {
    public static void main(final String... args) {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache doMustache = mustacheFactory.compile("com/mytiki/kgraph_generate/GraphVertexDO.mustache");
        Mustache repositoryMustache = mustacheFactory.compile("com/mytiki/kgraph_generate/GraphVertexRepository.mustache");
        Mustache lookupMustache = mustacheFactory.compile("com/mytiki/kgraph_generate/GraphVertexLookup.mustache");
        try {
            KGraphDefs defs = load();
            Map<String, List<Map<String,Object>>> lookupPlaceholders = new HashMap<>(1);
            List<Map<String,Object>> lookupList = new ArrayList<>(defs.getVertices().size());
            defs.getVertices().forEach(vertex -> {
                Map<String, Object> placeholders = new HashMap<>(3);
                String vertexPascal = camelToPascal(vertex.getType());
                placeholders.put("type_pascal", vertexPascal);
                placeholders.put("type_camel", vertex.getType());
                if(vertex.getFields() != null) {
                    List<Map<String, String>> fields = new ArrayList<>(vertex.getFields().size());
                    Set<String> imports = new HashSet<>();
                    vertex.getFields().forEach(m -> {
                        HashMap<String, String> map = new HashMap<>(3);
                        map.put("name_pascal", camelToPascal(m.getName()));
                        map.put("name_camel", m.getName());
                        map.put("clazz", m.getClazz());
                        map.put("type_java", m.getClazz().substring(m.getClazz().lastIndexOf(".")+1));
                        map.put("type_json", jsonType(m.getClazz()));
                        fields.add(map);
                        imports.add(m.getClazz());
                    });
                    placeholders.put("fields", fields);
                    placeholders.put("imports", imports);
                }
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
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        InputStream is = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("com/mytiki/kgraph_generate/defs.yaml");
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

    private static String jsonType(String clazz){
        switch (clazz){
            case "java.lang.Boolean":
                return "boolean";
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.lang.Short":
            case "java.lang.Double":
            case "java.lang.Float":
            case "java.lang.Number":
            case "java.math.BigDecimal":
            case "java.math.BigInteger":
                return "number";
            case "java.lang.String":
            case "java.time.OffsetDateTime":
            case "java.time.LocalDateTime":
            case "java.time.LocalDate":
            case "java.time.Instant":
            case "java.sql.Timestamp":
            case "java.sql.Date":
            case "java.util.Date":
            case "java.lang.byte[]":
            case "java.util.UUID":
            case "java.lang.Character":
            case "java.time.ZonedDateTime":
            default:
                return "string";
        }
    }
}
