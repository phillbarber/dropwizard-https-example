package com.github.phillbarber;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.JettySettings;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.AdminRequestHandler;
import com.github.tomakehurst.wiremock.http.HttpServer;
import com.github.tomakehurst.wiremock.http.HttpServerFactory;
import com.github.tomakehurst.wiremock.http.StubRequestHandler;
import com.github.tomakehurst.wiremock.jetty9.JettyHttpServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Rule;
import org.junit.Test;
import wiremock.org.eclipse.jetty.io.NetworkTrafficListener;
import wiremock.org.eclipse.jetty.server.ConnectionFactory;
import wiremock.org.eclipse.jetty.server.ServerConnector;
import wiremock.org.eclipse.jetty.server.SslConnectionFactory;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class HelloWorldAcceptanceTest {

    public static final WireMockConfiguration OPTIONS = options()
            .httpsPort(8000)

            .keystorePath("/home/pergola/dev-workspace/dropwizard-https-example/target/keys/server/5-keystore.pkcs12")
            .keystoreType("PKCS12")
            .keystorePassword("abcdefg")

            .trustStorePath("/home/pergola/dev-workspace/dropwizard-https-example/target/keys/client/5-keystore.pkcs12")
            .trustStoreType("PKCS12")
            .trustStorePassword("abcdefg")
            .needClientAuth(true)

            .httpServerFactory(new Pkcs12FriendlyHttpsServerFactory())
            ;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(OPTIONS);

    public static void main(String[] args) {
        new WireMockServer(OPTIONS).start();
    }

    @Test
    public void hello() {

        wireMockRule.stubFor(get("/remote-service/message").willReturn(aResponse().withBody("Hello World!").withStatus(200)));



    }

    public static class Pkcs12FriendlyHttpsServerFactory implements HttpServerFactory {
        @Override
        public HttpServer buildHttpServer(Options options, AdminRequestHandler adminRequestHandler, StubRequestHandler stubRequestHandler) {
            return new JettyHttpServer(
                    options,
                    adminRequestHandler,
                    stubRequestHandler
            ) {
                @Override
                protected ServerConnector createServerConnector(String bindAddress, JettySettings jettySettings, int port, NetworkTrafficListener listener, ConnectionFactory... connectionFactories) {
                    if (port == options.httpsSettings().port()) {
                        SslConnectionFactory sslConnectionFactory = (SslConnectionFactory) connectionFactories[0];
                        sslConnectionFactory.getSslContextFactory().setKeyStorePassword(options.httpsSettings().keyStorePassword());
                        connectionFactories = new ConnectionFactory[] {sslConnectionFactory, connectionFactories[1]};
                    }
                    return super.createServerConnector(bindAddress, jettySettings, port, listener, connectionFactories);
                }
            };

        }
    }
}
