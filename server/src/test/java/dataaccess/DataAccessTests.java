package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import passoff.server.DatabaseTests;
import service.GameService;
import service.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static dataaccess.DatabaseManager.getConnection;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {
    UserDAO userDAO = new SQLUserDAO();
    AuthDAO authDAO = new SQLAuthDAO();
    GameDAO gameDAO = new SQLGameDAO();
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
       userDAO.addUser(new UserData("a", "b", "c"));
       gameDAO.addGame("Gaaaame");
       authDAO.addAuthData("a");
       userDAO.clear();
       gameDAO.clear();
       authDAO.clear();
       assertEquals(0, countUserRows());
       assertEquals(0, countGameRows());
       assertEquals(0, countAuthRows());

    }
    @Test
    public void addAuthData() throws DataAccessException, SQLException {
        authDAO.addAuthData("a");
        assertEquals(1, countAuthRows());
        authDAO.clear();
    }
    @Test
    public void addAuthDataFailure() {
        assertThrows(DataAccessException.class, () -> authDAO.addAuthData(null));
    }
    @Test
    public void getAuthData() throws DataAccessException {
        AuthData authData = authDAO.addAuthData("a");
        AuthData authData1 = authDAO.getAuthData(authData.authToken());
        assertEquals("a", authData1.username());
        authDAO.clear();
    }
    @Test
    public void getAuthDataFailure() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> authDAO.getAuthData(null));
    }
    @Test
    public void removeAuthToken() throws DataAccessException, SQLException {
        AuthData authData = authDAO.addAuthData("a");
        authDAO.removeAuthToken(authData.authToken());
        assertEquals(0, countAuthRows());
    }
    @Test
    public void removeAuthTokenFailure() throws DataAccessException {

    }
    @Test void clearGame() throws DataAccessException, SQLException {
        gameDAO.addGame("Gaaaame");
        gameDAO.clear();
        assertEquals(0, countGameRows());
    }
    @Test void addGame() throws DataAccessException, SQLException {
        gameDAO.addGame("Gaaaame");
        assertEquals(1, countGameRows());
    }
    @Test void addGameFailure() throws DataAccessException {

    }
    @Test void getGame() throws DataAccessException {
        GameData gameData = gameDAO.addGame("Game");
        GameData gameData1 = gameDAO.getGame(gameData.gameID());
        assertEquals("Game", gameData1.gameName());
    }
    @Test void getGameFailure() throws DataAccessException {

    }
    @Test void getGames() throws DataAccessException {
        HashMap<Integer, GameData> expectedGames = new HashMap<>();
        GameData gameData = gameDAO.addGame("Game");
        expectedGames.put(gameData.gameID(), gameData);
        GameData gameData2 = gameDAO.addGame("Game2");
        expectedGames.put(gameData2.gameID(), gameData2);
        HashMap<Integer, GameData> games = gameDAO.getGames();
        assertEquals(expectedGames, games);
    }
    @Test void getGamesFailure() throws DataAccessException {

    }
    @Test void updateGames() throws DataAccessException {

    }
    @Test void updateGamesFailure() throws DataAccessException {

    }
    @Test void clearUser() throws DataAccessException, SQLException {
        userDAO.addUser(new UserData("a", "b", "c"));
        userDAO.clear();
        assertEquals(0, countUserRows());
    }
    @Test void getUser() throws DataAccessException {

    }
    @Test void getUserFailure() throws DataAccessException {

    }
    @Test void addUser() throws DataAccessException {

    }
    @Test void addUserFailure() throws DataAccessException {

    }


}
