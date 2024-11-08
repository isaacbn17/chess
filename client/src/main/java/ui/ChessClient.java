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

    public String eval(String input) throws Exception {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command) {
                case "register" -> registerUser(params);
                default -> help();
            };
        } catch (Exception ex) {
            throw ex;
        }
    }

    public String registerUser(String... params) throws Exception {
        if (params.length == 3) {
            UserData userData = new UserData(params[0], params[1], params[2]);
            RegisterRequest registerResult = server.createUser(userData);
            state = State.SIGNEDIN;
            return String.format("You're signed in as: %s", registerResult.username());
        }
        throw new ResponseException("Expected: <USERNAME> <PASSWORD> <EMAIL>");
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
