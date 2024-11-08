package ui;

import java.util.Arrays;

import exception.ResponseException;
import model.AuthData;
import model.LoginRequest;
import model.RegisterRequest;
import model.UserData;
import server.ServerFacade;

public class ChessClient {

    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverURL) {
        server = new ServerFacade(serverURL);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command) {
                case "register" -> registerUser(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String registerUser(String... params) throws ResponseException {
        if (params.length == 3) {
            UserData userData = new UserData(params[0], params[1], params[2]);
            RegisterRequest registerResult = new RegisterRequest("hello", "token");
            registerResult = server.createUser(userData);
            return String.format("You signed in: %s", registerResult.toString());
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    -register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    -login <USERNAME> <PASSWORD> - to play chess
                    -quit - playing chess
                    -help - with possible commands
                     """;
        }
        else {
            return """
                    -create <NAME> - create a game
                    -list - list games
                    -join <ID> [WHITE|BLACK] - join a game
                    -observe <ID> - observe a game
                    -logout - when you are done
                    -quit - stop playing chess
                    -help - with possible commands
                    """;
        }
    }
}
