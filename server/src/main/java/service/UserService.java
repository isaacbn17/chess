package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.RegisterRequest;
import model.UserData;

import java.util.Objects;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterRequest registerUser(UserData newUser) throws DataAccessException {
        if (newUser.username() == null || newUser.password() == null || newUser.email() == null ||
                newUser.username().isEmpty() || newUser.password().isEmpty() || newUser.email().isEmpty()) {
            throw new DataAccessException("Error: bad request");
        }
        if (userDAO.getUser(newUser.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        userDAO.addUser(newUser);
        AuthData authData = authDAO.addAuthToken(newUser.username());
        return new RegisterRequest(newUser.username(), authData.authToken());
    }
}
