package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import service.Delete;
import service.UserService;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Server {
    private MemoryUserDAO userDAO = new MemoryUserDAO();
    private MemoryGameDAO gameDAO = new MemoryGameDAO();
    private MemoryAuthDAO authDAO = new MemoryAuthDAO();

    private UserService userService = new UserService(userDAO, authDAO);
    private Delete delete = new Delete(userDAO, gameDAO, authDAO);

    ArrayList<UserData> users = new ArrayList<>();
    HashMap<String, GameData> games = new HashMap<>();
    ArrayList<AuthData> authTokens = new ArrayList<>();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> createUser(req, res));
        Spark.delete("/db", this::deleteEverything);
        Spark.exception(DataAccessException.class, this::badRequest);
        // This line initializes the server and can be removed once you have a functioning endpoint
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
    public void badRequest(DataAccessException ex, Request req, Response res) {
        String message = ex.getMessage();
        if (Objects.equals(message, "Error: bad request")) {
            res.status(400);
            res.body(new Gson().toJson(new ErrorMessage(message)));
        }
        else if (Objects.equals(message, "Error: already taken")) {
            res.status(403);
            res.body(new Gson().toJson(new ErrorMessage(message)));

        }
        else {
            res.status(500);
            res.body(new Gson().toJson(new ErrorMessage(message)));
        }
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String createUser(Request req, Response res) throws DataAccessException {
        UserData newUser = new Gson().fromJson(req.body(), UserData.class);

        RegisterRequest registeredUser = userService.registerUser(newUser);
        res.status(200);
        return new Gson().toJson(registeredUser);
    }

    private Object deleteEverything(Request req, Response res) {
        delete.clearUsers();
        delete.clearGames();
        delete.clearAuthTokens();
        res.status(200);
        return "";
    }
}
