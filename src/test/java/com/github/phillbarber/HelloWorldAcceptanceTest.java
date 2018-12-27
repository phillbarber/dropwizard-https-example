package com.github.phillbarber;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HelloWorldAcceptanceTest {

    @ClassRule
    public final static DropwizardAppRule<AppConfig> appRule = new DropwizardAppRule<>(App.class);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().port(8888));


    @Test
    public void hello() {

        wireMockRule.stubFor(get("/remote-service/message").willReturn(aResponse().withBody("Hello World!").withStatus(200)));

        Response response = appRule.client().target("http://localhost:8080/message").request().get();

        Object entity = response.readEntity(String.class);

        assertThat(entity, is("Hello World!"));

    }
}
