package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void updateGames(GameData newGame) {
        Integer gameID = newGame.gameID();
        games.remove(gameID);
        games.put(gameID, newGame);
    }

}
