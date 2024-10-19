package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.LoginRequest;
import model.RegisterRequest;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    @BeforeAll
    static void addTest() {
    }

    @BeforeEach
    void addTest2() {
    }

    @AfterAll
    static void addTest3() {
    }

    @AfterEach
    void addTest4() {
    }
    // @Disabled
    // @ParameterizedTest

    @Test
    void examples() {
        assertEquals(2, 1 + 1);
        assertNotEquals(true, false);
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException();
        });
    }

    @Test
    public void logoutUser() throws DataAccessException {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var userService = new UserService(userDAO, authDAO);

        RegisterRequest registerResult = userService.registerUser(new UserData("a", "b", "c.com"));

        userService.logoutUser(registerResult.authToken());
        assertFalse(authDAO.getAuthData().containsKey("a"));

    }

    @Test
    public void loginUser() throws DataAccessException {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var userService = new UserService(userDAO, authDAO);
        RegisterRequest registered = userService.registerUser(new UserData("a", "b", "c.com"));
        userService.logoutUser(registered.authToken());

        LoginRequest loginRequest = new LoginRequest("a", "b");
        var actual = userService.loginUser(loginRequest);
        Assertions.assertEquals(loginRequest.username(), actual.username());
    }

    @Test
    public void loginUserBadRequest() throws DataAccessException {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var userService = new UserService(userDAO, authDAO);

        RegisterRequest expected = userService.registerUser(new UserData("a", "b", "c.com"));
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
