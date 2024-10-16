package dataaccess;

import java.util.ArrayList;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
  private ArrayList<AuthData> authTokens = new ArrayList<>();
  private int nextAuthToken = 1234;

  @Override
  public void clear() {
    authTokens.clear();
  }

  @Override
  public AuthData addAuthToken(String username) {
    AuthData authData = new AuthData(Integer.toString(nextAuthToken++), username);
    authTokens.add(authData);
    return authData;
  }
}
