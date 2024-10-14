package dataaccess;

import java.util.ArrayList;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
  @Override
  public void clear(ArrayList<AuthData> authTokens) {
    authTokens.clear();
  }
}
