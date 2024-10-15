package server;

import dataaccess.DataAccessException;
import model.UserData;
import model.GameData;
import model.AuthData;
import service.Delete;
import spark.*;

import java.util.ArrayList;

public class Server {
    ArrayList<UserData> users = new ArrayList<>();
    ArrayList<GameData> games = new ArrayList<>();
    ArrayList<AuthData> authTokens = new ArrayList<>();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> createUser(req, res));
        Spark.delete("/db", this::deleteEverything);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private String createUser(Request req, Response res) {
        return """
                { "username":"", "password":"", "email":"" }
                """;
    }


    private Object deleteEverything(Request req, Response res) throws DataAccessException {
        Delete delete = new Delete();
        delete.clearUsers(users);
        delete.clearGames(games);
        delete.clearAuthTokens(authTokens);
        res.status(204);
        return "";
    }
}
