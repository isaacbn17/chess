package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import service.Delete;
import service.GameService;
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
    private GameService gameService = new GameService(gameDAO, authDAO);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> createUser(req, res));
        Spark.delete("/db", this::deleteEverything);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void exceptionHandler(Exception ex, Request req, Response res) {
        String message = ex.getMessage();
        if (Objects.equals(message, "Error: bad request")) {
            res.status(400);
            res.body(new Gson().toJson(new ErrorMessage(message)));
        }
        else if (Objects.equals(message, "Error: already taken")) {
            res.status(403);
            res.body(new Gson().toJson(new ErrorMessage(message)));

        }
        else if (Objects.equals(message, "Error: unauthorized")) {
            res.status(401);
            res.body(new Gson().toJson(new ErrorMessage(message)));
        }
        else {
            res.status(500);
            res.body(new Gson().toJson(new ErrorMessage(message)));
        }
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        JoinRequest joinRequest = new Gson().fromJson(req.body(), JoinRequest.class);
        gameService.joinGame(authToken, joinRequest);
        res.status(200);
        return "";
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        GameName gameNameObj = new Gson().fromJson(req.body(), GameName.class);
        GameData game = gameService.createGame(authToken, gameNameObj.gameName());
        res.status(200);

        Map<String, Integer> response = new HashMap<>();
        response.put("gameID", game.gameID());

        return new Gson().toJson(response);
    }
    private Object listGames(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        ArrayList<GameSimplified> games = gameService.listGames(authToken);
        res.status(200);

        Map<String, ArrayList<GameSimplified>> response = new HashMap<>();
        response.put("games", games);

        return new Gson().toJson(response);
    }
    private Object logoutUser(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        userService.logoutUser(authToken);
        res.status(200);
        return "";
    }
    private String loginUser(Request req, Response res) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);

        RegisterRequest authenticatedUser = userService.loginUser(loginRequest);
        res.status(200);
        return new Gson().toJson(authenticatedUser);
    }

    private String createUser(Request req, Response res) throws DataAccessException {
        try {
            UserData newUser = new Gson().fromJson(req.body(), UserData.class);

            RegisterRequest registeredUser = userService.registerUser(newUser);
            res.status(200);
            return new Gson().toJson(registeredUser);
        } catch (JsonSyntaxException ex) {
            exceptionHandler(ex, req, res);
            return "";
        }
    }

    private String deleteEverything(Request req, Response res) {
        delete.clearUsers();
        delete.clearGames();
        delete.clearAuthTokens();
        res.status(200);
        return "";
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
