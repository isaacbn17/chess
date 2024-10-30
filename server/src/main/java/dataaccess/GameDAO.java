package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public interface GameDAO {
  public void clear() throws DataAccessException;
  public GameData addGame(String gameName) throws DataAccessException;
  public HashMap<Integer, GameData> getGames() throws DataAccessException;
  public GameData getGame(Integer gameID) throws DataAccessException;
  public GameData updateGames(int gameID, String color, String username) throws DataAccessException;
}
