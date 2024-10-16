package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {

  ArrayList<UserData> users = new ArrayList<>();

  @Override
  public void clear() {
    users.clear();
  }

  @Override
  public UserData addUser(UserData newUser) {
    users.add(newUser);
    return newUser;
  }
}
