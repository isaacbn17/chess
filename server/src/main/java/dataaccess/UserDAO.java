package dataaccess;

import model.UserData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface UserDAO {


  public void clear();
  public UserData addUser(UserData newUser);

  public String getUser(String username);
}
