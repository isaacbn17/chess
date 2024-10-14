package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {

  ArrayList<UserData> users = new ArrayList<>();

  @Override
  public void addUser() {

  }
  @Override
  public void clear(ArrayList<UserData> users) {
    users.clear();
  }
}
