package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;

public interface GameDAO {
  public void clear() throws DataAccessException;
  public GameData addGame(String gameName) throws DataAccessException;
  public HashMap<Integer, GameData> getGames() throws DataAccessException;
  public GameData getGame(Integer gameID) throws DataAccessException;
  public GameData addPlayer(int gameID, String color, String username) throws DataAccessException;

  public void updateGame(int gameID, ChessGame game) throws DataAccessException;
}
