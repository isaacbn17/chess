package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.HashMap;


public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, GameData> games = new HashMap<>();
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
        GameData game = games.get(gameID);
        GameData newGame;
        if (color.equalsIgnoreCase("WHITE")) {
            newGame=new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        }
        else {
            newGame=new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        games.put(gameID, newGame);
        return games.get(gameID);
    }

}
