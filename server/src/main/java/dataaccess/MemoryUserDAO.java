package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {

  @Override
  public void clear(ArrayList<UserData> users) {
    users.clear();
  }

  @Override
  public void addUser() {

  }
}
