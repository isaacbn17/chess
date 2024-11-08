package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    UserService userService = new UserService(userDAO, authDAO);
    GameService gameService = new GameService(gameDAO, authDAO);

    @Test
    public void joinGameBadRequest() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(new UserData("a", "b", "c.com"));
        GameData game = gameService.createGame(registerResult.authToken(), "game1");
        JoinRequest joinRequest = new JoinRequest("WHITE", game.gameID());
        assertThrows(DataAccessException.class, () -> gameService.joinGame("hello", joinRequest));
    }
    @Test
    public void joinGame() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(new UserData("a", "b", "c.com"));
        GameData game = gameService.createGame(registerResult.authToken(), "game1");
        JoinRequest joinRequest = new JoinRequest("WHITE", game.gameID());
        game = gameService.joinGame(registerResult.authToken(), joinRequest);
        assertEquals("a", game.whiteUsername());
    }
    @Test
    public void createGameBadRequest() {
        assertThrows(DataAccessException.class, () -> gameService.createGame("hi", "game2"));
    }
    @Test
    public void createGame() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(new UserData("a", "b", "c.com"));
        GameData game = gameService.createGame(registerResult.authToken(), "game1");
        int gameID = game.gameID();
        ChessGame chessGame = game.game();
        GameData expectedGame = new GameData(gameID, null, null, "game1", chessGame);
        assertEquals(expectedGame, game);
    }
    @Test
    public void listGames() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(new UserData("a", "b", "c.com"));
        GameData game1 = gameService.createGame(registerResult.authToken(), "game1");
        ArrayList<GameSimplified> games = gameService.listGames(registerResult.authToken());
        ListGameResult returnVal = new ListGameResult(games);
        String actual = new Gson().toJson(returnVal);
        String expected = "{\"games\":[{\"gameID\":" + game1.gameID() + ",\"gameName\":\"game1\"}]}";
        assertEquals(expected, actual);
    }
    @Test
    public void listGamesBadRequest() {
        assertThrows(DataAccessException.class, () -> gameService.listGames("My friend hello"));
    }
    @Test
    public void logoutUser() throws DataAccessException {
        RegisterResult registerResult = userService.registerUser(new UserData("a", "b", "c.com"));

        userService.logoutUser(registerResult.authToken());
        assertNull(authDAO.getAuthData(registerResult.authToken()));
    }
    @Test
    public void logoutUserBadRequest() {
        assertThrows(DataAccessException.class, () -> userService.logoutUser("Not a token"));
    }
    @Test
    public void loginUser() throws DataAccessException {
        RegisterResult registered = userService.registerUser(new UserData("a", "b", "c.com"));
        userService.logoutUser(registered.authToken());

        LoginRequest loginRequest = new LoginRequest("a", "b");
        var actual = userService.loginUser(loginRequest);
        Assertions.assertEquals(loginRequest.username(), actual.username());
    }
    @Test
    public void loginUserBadRequest() {
        assertThrows(DataAccessException.class, () -> userService.loginUser(new LoginRequest("a", "f")));
    }
    @Test
    public void registerUser() throws DataAccessException {
        var expected = new UserData("a", "b", "c.com");
        userDAO.addUser(expected);
        var actual=userDAO.getUser("a");
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void registerUserBadRequest() {
        var user = new UserData("a", "b", "");
        assertThrows(DataAccessException.class, () -> userService.registerUser(user));
    }
    @Test void clear() throws DataAccessException {
        gameDAO.addGame("Party");
        gameDAO.clear();
        assertEquals(0, gameDAO.getGames().size());
    }
}
