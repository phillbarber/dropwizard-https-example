package com.github.phillbarber;

import com.github.phillbarber.resources.HelloWorldResource;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import javax.ws.rs.core.Response;

public class HelloWorldAcceptanceTest {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HelloWorldResource())
            .build();

    @Test
    public void hello() {
        Response response = resources.client().target("/helloworld").request().get();

        Object entity = response.readEntity(String.class);

        assertThat(entity, is("HELLO"));

    }
}
