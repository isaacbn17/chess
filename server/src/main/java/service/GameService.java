package service;

import dataaccess.*;
import model.*;

import java.util.Objects;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void listGames() {

    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        if (! authDAO.getAuthData().containsKey(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }

        return gameDAO.addGame(gameName);
    }
}
