package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {
    UserDAO userDAO = new SQLUserDAO();
    AuthDAO authDAO = new SQLAuthDAO();
    GameDAO gameDAO = new SQLGameDAO();
    UserService userService = new UserService(userDAO, authDAO);
    GameService gameService = new GameService(gameDAO, authDAO);

    public DataAccessTests() throws DataAccessException {
    }

    @Test void clearAuth() throws DataAccessException {
       
    }
    @Test
    public void addAuthData() throws DataAccessException {

    }
    @Test
    public void addAuthDataFailure() throws DataAccessException {

    }
    @Test
    public void getAuthData() throws DataAccessException {

    }
    @Test
    public void getAuthDataFailure() throws DataAccessException {

    }
    @Test
    public void removeAuthToken() throws DataAccessException {

    }
    @Test
    public void removeAuthTokenFailure() throws DataAccessException {

    }
    @Test void clearGame() throws DataAccessException {

    }
    @Test void addGame() throws DataAccessException {

    }
    @Test void addGameFailure() throws DataAccessException {

    }
    @Test void getGame() throws DataAccessException {

    }
    @Test void getGameFailure() throws DataAccessException {

    }
    @Test void getGames() throws DataAccessException {

    }
    @Test void getGamesFailure() throws DataAccessException {

    }
    @Test void updateGames() throws DataAccessException {

    }
    @Test void updateGamesFailure() throws DataAccessException {

    }
    @Test void clear() throws DataAccessException {

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
