package dataaccess;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, AuthData> authDataMap = new HashMap<>();
    Set<String> authTokens = new HashSet<>();

    @Override
    public void clear() {
      authDataMap.clear();
    }

    @Override
    public AuthData addAuthData(String username) {
        String authToken = UUID.randomUUID().toString();

        AuthData authData = new AuthData(authToken, username);
        authDataMap.put(username, authData);
        return authData;
    }

    @Override
    public Map<String, AuthData> getAuthData() {
        return authDataMap;
    }

    @Override
    public Set<String> getAuthTokens() {
        return authTokens;
    }

}
