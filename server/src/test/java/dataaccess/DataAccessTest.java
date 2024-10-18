package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataAccessTest {
    @Test
    public void registerUserNotExists() {
        var dataAccess=new MemoryUserDAO();
        var expected=new UserData("a", "p", "a@a.com");
        dataAccess.addUser(expected);
        var expectedUsername=expected.username();
        var actual=dataAccess.getUser("a");
        Assertions.assertEquals(expectedUsername, actual);
    }
}
