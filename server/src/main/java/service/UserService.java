package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.LoginRequest;
import model.RegisterResult;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public RegisterResult registerUser(UserData newUser) throws DataAccessException {
        if (newUser.username() == null || newUser.password() == null || newUser.email() == null ||
                newUser.username().isEmpty() || newUser.password().isEmpty() || newUser.email().isEmpty()) {
            throw new DataAccessException("Error: bad request");
        }
        if (userDAO.getUser(newUser.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        String hashedPassword = hashPassword(newUser.password());
        userDAO.addUser(new UserData(newUser.username(), hashedPassword, newUser.email()));
        AuthData authData = authDAO.addAuthData(newUser.username());
        return new RegisterResult(newUser.username(), authData.authToken());
    }

    public RegisterResult loginUser(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password = loginRequest.password();
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new DataAccessException("Error: unauthorized");
        }
        UserData userData = userDAO.getUser(username);
        if (userData == null) { throw new DataAccessException("Error: unauthorized"); }
        if (! BCrypt.checkpw(password, userData.password())) { throw new DataAccessException("Error: unauthorized"); }

        AuthData authData = authDAO.addAuthData(username);

        return new RegisterResult(username, authData.authToken());
    }

    public void logoutUser(String authToken) throws DataAccessException {
        if (authDAO.getAuthData(authToken) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        authDAO.removeAuthToken(authToken);
    }
}
