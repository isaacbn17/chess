package client;

import java.util.*;

import chess.ChessGame.TeamColor;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.*;
import server.ServerFacade;

import static client.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static client.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class ChessClient {

    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private String authToken = "";
    private TeamColor playerColor;
    private int playerGameID;
    private final HashMap<Integer, Integer> gameIDtoNumber = new HashMap<>();
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;


    public ChessClient(String serverURL, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverURL);
        this.serverUrl = serverURL;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) throws Exception {
        String[] tokens = input.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (state == State.SIGNEDOUT) {
            return switch (command) {
                case "quit" -> "quit";
                case "register" -> registerUser(params);
                case "login" -> loginUser(params);
                default -> help();
            };
        }
        else if (state == State.SIGNEDIN) {
            return switch (command) {
                case "quit" -> "quit";
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logoutUser();
                default -> help();
            };
        }
        else {
            return switch (command) {
                case "quit" -> "quit";
                case "redraw" -> drawChessBoard();
                case "move" -> makeMove(params);
                case "leave" -> leaveGame();
                case "resign" -> forfeitGame();
                case "highlight" -> highlightLegalMoves(params);
                default -> help();
            };
        }
    }

    private static ChessPosition formatPosition(String position) {
        int row = 0;
        int col = 0;
        switch (position.charAt(0)) {
            case ('a') -> col = 1;
            case ('b') -> col = 2;
            case ('c') -> col = 3;
            case ('d') -> col = 4;
            case ('e') -> col = 5;
            case ('f') -> col = 6;
            case ('g') -> col = 7;
            case ('h') -> col = 8;
        }
        switch (position.charAt(1)) {
            case ('1') -> row = 1;
            case ('2') -> row = 2;
            case ('3') -> row = 3;
            case ('4') -> row = 4;
            case ('5') -> row = 5;
            case ('6') -> row = 6;
            case ('7') -> row = 7;
            case ('8') -> row = 8;
        }
        return new ChessPosition(row, col);
    }

    private String highlightLegalMoves(String... params) throws Exception {
        if (params.length == 1) {
            String position = params[0];
            if (! position.matches("^[a-g][1-8]$")) {
                return "Error: Position must be [a-g][1-8]";
            }
            ChessPosition piecePosition = formatPosition(position);
            ws.highlightLegalMoves(authToken, playerGameID, playerColor, piecePosition);
            return "";
        }
        else {
            throw new ResponseException("Error: Expected: <POSITION>");
        }
    }
    private String drawChessBoard() throws ResponseException {
        ws.drawBoard(authToken, playerGameID, playerColor);
        return "";
    }

    private String makeMove(String[] params) throws ResponseException {
        if (params.length >= 2) {
            String start = params[0];
            String end = params[1];
            if (! start.matches("^[a-h][1-8]$") && ! end.matches("^[a-h][1-8]$")) {
                return "Error: Positions must be [a-h][1-8]";
            }
            ChessPiece.PieceType promotionPiece = null;
            if (params.length == 3) {
                promotionPiece = formatPromotionPiece(params[2]);
            }
            ChessPosition startPosition = formatPosition(start);
            ChessPosition endPosition = formatPosition(end);
            ws.makeChessMove(authToken, playerGameID, playerColor, new ChessMove(startPosition, endPosition, promotionPiece));
            return "";
        }
        else {
            throw new ResponseException("Error: Expected <START_POSITION> <END_POSITION> <PROMOTION_PIECE>");
        }
    }
    private ChessPiece.PieceType formatPromotionPiece(String piece) throws ResponseException {
        switch (piece) {
            case "queen" -> {
                return ChessPiece.PieceType.QUEEN;
            }
            case "bishop" -> {
                return ChessPiece.PieceType.BISHOP;
            }
            case "knight" -> {
                return ChessPiece.PieceType.KNIGHT;
            }
            case "rook" -> {
                return ChessPiece.PieceType.ROOK;
            }
            default -> throw new ResponseException("Error: Promotion pieces are queen, bishop, knight, and rook.");
        }
    }

    private String forfeitGame() throws ResponseException {
        System.out.print(SET_TEXT_COLOR_BLUE + "Are you sure you want to resign? (y/n)");
        System.out.print(SET_TEXT_COLOR_WHITE + "\n" +  ">>> ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (Objects.equals(line, "y")) {
            ws.forfeitGame(authToken, playerGameID, playerColor);
//            state = State.SIGNEDIN;
        }
        else {
            System.out.print(SET_TEXT_COLOR_BLUE + "You did not resign from the game");
        }
        return "";
    }
    private String leaveGame() throws ResponseException {
        ws.leaveGame(authToken, playerGameID, playerColor);
        state = State.SIGNEDIN;
        return "";
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
//            gameIDtoNumber.put(gameID, gameIDtoNumber.size()+1);
            return "You've created a game.";
        }
        throw new ResponseException("Error: Expected: <GAME NAME>");
    }
    private String listGames() throws Exception {
        ArrayList<GameSimplified> games = server.listGames(authToken);
        StringBuilder gamesString = new StringBuilder();
        int i = 1;
        for (GameSimplified game : games) {
            gamesString.append(i).append(")\n")
                    .append("Game Name: ")
                    .append(game.gameName())
                    .append("\nWhite Username: ")
                    .append(game.whiteUsername())
                    .append("\nBlack Username: ")
                    .append(game.blackUsername())
                    .append("\n\n");
            gameIDtoNumber.put(game.gameID(), i);
            i++;
        }
        return gamesString.toString();
    }
    private String joinGame(String... params) throws Exception {
        if (params.length == 2) {
            int gameID;
            try {
                int gameNumber = Integer.parseInt(params[0]);
                gameID = gameIDtoNumber.get(gameNumber);
            } catch (Exception ex) {
                return "Error: incorrect parameter";
            }
            JoinRequest joinRequest = new JoinRequest(params[1], gameID);
            server.joinGame(joinRequest, authToken);
            playerColor = Objects.equals(joinRequest.playerColor(), "white") ? TeamColor.WHITE : TeamColor.BLACK;
            playerGameID = gameID;

            ws = new WebSocketFacade(serverUrl, notificationHandler);
            if (Objects.equals(joinRequest.playerColor(), "black")) {
                ws.joinGame(authToken, gameID, TeamColor.BLACK);
            }
            else {
                ws.joinGame(authToken, gameID, TeamColor.WHITE);
            }
            state = State.PLAYING;
            return "Joined successfully.";
        }
        throw new ResponseException("Error: Expected <ID> [WHITE|BLACK]");
    }
    private String observeGame(String... params) throws Exception {
        if (params.length == 1) {
            int gameID;
            try {
                gameID = gameIDtoNumber.get(Integer.parseInt(params[0]));
            } catch (Exception ex) {
                return "Error: incorrect parameter";
            }
            playerGameID = gameID;
            playerColor = TeamColor.WHITE;
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.joinGame(authToken, gameID, null);

            state = State.PLAYING;
            return "Observing game " + params[0];
        }
        throw new ResponseException("Error: Expected <ID>");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    -register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    -login <USERNAME> <PASSWORD> - to play chess
                    -quit - stop playing chess
                    -help - list possible commands
                     """;
        }
        else if (state == State.SIGNEDIN) {
            return """
                    -create <GAME NAME> - create a game
                    -list - list games
                    -join <ID> [WHITE|BLACK] - join a game
                    -observe <ID> - observe a game
                    -logout - when you are done
                    -quit - stop playing chess
                    -help - list possible commands
                    """;
        }
        else {
            return """
                   -help - list possible commands
                   -redraw - draw the chess board again
                   -move <START POSITION> <END POSITION> <PROMOTION PIECE> - input your move
                   -leave - leave the game
                   -resign - forfeit the game
                   -highlight <START POSITION> - choose a piece and highlight its legal moves
                    """;
        }
    }
}
