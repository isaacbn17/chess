package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import exception.ResponseException;
import model.*;
import server.ServerFacade;

public class ChessClient {

    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken = "";
    private final HashMap<Integer, Integer> gameNumberAndIDs = new HashMap<>();

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
            case "logout" -> logoutUser();
            case "create" -> createGame(params);
            case "list" -> listGames();
            case "join" -> joinGame(params);
            case "observe" -> observeGame(params);
            default -> help();
        };
    }

    public String registerUser(String... params) throws Exception {
        if (params.length == 3) {
            UserData userData = new UserData(params[0], params[1], params[2]);
            RegisterResult registerResult = server.createUser(userData);
            authToken = registerResult.authToken();
            state = State.SIGNEDIN;
            return String.format("You're signed in as: %s", registerResult.username());
        }
        throw new ResponseException("Error: Expected <USERNAME> <PASSWORD> <EMAIL>");
    }
    private String loginUser(String... params) throws Exception {
        if (params.length == 2) {
            LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
            RegisterResult loginResult = server.loginUser(loginRequest);
            authToken = loginResult.authToken();
            state = State.SIGNEDIN;
            return String.format("You're signed in as: %s", loginResult.username());
        }
        throw new ResponseException("Error: Expected <USERNAME> <PASSWORD>");
    }
    private String logoutUser() throws Exception {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in.");
        }
        server.logoutUser(authToken);
        state = State.SIGNEDOUT;
        return "You've logged out.";
    }
    private String createGame(String... params) throws Exception {
        if (params.length == 1) {
            GameName gameName = new GameName(params[0]);
            server.createGame(gameName, authToken);
            return "You've created a game.";
        }
        throw new ResponseException("Error: Expected: <GAME NAME>");
    }
    private String listGames() throws Exception {
        ArrayList<GameSimplified> games = server.listGames(authToken);
        StringBuilder gamesString =new StringBuilder();
        int i = 1;
        for (GameSimplified game : games) {
            gamesString.append(i).append(")")
                    .append("\nGame Name: ")
                    .append(game.gameName())
                    .append("\nWhite Username: ")
                    .append(game.whiteUsername())
                    .append("\nBlack Username: ")
                    .append(game.blackUsername())
                    .append("\n\n");
            gameNumberAndIDs.put(i, game.gameID());
            i++;
        }
        return gamesString.toString();
    }
    private String joinGame(String... params) throws Exception {
        if (params.length == 2) {
            int gameID;
            try {
                gameID = gameNumberAndIDs.get(Integer.parseInt(params[0]));
            } catch (Exception ex) {
                return "Error: incorrect parameter";
            }
            JoinRequest joinRequest = new JoinRequest(params[1], gameID);
            server.joinGame(joinRequest, authToken);
            if (Objects.equals(joinRequest.playerColor(), "black")) {
                PrintBoard.drawBlackPerspective();
            }
            else {
                PrintBoard.drawWhitePerspective();
            }
            return "Joined successfully.";
        }
        throw new ResponseException("Error: Expected <ID> [WHITE|BLACK]");
    }
    private String observeGame(String... params) throws Exception {
        if (params.length == 1) {
            PrintBoard.drawWhitePerspective();
            return "Observing game " + params[0];
        }
        throw new ResponseException("Error: Expected <ID>");
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
