package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

  @Override
  public void clear(HashMap<String, GameData> games) {
    games.clear();
  }
}
