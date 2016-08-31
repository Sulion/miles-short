package ru.sulion.webapplications.db;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.mapdb.DB;
import org.mapdb.Serializer;
import ru.sulion.webapplications.api.*;
import ru.sulion.webapplications.core.KeyComposer;

import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by sulion on 28.08.16.
 */
public class MapDBAccountService implements AccountService {
    protected static final String RECORDS_DB_PREFIX = "records/";
    private static final String ACCOUNT_DB = "ACCOUNTS";
    public static final String NEGATIVE_MESSAGE = "This account ID already exists";
    public static final SignUpResponse NEGATIVE_RESPONSE = new SignUpResponse(false, NEGATIVE_MESSAGE, null);
    public static final String AFFIRMATIVE_MESSAGE = "Your account was successfully created";
    private final DB db;
    private final KeyComposer keyComposer;
    private final URL domainURL;
    private final SecureRandom random = new SecureRandom();

    @Inject
    public MapDBAccountService(DB db, KeyComposer keyComposer, @Named("HTTP_URL") String domainAddressPart)
            throws MalformedURLException {
        this.db = db;
        this.keyComposer = keyComposer;
        this.domainURL = domainAddressPart == null? null : new URL(domainAddressPart);
    }

    protected String generatePassword() {
        KeyComposer keyComposer = new KeyComposer(null);
        return keyComposer.toBase62((new BigInteger(45, random)).longValue());
    }

    @Override
    public List<Redirect> getRecordsFor(String accountId) {
        Map<String, Redirect> map = openAccountRecordsMap(accountId);
        if(map.size() == 0)
            return Collections.emptyList();
        return new ArrayList<>(map.values());
    }

    private Map<String, Redirect> openAccountRecordsMap(String name) {
        return db.treeMap(RECORDS_DB_PREFIX + name)
                .keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA).createOrOpen();
    }

    private Map<String, Redirect> openGlobalRecordsMap() {
        return db.treeMap(RedirectDictionary.DICT_NAME)
                .keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA).createOrOpen();
    }

    private Map<String, String> openUserMap() {
        return db.treeMap(ACCOUNT_DB)
                .keySerializer(Serializer.STRING).valueSerializer(Serializer.STRING).createOrOpen();
    }

    @Override
    public RegisteredURLResponse registerRecordFor(String accountId, RegisterURLRequest request) {
        Map<String, Redirect> map = openAccountRecordsMap(accountId);

        String key = keyComposer.toShortURL(request.getUrl());
        Redirect redirect = map.computeIfAbsent(request.getUrl(), (g) -> new Redirect(
                Optional.ofNullable(
                        Response.Status.fromStatusCode(request.getRedirectType()))
                        .orElse(Response.Status.FOUND),
                request.getUrl(), key));
        openGlobalRecordsMap().put(key, redirect);
        db.commit();
        try {
            return new RegisteredURLResponse((new URL(domainURL, key)).toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException("This can't be happening");
        }
    }

    @Override
    public SignUpResponse register(SignUpRequest request) {
        String accountId = request.getAccountId();
        Map<String, String> accounts = openUserMap();
        if(accounts.containsKey(accountId))
            return NEGATIVE_RESPONSE;
        else {
            String password = generatePassword();
            accounts.put(accountId, password);
            db.commit();
            return new SignUpResponse(true, AFFIRMATIVE_MESSAGE, password);
        }
    }

    @Override
    public boolean checkAccount(String username, String password) {
        Map<String, String> accounts = openUserMap();
        return accounts.containsKey(username) && accounts.get(username).equals(password);
    }
}
