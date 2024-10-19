package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
  private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() {
        games.clear();
    }
//    @Override
//    public void addGame(GameData gameData) {
//        games.put(gameData.gameID(), gameData);
//    }
}
