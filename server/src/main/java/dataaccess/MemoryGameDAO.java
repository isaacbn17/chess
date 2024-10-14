package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {

  @Override
  public void clear(ArrayList<GameData> games) {
    games.clear();
  }
}
