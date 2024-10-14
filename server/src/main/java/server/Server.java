package server;

import dataaccess.DataAccessException;
import model.UserData;
import service.Delete;
import spark.*;

import java.util.ArrayList;

public class Server {
    ArrayList<UserData> users = new ArrayList<>();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");


        // Register your endpoints and handle exceptions here.
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

    private Object deleteEverything(Request req, Response res) throws DataAccessException {
        Delete delete = new Delete();
        delete.clearApp(users);
        res.status(204);
        return "";
    }
}
