package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> games = new HashMap<>();
    Integer gameID = 1000;


    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public GameData addGame(String gameName) {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(++gameID, null, null, gameName, chessGame);
        games.put(gameID, gameData);
        return gameData;
    }

    @Override
    public HashMap<Integer, GameData> getGames() {
        return games;
    }

    @Override
    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }

    @Override
    public GameData updateGames(int gameID, String color, String username) {
        if (color.equalsIgnoreCase("WHITE")) {
            GameData game = games.get(gameID);
            GameData newGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            games.put(gameID, newGame);
            return games.get(gameID);
        }
        else {
            GameData game = games.get(gameID);
            GameData newGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            games.put(gameID, newGame);
            return games.get(gameID);
        }
    }

}
