package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
  private HashMap<String, GameData> games = new HashMap<>();

  @Override
  public void clear() {
    games.clear();
  }
}
