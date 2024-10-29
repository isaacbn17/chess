package dataaccess;

import model.GameData;

import java.util.HashMap;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public GameData addGame(String gameName) {
        return null;
    }

    @Override
    public HashMap<Integer, GameData> getGames() {
        return null;
    }

    @Override
    public GameData getGame(Integer gameID) {
        return null;
    }

    @Override
    public GameData updateGames(int gameID, String color, String username) {
        return null;
    }
}
