package dataaccess;

import java.util.ArrayList;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
  ArrayList<AuthData> authTokens = new ArrayList<>();
  @Override
  public void clear() {
    authTokens.clear();
  }
}
