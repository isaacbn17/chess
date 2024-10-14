package dataaccess;

import model.UserData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface UserDAO {

  public void clear(ArrayList<UserData> users);
  public void addUser();
}
