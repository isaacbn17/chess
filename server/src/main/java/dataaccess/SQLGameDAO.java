package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.ResultSet;
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
    public HashMap<Integer, GameData> getGames() throws DataAccessException {
        HashMap<Integer, GameData> games = new HashMap<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(
                    "SELECT ALL FROM game")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        addGame(games, rs);
                    }
                }
            }

        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return games;
    }

    private void addGame(HashMap<Integer, GameData> games, ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String whiteUser = rs.getString("whiteUser");
        String blackUser = rs.getString("blackUser");
        String gameName = rs.getString("gameName");
        String game = rs.getString("game");
        ChessGame chessGame = new Gson().fromJson(game, ChessGame.class);
        GameData gameData = new GameData(id, whiteUser, blackUser, gameName, chessGame);
        games.put(id, gameData);
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData updateGames(int gameID, String color, String username) throws DataAccessException {
        return null;
    }
}
