package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

//  ArrayList<UserData> users = new ArrayList<>();
static Map<String, UserData> users = new HashMap<>();

  @Override
  public void clear() {
    users.clear();
  }

  @Override
  public UserData addUser(UserData newUser) {
    users.put(newUser.username(), newUser);
    return newUser;
  }
  @Override
  public String getUser(String username) {
    return users.get(username).username();
  }

}
