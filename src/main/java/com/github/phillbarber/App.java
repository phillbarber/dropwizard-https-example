package com.github.phillbarber;

import com.github.phillbarber.resources.MessageResource;
import com.google.common.io.Resources;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.ClientBuilder;
import java.io.File;

public class App extends Application<AppConfig> {

    public static void main(final String[] args) throws Exception {

        File file = new File(Resources.getResource("config.yml").toURI());

        new App().run(new String[]{"server", file.getAbsolutePath()});
    }

    @Override
    public String getName() {
        return "demo";
    }

    @Override
    public void initialize(final Bootstrap<AppConfig> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final AppConfig appConfig,
                    final Environment environment) {



        environment.jersey().register(new MessageResource());
    }

}
