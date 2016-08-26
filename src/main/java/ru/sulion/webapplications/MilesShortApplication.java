package ru.sulion.webapplications;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.sulion.webapplications.health.MilesShortHealthcheck;
import ru.sulion.webapplications.resources.WebInterfaceResource;

public class MilesShortApplication extends Application<MilesShortConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MilesShortApplication().run(args);
    }

    @Override
    public String getName() {
        return "MilesShort";
    }

    @Override
    public void initialize(final Bootstrap<MilesShortConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final MilesShortConfiguration configuration,
                    final Environment environment) {
        final WebInterfaceResource webInterfaceResource = new WebInterfaceResource();
        final MilesShortHealthcheck healthcheck = new MilesShortHealthcheck();
        environment.healthChecks().register("health", healthcheck);
        environment.jersey().register(webInterfaceResource);
    }

}
