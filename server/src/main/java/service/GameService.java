package service;

import dataaccess.*;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
            gamesReturn.add(new GameSimplified(game.gameID(), game.whiteUserName(), game.blackUsername(), game.gameName()));
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

    public void joinGame(String authToken, JoinRequest joinRequest) throws DataAccessException {
        if (!authDAO.getAuthData().containsKey(authToken)) { throw new DataAccessException("Error: unauthorized"); }
        if (joinRequest.gameID() == null) { throw new DataAccessException("Error: bad request"); }
        GameData game=gameDAO.getGame(joinRequest.gameID());
        if (game == null) {
            throw new DataAccessException("Error: bad request");
        }
        String color = joinRequest.playerColor();
        if (Objects.equals(color, "WHITE")) {
            if (game.whiteUserName() != null) { throw new DataAccessException("Error: already taken"); }
            AuthData authData=authDAO.getAuthData().get(authToken);
            GameData newGame=new GameData(game.gameID(), authData.username(), game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGames(newGame);
        }
        else if (Objects.equals(color, "BLACK")) {
            if (game.blackUsername() != null) { throw new DataAccessException("Error: already taken"); }
            AuthData authData=authDAO.getAuthData().get(authToken);
            GameData newGame=new GameData(game.gameID(), game.whiteUserName(), authData.username(), game.gameName(), game.game());
            gameDAO.updateGames(newGame);
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }
}
