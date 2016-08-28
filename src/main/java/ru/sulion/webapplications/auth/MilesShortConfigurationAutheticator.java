package ru.sulion.webapplications.auth;

import com.google.inject.Inject;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import ru.sulion.webapplications.core.AccountService;
import ru.sulion.webapplications.core.User;

import java.util.Optional;

/**
 * Created by sulion on 27.08.16.
 */
public class MilesShortConfigurationAutheticator  implements Authenticator<BasicCredentials, User> {

    private final AccountService accountService;

    @Inject
    public MilesShortConfigurationAutheticator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (accountService.checkAccount(credentials.getUsername(), credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername()));
        }
        return Optional.empty();
    }
}
