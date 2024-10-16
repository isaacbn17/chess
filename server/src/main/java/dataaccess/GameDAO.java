package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
  public void clear(HashMap<String, GameData> games);
}
