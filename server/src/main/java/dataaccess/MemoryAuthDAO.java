package dataaccess;

import java.util.ArrayList;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void clear() {
      authTokens.clear();
    }

    @Override
    public AuthData addAuthToken(String username) {
      int authToken = 1000 + new Random().nextInt(9000);

      AuthData authData = new AuthData(Integer.toString(authToken), username);
      authTokens.put(username, authData);
      return authData;
    }

    @Override
    public Map<String, AuthData> getAuthData() {
        return authTokens;
    }

}
