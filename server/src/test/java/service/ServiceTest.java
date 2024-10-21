package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
//    @BeforeAll
//    @BeforeEach
//    @AfterAll
//    @AfterEach
//    @Disabled
//    @ParameterizedTest
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    UserService userService = new UserService(userDAO, authDAO);
    GameService gameService = new GameService(gameDAO, authDAO);
    @Test
    void examples() {
        assertEquals(2, 1 + 1);
        assertNotEquals(true, false);
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException();
        });
    }

    @Test
    public void joinGameBadRequest() throws DataAccessException {
        RegisterRequest registerResult = userService.registerUser(new UserData("a", "b", "c.com"));
        GameData game = gameService.createGame(registerResult.authToken(), "game1");
        JoinRequest joinRequest = new JoinRequest("WHITE", game.gameID());
        assertThrows(DataAccessException.class, () -> gameService.joinGame("hello", joinRequest));
    }
    @Test
    public void joinGame() throws DataAccessException {
        RegisterRequest registerResult = userService.registerUser(new UserData("a", "b", "c.com"));
        GameData game = gameService.createGame(registerResult.authToken(), "game1");
        JoinRequest joinRequest = new JoinRequest("WHITE", game.gameID());
        game = gameService.joinGame(registerResult.authToken(), joinRequest);
        assertEquals("a", game.whiteUsername());
    }

    @Test
    public void createGameBadRequest() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> gameService.createGame("hi", "game2"));
    }
    @Test
    public void createGame() throws DataAccessException {
        RegisterRequest registerResult = userService.registerUser(new UserData("a", "b", "c.com"));
        GameData game = gameService.createGame(registerResult.authToken(), "game1");
        int gameID = game.gameID();
        ChessGame chessGame = game.game();
        GameData expectedGame = new GameData(gameID, null, null, "game1", chessGame);
        assertEquals(expectedGame, game);
    }

    @Test
    public void logoutUser() throws DataAccessException {

        RegisterRequest registerResult = userService.registerUser(new UserData("a", "b", "c.com"));

        userService.logoutUser(registerResult.authToken());
        assertFalse(authDAO.getAuthData().containsKey("a"));

    }

    @Test
    public void loginUser() throws DataAccessException {
        RegisterRequest registered = userService.registerUser(new UserData("a", "b", "c.com"));
        userService.logoutUser(registered.authToken());

        LoginRequest loginRequest = new LoginRequest("a", "b");
        var actual = userService.loginUser(loginRequest);
        Assertions.assertEquals(loginRequest.username(), actual.username());
    }

    @Test
    public void loginUserBadRequest() throws DataAccessException {
//        RegisterRequest expected = userService.registerUser(new UserData("a", "b", "c.com"));
        assertThrows(DataAccessException.class, () -> userService.loginUser(new LoginRequest("a", "f")));
    }

    @Test
    public void registerUser() {
        var dataAccess = new MemoryUserDAO();
        var expected = new UserData("a", "b", "c.com");
        dataAccess.addUser(expected);
        var actual=dataAccess.getUser("a");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void registerUserBadRequest() throws Exception {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var userService = new UserService(userDAO, authDAO);
        var user = new UserData("a", "b", "");
        assertThrows(DataAccessException.class, () -> userService.registerUser(user));
    }

}
