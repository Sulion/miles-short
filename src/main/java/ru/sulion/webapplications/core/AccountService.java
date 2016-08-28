package ru.sulion.webapplications.core;

import ru.sulion.webapplications.api.Redirect;
import ru.sulion.webapplications.api.RegisteredURLResponse;
import ru.sulion.webapplications.api.SignUpRequest;
import ru.sulion.webapplications.api.SignUpResponse;

import java.util.List;

/**
 * Created by sulion on 28.08.16.
 */
public interface AccountService {
    List<Redirect> getRecordsFor(String accountId);

    RegisteredURLResponse registerRecordFor(String name);

    SignUpResponse register(SignUpRequest request);
}
