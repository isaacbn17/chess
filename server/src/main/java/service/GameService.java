package service;

import dataaccess.*;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ArrayList<GameSimplified> listGames(String authToken) throws DataAccessException {
        if (! authDAO.getAuthData().containsKey(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        HashMap<Integer, GameData> games = gameDAO.getGames();
        ArrayList<GameSimplified> gamesReturn = new ArrayList<>();
        for (GameData game : games.values()) {
            gamesReturn.add(new GameSimplified(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
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

    public GameData joinGame(String authToken, JoinRequest joinRequest) throws DataAccessException {
        if (!authDAO.getAuthData().containsKey(authToken)) { throw new DataAccessException("Error: unauthorized"); }
        if (joinRequest.gameID() == null) { throw new DataAccessException("Error: bad request"); }
        String color = joinRequest.playerColor();
        GameData game=gameDAO.getGame(joinRequest.gameID());
        if (game == null || color == null) {
            throw new DataAccessException("Error: bad request");
        }
        if (color.equalsIgnoreCase("WHITE")) {
            if (game.whiteUsername() != null) { throw new DataAccessException("Error: already taken"); }
            AuthData authData=authDAO.getAuthData().get(authToken);
            return gameDAO.updateGames(game.gameID(), "WHITE", authData.username());
        }
        else if (color.equalsIgnoreCase("BLACK")) {
            if (game.blackUsername() != null) { throw new DataAccessException("Error: already taken"); }
            AuthData authData=authDAO.getAuthData().get(authToken);
            return gameDAO.updateGames(game.gameID(), "BLACK", authData.username());
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }
}
