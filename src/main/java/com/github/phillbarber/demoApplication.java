package com.github.phillbarber;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class demoApplication extends Application<demoConfiguration> {

    public static void main(final String[] args) throws Exception {
        new demoApplication().run(args);
    }

    @Override
    public String getName() {
        return "demo";
    }

    @Override
    public void initialize(final Bootstrap<demoConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final demoConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
