package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.AuthData;

public interface AuthDAO {
  public void clear();
  public AuthData addAuthToken(String username);
  public Map<String, AuthData> getAuthData();
}
