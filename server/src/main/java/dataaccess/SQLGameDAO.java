package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE game")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public GameData addGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO game (whiteUser, blackUser, gameName, game) VALUES (?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, gameName);
                ChessGame game = new ChessGame();
                preparedStatement.setString(4, new Gson().toJson(game));

                preparedStatement.executeUpdate();

                var gameSet = preparedStatement.getGeneratedKeys();
                var ID = 0;
                if (gameSet.next()) {
                    ID = gameSet.getInt(1);
                }
                return new GameData(ID, null, null, gameName, game);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
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
