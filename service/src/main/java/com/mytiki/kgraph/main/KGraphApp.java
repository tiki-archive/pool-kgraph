/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
        KGraphConfig.class
})
@SpringBootApplication
public class KGraphApp {

    public static void main(final String... args) {
        SpringApplication.run(KGraphApp.class, args);
    }
}