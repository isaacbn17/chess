package client;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.DeleteService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        clearDatabase();
    }

    private static void clearDatabase() throws DataAccessException {
        UserDAO userDAO = new SQLUserDAO();
        GameDAO gameDAO = new SQLGameDAO();
        AuthDAO authDAO = new SQLAuthDAO();
        DeleteService deleteService = new DeleteService(userDAO, gameDAO, authDAO);
        deleteService.clearUsers();
        deleteService.clearGames();
        deleteService.clearAuthTokens();
    }

    @AfterAll
    static void stopServer() throws DataAccessException {
        server.stop();
        clearDatabase();
    }


    @Test
    public void register() throws Exception {
        RegisterResult authData = facade.createUser(new UserData("player", "password", "hello@gmail.com"));
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    public void registerFails() throws Exception {
        assertThrows(Exception.class, () -> facade.createUser(new UserData("", "", "")));
    }
    @Test
    public void login() throws Exception {
        RegisterResult authData = facade.loginUser(new LoginRequest("player", "password"));
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    public void loginFails() {
        assertThrows(Exception.class, () -> facade.loginUser(new LoginRequest("hi", "hello")));
    }
    @Test
    public void logout() throws Exception {
        RegisterResult authData = facade.createUser(new UserData("player1", "pw", "hello"));
        String authToken = authData.authToken();
        facade.logoutUser(authToken);
        assertThrows(Exception.class, () -> facade.createGame(new GameName("hello"), authToken).toString());
    }
    @Test
    public void logoutFails() {
        assertThrows(Exception.class, () -> facade.logoutUser("Not an authToken"));
    }
    @Test
    public void createGame() throws Exception {
        RegisterResult authData = facade.createUser(new UserData("player3", "password", "hello@gmail.com"));
        GameID gameID = facade.createGame(new GameName("Game10"), authData.authToken());
        assertNotNull(gameID);
    }
    @Test
    public void createGameFails() {
        assertThrows(Exception.class, () -> facade.createGame(new GameName("hello"), "Not an authToken"));
    }
    @Test
    public void listGames() throws Exception {
        RegisterResult authData = facade.createUser(new UserData("player4", "pw", "mail.com"));
        facade.createGame(new GameName("Game10"), authData.authToken());
        ArrayList<GameSimplified> expected = facade.listGames(authData.authToken());
        assertFalse(expected.isEmpty());
    }
    @Test
    public void listGamesFails() {
        assertThrows(Exception.class, () -> facade.listGames("Not an authToken"));
    }
    @Test
    public void joinGame() throws Exception {
        RegisterResult authData = facade.createUser(new UserData("player5", "pw", "mail"));
        facade.createGame(new GameName("Game10"), authData.authToken());
        facade.joinGame(new JoinRequest("white", 1), authData.authToken());
        ArrayList<GameSimplified> games = facade.listGames(authData.authToken());
        String whiteUsername = games.getFirst().whiteUsername();
        assertNotNull(whiteUsername);
    }
    @Test
    public void joinGames() {
        assertThrows(Exception.class,
                () -> facade.joinGame(new JoinRequest("WHITE", 2), "hello"));
    }
}
