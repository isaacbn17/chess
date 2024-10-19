package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> games = new HashMap<>();
    private ArrayList<GameData> gameList = new ArrayList<>();
    Integer gameID = 1000;


    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public GameData addGame(String gameName) {
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(gameID++, null, null, gameName, chessGame);
        games.put(gameID, gameData);
        gameList.add(gameData);
        return gameData;
    }

    @Override
    public ArrayList<GameData> getGames() {
        return gameList;
    }

}
