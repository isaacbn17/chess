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
                var id = 0;
                if (gameSet.next()) {
                    id = gameSet.getInt(1);
                    return new GameData(id, null, null, gameName, game);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return null;
    }

    @Override
    public HashMap<Integer, GameData> getGames() throws DataAccessException {
        HashMap<Integer, GameData> games = new HashMap<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(
                    "SELECT * FROM game")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        GameData gameData = getGameData(rs);
                        games.put(gameData.gameID(), gameData);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return games;
    }

    private GameData getGameData(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String whiteUser = rs.getString("whiteUser");
        String blackUser = rs.getString("blackUser");
        String gameName = rs.getString("gameName");
        String game = rs.getString("game");
        ChessGame chessGame = new Gson().fromJson(game, ChessGame.class);
        return new GameData(id, whiteUser, blackUser, gameName, chessGame);
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("SELECT * FROM game WHERE id=?")) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return getGameData(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: bad request");
        }
        return null;
    }

    @Override
    public GameData addPlayer(int gameID, String color, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            if (color.equals("WHITE")) {
                try (var ps = conn.prepareStatement("UPDATE game SET whiteUser=? WHERE id=?")) {
                    ps.setString(1, username);
                    ps.setInt(2, gameID);
                    ps.executeUpdate();
                    return getGame(gameID);
                }
            }
            else {
                try (var ps = conn.prepareStatement("UPDATE game SET blackUser=? WHERE id=?")) {
                    ps.setString(1, username);
                    ps.setInt(2, gameID);
                    ps.executeUpdate();
                    return getGame(gameID);
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("UPDATE game SET game=? WHERE id=?")) {
                ps.setString(1, new Gson().toJson(game));
                ps.setInt(2, gameID);
                ps.executeUpdate();
            }
        }
        catch (Exception ex) {
            throw new DataAccessException("Error: bad request");
        }
    }
}
