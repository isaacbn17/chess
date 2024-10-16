package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.UserData;
import model.GameData;
import model.AuthData;
import service.Delete;
import service.Register;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private Register s = new Register();
    ArrayList<UserData> users = new ArrayList<>();
    HashMap<String, GameData> games = new HashMap<>();
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
        var user = new Gson();
        var newUser = user.fromJson(
                """
                { "username":"", "password":"", "email":"" }
                """, UserData.class);
        var x = s.registerUser(newUser);

        return user.toJson(user);

    }

    private Object deleteEverything(Request req, Response res) {
        Delete delete = new Delete();
        delete.clearUsers(users);
        delete.clearGames(games);
        delete.clearAuthTokens(authTokens);
        res.status(204);
        return "";
    }
}
