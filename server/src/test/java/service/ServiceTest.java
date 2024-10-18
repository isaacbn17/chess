package service;

import dataaccess.MemoryUserDAO;
import model.UserData;
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
    void simple() {
        assertTrue(true);
    }

    @Test
    void examples() {
        assertEquals(2, 1 + 1);
        assertNotEquals(true, false);
        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException();
        });
    }

    @Test
    public void registerUser() throws Exception {
        var user=new UserData("a", "b", "a@a.com");
    }

    @Test
    public void registerUserBadRequest() {
        var dataAccess = new MemoryUserDAO();
        var expected = new UserData("a", "p", "A@a.com");
        dataAccess.addUser(expected);
        var expectedUsername=expected.username();
        var actual=dataAccess.getUser("a");
        Assertions.assertEquals(expectedUsername, actual);
    }

}
