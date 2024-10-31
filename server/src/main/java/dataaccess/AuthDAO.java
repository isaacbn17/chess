package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.AuthData;

import javax.xml.crypto.Data;

public interface AuthDAO {
  public void clear() throws DataAccessException;
  public AuthData addAuthData(String username) throws DataAccessException;
  public AuthData getAuthData(String authToken) throws DataAccessException;
  public void removeAuthToken(String authToken) throws DataAccessException;
}
