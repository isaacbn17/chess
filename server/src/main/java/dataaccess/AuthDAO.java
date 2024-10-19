package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.AuthData;

public interface AuthDAO {
  public void clear();
  public AuthData addAuthData(String username);
  public Map<String, AuthData> getAuthData();
  public Set<Integer> getAuthTokens();
}
