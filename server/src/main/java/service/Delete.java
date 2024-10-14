package service;

import dataaccess.*;
import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.ArrayList;

public class Delete {

    public void clearUsers(ArrayList<UserData> users) throws DataAccessException {
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.clear(users);
    }
    public void clearGames(ArrayList<GameData> games) throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        gameDAO.clear(games);
    }
    public void clearAuthTokens(ArrayList<AuthData> authTokens) throws DataAccessException {
        AuthDAO authDAO = new MemoryAuthDAO();
        authDAO.clear(authTokens);
    }
}
