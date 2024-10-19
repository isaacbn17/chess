package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.LoginRequest;
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

    public RegisterRequest loginUser(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password =loginRequest.password();
        if (username == null || password == null ||
                username.isEmpty() || password.isEmpty()) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (userDAO.getUser(username) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        UserData userData = userDAO.getUser(username);
        if (! Objects.equals(password, userData.password())) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new RegisterRequest(username, authDAO.getAuthData().get(username).authToken());
    }
}
