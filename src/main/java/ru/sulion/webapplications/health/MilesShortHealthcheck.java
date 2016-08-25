package ru.sulion.webapplications.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * Created by sulion on 25.08.16.
 */
public class MilesShortHealthcheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy();//nothing to check yet
    }
}
