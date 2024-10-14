package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
  public void clear(ArrayList<GameData> games);
}
