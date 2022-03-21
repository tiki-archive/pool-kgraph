/*
 * Copyright (c) My Tiki, Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.kgraph.config;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoAuditing;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@EnableArangoAuditing
public class ConfigArangodb implements ArangoConfiguration {
    @Value("${arangodb.host:127.0.0.1}")
    String host;

    @Value("${arangodb.port:8529}")
    Integer port;

    @Value("${arangodb.user:}")
    String user;

    @Value("${arangodb.password:}")
    String password;

    @Value("${arangodb.timeout:0}")
    Integer timeout;

    @Value("${arangodb.useSsl:false}")
    Boolean useSsl;

    @Value("${arangodb.tls.ca:}")
    String tlsCA;

    @Autowired
    private ConfigProperties properties;

    @Override
    public ArangoDB.Builder arango() {
            ArangoDB.Builder builder = new ArangoDB.Builder()
                    .useSsl(useSsl)
                    .host(host, port)
                    .user(user)
                    .password(password)
                    .timeout(timeout);
        try {
            if(tlsCA != null && !tlsCA.isEmpty()) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509Certificate caCert = (X509Certificate) cf.generateCertificate(
                        new ByteArrayInputStream(Base64.getDecoder().decode(tlsCA)));
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                ks.load(null);
                ks.setCertificateEntry("caCert", caCert);
                tmf.init(ks);
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, tmf.getTrustManagers(), null);
                return builder.sslContext(sslContext);
            }else
                return builder;
        }catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException | KeyManagementException e) {
            throw new RuntimeException("Failed to initialize ArangoDB");
        }
    }

    @Override
    public String database() {
        return properties.getDbName();
    }
}
