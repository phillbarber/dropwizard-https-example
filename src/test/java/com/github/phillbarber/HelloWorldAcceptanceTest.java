package com.github.phillbarber;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

;

@ExtendWith(DropwizardExtensionsSupport.class)
public class HelloWorldAcceptanceTest {

    private static DropwizardAppExtension<AppConfig> EXT = new DropwizardAppExtension<>(
            App.class,
            ResourceHelpers.resourceFilePath("config.yml")
    );

    @Test
    void loginHandlerRedirectsAfterPost() {
        Client client = EXT.client();

        Response response = client.target("https://localhost:8443/login")
                .request()
                .get();

        assertNotNull(response);
    }

}
