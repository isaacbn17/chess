package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.UserData;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public UserData registerUser(UserData newUser) throws DataAccessException {
        if (userDAO.getUser(newUser.username()) != null) {
            throw new DataAccessException("User already exists");
        }
        userDAO.addUser(newUser);
        authDAO.addAuthToken(newUser.username());

        return newUser;
        // return dataAccess.getUser(newUser.username());
    }
}
