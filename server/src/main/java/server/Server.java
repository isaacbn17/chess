package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.*;
import model.*;
import server.websocket.WebSocketHandler;
import service.DeleteService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.ArrayList;

public class Server {
    private UserDAO userDAO = new MemoryUserDAO();
    private GameDAO gameDAO = new MemoryGameDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();
    private final WebSocketHandler webSocketHandler;

    private final UserService userService;
    private final DeleteService deleteService;
    private final GameService gameService;

    public Server() {
        try {
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
            authDAO = new SQLAuthDAO();
        } catch (Exception ex) { System.out.println(ex); }

        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
        this.deleteService = new DeleteService(userDAO, gameDAO, authDAO);
        webSocketHandler = new WebSocketHandler(gameDAO, authDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::deleteEverything);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(Exception ex, Request req, Response res) {
        String message = ex.getMessage();
        switch (message) {
            case "Error: bad request" -> {
                res.status(400);
                res.body(new Gson().toJson(new ErrorMessage(message)));
            }
            case "Error: unauthorized" -> {
                res.status(401);
                res.body(new Gson().toJson(new ErrorMessage(message)));
            }
            case "Error: already taken" -> {
                res.status(403);
                res.body(new Gson().toJson(new ErrorMessage(message)));
            }
            case null, default -> {
                res.status(500);
                res.body(new Gson().toJson(new ErrorMessage(message)));
            }
        }
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        JoinRequest joinRequest = new Gson().fromJson(req.body(), JoinRequest.class);
        GameData game = gameService.joinGame(authToken, joinRequest);
        res.status(200);
        System.out.println(game.gameID() + " " + game.whiteUsername());
        return "";
    }
    private Object createGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        GameName gameNameObj = new Gson().fromJson(req.body(), GameName.class);
        GameData game = gameService.createGame(authToken, gameNameObj.gameName());
        res.status(200);
        GameID newGame = new GameID(game.gameID());

        return new Gson().toJson(newGame);
    }
    private Object listGames(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        ArrayList<GameSimplified> games = gameService.listGames(authToken);
        res.status(200);
        ListGameResult returnVal = new ListGameResult(games);
        return new Gson().toJson(returnVal);
    }
    private Object logoutUser(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        userService.logoutUser(authToken);
        res.status(200);
        return "";
    }
    private String loginUser(Request req, Response res) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);

        RegisterResult authenticatedUser = userService.loginUser(loginRequest);
        res.status(200);
        return new Gson().toJson(authenticatedUser);
    }
    private String createUser(Request req, Response res) throws DataAccessException {
        try {
            UserData newUser = new Gson().fromJson(req.body(), UserData.class);

            RegisterResult registeredUser = userService.registerUser(newUser);
            res.status(200);
            return new Gson().toJson(registeredUser);
        } catch (JsonSyntaxException ex) {
            exceptionHandler(ex, req, res);
            return "";
        }
    }
    private String deleteEverything(Request req, Response res) throws DataAccessException {
        deleteService.clearUsers();
        deleteService.clearGames();
        deleteService.clearAuthTokens();
        res.status(200);
        return "";
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
