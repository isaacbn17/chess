package dataaccess;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, AuthData> authDataMap = new HashMap<>();
    Set<Integer> authTokens = new HashSet<>();
//    Integer authToken = 1000;
    @Override
    public void clear() {
      authDataMap.clear();
    }

    @Override
    public AuthData addAuthData(String username) {
      int authToken = 1000 + new Random().nextInt(9000);
      while (authTokens.contains(authToken)) {
          authToken = 1000 + new Random().nextInt(9000);
      }
      authTokens.add(authToken);

      AuthData authData = new AuthData(Integer.toString(authToken), username);
      authDataMap.put(username, authData);
      return authData;
    }

    @Override
    public Map<String, AuthData> getAuthData() {
        return authDataMap;
    }

    @Override
    public Set<Integer> getAuthTokens() {
        return authTokens;
    }

}
