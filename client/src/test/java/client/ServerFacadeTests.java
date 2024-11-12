package client;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.DeleteService;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static String authToken = "";

    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
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
    static void stopServer() {
        server.stop();
    }


    @Test
    public void register() throws Exception {
        RegisterResult authData = facade.createUser(new UserData("player1", "password", "hello@gmail.com"));
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    public void registerFails() throws Exception {
        assertThrows(Exception.class, () -> facade.createUser(new UserData("", "", "")));
    }
    @Test
    public void login() throws Exception {
        RegisterResult authData = facade.loginUser(new LoginRequest("player1", "password"));
        authToken = authData.authToken();
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    public void loginFails() {
        assertThrows(Exception.class, () -> facade.loginUser(new LoginRequest("hi", "hello")));
    }
    @Test
    public void logout() throws Exception {
        facade.logoutUser(authToken);
    }
    @Test
    public void logoutFails() throws Exception {
        assertThrows(Exception.class, () -> facade.logoutUser("Not an authToken"));
    }
//    @Test
//    public void createGame() throws Exception {
//        RegisterResult authData = facade.createUser(new UserData("player1", "password", "hello@gmail.com"));
//        assertTrue(authData.authToken().length() > 10);
//    }
//    @Test
//    public void createGameFails() throws Exception {
//        assertThrows(Exception.class, () -> facade.createUser(new UserData("", "", "")));
//    }

}
