package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.AuthData;

public interface AuthDAO {
  public void clear() throws DataAccessException;
  public AuthData addAuthData(String username) throws DataAccessException;
  public Map<String, AuthData> getAuthData() throws DataAccessException;
}
