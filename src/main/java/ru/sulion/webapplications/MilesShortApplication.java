package ru.sulion.webapplications;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.sulion.webapplications.auth.MilesShortConfigAuthorizer;
import ru.sulion.webapplications.auth.MilesShortConfigurationAutheticator;
import ru.sulion.webapplications.core.User;
import ru.sulion.webapplications.health.MilesShortHealthcheck;
import ru.sulion.webapplications.resources.ConfigurationResource;
import ru.sulion.webapplications.resources.RedirectingResource;

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
        bootstrap.addBundle(new AssetsBundle("/assets", "/help", "help.html"));

    }

    @Override
    public void run(final MilesShortConfiguration configuration,
                    final Environment environment) {
        Injector injector = Guice.createInjector(new MilesShortModule(configuration));
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(injector.getInstance(MilesShortConfigurationAutheticator.class))
                        .setAuthorizer(new MilesShortConfigAuthorizer())
                        .setRealm("URL MANAGEMENT")
                        .buildAuthFilter()));
        final ConfigurationResource configurationResource = injector.getInstance(ConfigurationResource.class);
        final RedirectingResource redirectingResource = injector.getInstance(RedirectingResource.class);
        final MilesShortHealthcheck healthcheck = new MilesShortHealthcheck();
        environment.healthChecks().register("health", healthcheck);
        environment.jersey().register(configurationResource);
        environment.jersey().register(redirectingResource);
    }

}
