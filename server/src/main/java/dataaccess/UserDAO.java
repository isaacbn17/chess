package dataaccess;

import model.UserData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface UserDAO {


  public void clear() throws DataAccessException;
  public UserData addUser(UserData newUser) throws DataAccessException;

  public UserData getUser(String username) throws DataAccessException;
}
