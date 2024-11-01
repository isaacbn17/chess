package dataaccess;

import chess.ChessGame;
import model.*;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {
    UserDAO userDAO = new SQLUserDAO();
    static AuthDAO authDAO = new SQLAuthDAO();
    static GameDAO gameDAO = new SQLGameDAO();
    Connection connection = DatabaseManager.getConnection();

    public DataAccessTests() throws DataAccessException {
    }

    private int countUserRows() throws SQLException {
        int rows = 0;
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT count(*) FROM user")) {
                if (resultSet.next()) {
                    rows = resultSet.getInt(1);
                }
            }
        }
        return rows;
    }
    private int countGameRows() throws SQLException {
        int rows = 0;
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT count(*) FROM game")) {
                if (resultSet.next()) {
                    rows = resultSet.getInt(1);
                }
            }
        }
        return rows;
    }
    private int countAuthRows() throws SQLException {
        int rows = 0;
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery("SELECT count(*) FROM auth")) {
                if (resultSet.next()) {
                    rows = resultSet.getInt(1);
                }
            }
        }
        return rows;
    }

    @Test void clearAuth() throws DataAccessException, SQLException {
       authDAO.addAuthData("b");
       authDAO.clear();
       assertEquals(0, countAuthRows());
    }
    @Test
    public void addAuthData() throws DataAccessException, SQLException {
        authDAO.addAuthData("d");
        assertEquals(1, countAuthRows());
        authDAO.clear();
    }
    @Test
    public void addAuthDataFailure() {
        assertThrows(DataAccessException.class, () -> authDAO.addAuthData(null));
    }
    @Test
    public void getAuthData() throws DataAccessException {
        AuthData authData = authDAO.addAuthData("e");
        AuthData authData1 = authDAO.getAuthData(authData.authToken());
        assertEquals("e", authData1.username());
        authDAO.clear();
    }
    @Test
    public void getAuthDataFailure() throws DataAccessException {
        authDAO.addAuthData("Mikey");
        assertNull(authDAO.getAuthData(""));
        authDAO.clear();
    }
    @Test
    public void removeAuthToken() throws DataAccessException, SQLException {
        AuthData authData = authDAO.addAuthData("f");
        authDAO.removeAuthToken(authData.authToken());
        assertEquals(0, countAuthRows());
    }
    @Test
    public void removeAuthTokenFailure() throws DataAccessException, SQLException {
        authDAO.addAuthData("g");
        authDAO.removeAuthToken("hmm");
        assertEquals(1, countAuthRows());
        authDAO.clear();
    }
    @Test void clearGame() throws DataAccessException, SQLException {
        gameDAO.addGame("Gaaaame");
        gameDAO.clear();
        assertEquals(0, countGameRows());
    }
    @Test void addGame() throws DataAccessException, SQLException {
        gameDAO.addGame("Gaaaame");
        assertEquals(1, countGameRows());
        gameDAO.clear();
    }
    @Test void addGameFailure() {
        assertThrows(DataAccessException.class, () -> gameDAO.addGame(null));
    }
    @Test void getGame() throws DataAccessException {
        GameData gameData = gameDAO.addGame("Game");
        GameData gameData1 = gameDAO.getGame(gameData.gameID());
        assertEquals("Game", gameData1.gameName());
        gameDAO.clear();
    }
    @Test void getGameFailure() throws DataAccessException {
        assertNull(gameDAO.getGame(320));
    }
    @Test void getGames() throws DataAccessException {
        GameData gameData = gameDAO.addGame("Game");
        GameData gameData2 = gameDAO.addGame("Game2");
        HashMap<Integer, GameData> games = gameDAO.getGames();
        GameData game1 = games.get(gameData.gameID());
        GameData game2 = games.get(gameData2.gameID());
        assertEquals(gameData.gameName(), game1.gameName());
        assertEquals(gameData2.gameName(), game2.gameName());
        gameDAO.clear();
    }
    @Test void getGamesFailure() throws DataAccessException {
        gameDAO.addGame("Game");
        HashMap<Integer, GameData> games = gameDAO.getGames();
        GameData game = new GameData(100, null, null, "winner", new ChessGame());
        assertNotEquals(games, game);
        gameDAO.clear();
    }
    @Test void updateGames() throws DataAccessException {
        GameData gameData = gameDAO.addGame("Gaaaame");
        GameData gameData1 = gameDAO.updateGames(gameData.gameID(), "WHITE", "Deborah");
        assertEquals(gameData1.whiteUsername(), "Deborah");
        gameDAO.clear();
    }
    @Test void updateGamesFailure() throws DataAccessException {
        GameData gameData = gameDAO.addGame("Gaaaame");
        gameDAO.updateGames(300, "WHITE", "Deborah");
        assertNotEquals(gameData.whiteUsername(), "Deborah");
        gameDAO.clear();
    }
    @Test void clearUser() throws DataAccessException, SQLException {
        userDAO.addUser(new UserData("i", "b", "c"));
        userDAO.clear();
        assertEquals(0, countUserRows());
    }
    @Test void getUser() throws DataAccessException {
        UserData userDataExpected = userDAO.addUser(new UserData("j", "b", "c"));
        UserData userData = userDAO.getUser("j");
        assertEquals(userDataExpected, userData);
        userDAO.clear();
    }
    @Test void getUserFailure() throws DataAccessException {
        assertNull(userDAO.getUser(null));
    }
    @Test void addUser() throws DataAccessException, SQLException {
        userDAO.addUser(new UserData("k", "b", "c"));
        assertEquals(1, countUserRows());
        userDAO.clear();
    }
    @Test void addUserFailure() throws DataAccessException {
        assertThrows(DataAccessException.class,
                () -> userDAO.addUser(new UserData("l", null, "c")));
    }

}