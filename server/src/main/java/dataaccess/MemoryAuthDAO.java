package dataaccess;

import java.util.*;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, AuthData> authDataMap = new HashMap<>();

    @Override
    public void clear() {
      authDataMap.clear();
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AuthData addAuthData(String username) {
        String token = generateToken();
        AuthData authData = new AuthData(token, username);
        authDataMap.put(token, authData);
        return authData;
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return authDataMap.get(authToken);
    }

    @Override
    public void removeAuthToken(String authToken) {
        authDataMap.remove(authToken);
    }

}