package service;

import dataaccess.*;

public class Delete {

    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;

    public Delete(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
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
