package service;

import dataaccess.*;

public class DeleteService {

    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;

    public DeleteService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void clearUsers() throws DataAccessException {
        userDAO.clear();
    }
    public void clearGames() throws DataAccessException {
        gameDAO.clear();
    }
    public void clearAuthTokens() throws DataAccessException {
        authDAO.clear();
    }
}
