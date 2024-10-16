package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.UserData;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public UserData registerUser(UserData newUser) {
      userDAO.addUser(newUser);
      authDAO.addAuthToken(newUser.username());

      return newUser;
    }
}
