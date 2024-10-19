package service;

import dataaccess.*;
import model.*;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ArrayList<GameList> listGames(String authToken) throws DataAccessException {
        if (! authDAO.getAuthData().containsKey(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        ArrayList<GameData> games = gameDAO.getGames();
        ArrayList<GameList> gamesReturn = new ArrayList<>();
        for (GameData game : games) {
            gamesReturn.add(new GameList(game.gameID(), game.whiteUserName(), game.blackUsername(), game.gameName()));
        }
        return gamesReturn;
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        if (! authDAO.getAuthData().containsKey(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (gameName == null || gameName.isEmpty()) {
            throw new DataAccessException("Error: bad request");
        }

        return gameDAO.addGame(gameName);
    }
}
