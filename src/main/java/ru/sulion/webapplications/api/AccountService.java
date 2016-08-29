package ru.sulion.webapplications.api;

import java.util.List;

/**
 * Created by sulion on 28.08.16.
 */
public interface AccountService {
    List<Redirect> getRecordsFor(String accountId);

    RegisteredURLResponse registerRecordFor(String name, RegisterURLRequest request);

    SignUpResponse register(SignUpRequest request);

    boolean checkAccount(String username, String password);
}
