package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.UserData;

public class Register {

    UserDAO userDAO;
    AuthDAO authDAO;

    public Register(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public UserData registerUser(UserData newUser) {
      userDAO.addUser(newUser);
      return newUser;
    }
}
