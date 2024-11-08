package ui;

import java.util.Arrays;

import exception.ResponseException;
import model.LoginRequest;
import model.RegisterResult;
import model.UserData;
import server.ServerFacade;

public class ChessClient {

    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverURL) {
        server = new ServerFacade(serverURL);
    }

    public String eval(String input) throws Exception {
        String[] tokens = input.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "quit" -> "quit";
            case "register" -> registerUser(params);
            case "login" -> loginUser(params);
            default -> help();
        };
    }

    private String loginUser(String[] params) throws Exception {
        if (params.length == 2) {
            LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
            RegisterResult loginResult = server.loginUser(loginRequest);
            state = State.SIGNEDIN;
            return String.format("You're signed in as: %s", loginResult.username());
        }
        throw new ResponseException("Error: Expected <USERNAME> <PASSWORD");
    }

    public String registerUser(String... params) throws Exception {
        if (params.length == 3) {
            UserData userData = new UserData(params[0], params[1], params[2]);
            RegisterResult registerResult = server.createUser(userData);
            state = State.SIGNEDIN;
            return String.format("You're signed in as: %s", registerResult.username());
        }
        throw new ResponseException("Error: Expected <USERNAME> <PASSWORD> <EMAIL>");
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
                    -create <GAME NAME> - create a game
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
