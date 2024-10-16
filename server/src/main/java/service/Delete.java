package service;

import dataaccess.*;
import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.ArrayList;
import java.util.HashMap;

public class Delete {

    public void clearUsers(ArrayList<UserData> users) {
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.clear(users);
    }
    public void clearGames(HashMap<String, GameData> games) {
        GameDAO gameDAO = new MemoryGameDAO();
        gameDAO.clear(games);
    }
    public void clearAuthTokens(ArrayList<AuthData> authTokens) {
        AuthDAO authDAO = new MemoryAuthDAO();
        authDAO.clear(authTokens);
    }
}
