package ru.sulion.webapplications.auth;

import io.dropwizard.auth.Authorizer;
import ru.sulion.webapplications.core.User;

/**
 * Created by sulion on 27.08.16.
 */
public class MilesShortConfigAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String s) {
        return true;
    }
}
