package com.github.phillbarber;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@ExtendWith(DropwizardExtensionsSupport.class)
public class HelloWorldAcceptanceTest {

    private static DropwizardAppExtension<AppConfig> EXT = new DropwizardAppExtension<>(
            App.class,
            ResourceHelpers.resourceFilePath("config.yml")
    );

    @Test
    void message() {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(
                new HttpComponentsClientHttpRequestFactory(
                        HttpClients.custom().setConnectionManager(getConnectionManager()).build()));

        ResponseEntity<String> response = restTemplate.getForEntity("https://localhost:8443/hello", String.class);

        System.out.println(response.getBody());
    }

    private static PoolingHttpClientConnectionManager getConnectionManager() {
        return PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(getSslConnectionSocketFactory()).build();
    }

    private static SSLConnectionSocketFactory getSslConnectionSocketFactory() {
        return SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(getSSLContext())
                .build();
    }

    private static SSLContext getSSLContext() {
        String resourceFilePath = ResourceHelpers.resourceFilePath("keys/server/6-keystore-with-publiccert-only.pfx");

        try {
            return SSLContextBuilder.create()
                    .loadTrustMaterial(
                            new File(resourceFilePath),
                            "abcdefg".toCharArray()
                    )
                    .build();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
