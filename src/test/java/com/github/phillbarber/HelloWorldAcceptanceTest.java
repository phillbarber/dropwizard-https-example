package com.github.phillbarber;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Rule;
import org.junit.Test;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HelloWorldAcceptanceTest {

    public static final WireMockConfiguration OPTIONS = options()
            .httpsPort(8000)
            .keystorePath("/home/pergola/dev-workspace/dropwizard-https-example/target/keys/remote-server/5-keystore.pkcs12")
            .keystoreType("PKCS12")
            .keystorePassword("abcdefg");

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(OPTIONS);

    public static void main(String[] args) {
        new WireMockServer(OPTIONS).start();
    }

    @Test
    public void hello() {

        wireMockRule.stubFor(get("/remote-service/message").willReturn(aResponse().withBody("Hello World!").withStatus(200)));



    }
}
