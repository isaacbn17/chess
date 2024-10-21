package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public interface GameDAO {
  public void clear();
  public GameData addGame(String gameName);
  public HashMap<Integer, GameData> getGames();
  public GameData getGame(Integer gameID);
  public GameData updateGames(int gameID, String color, String username);
}
