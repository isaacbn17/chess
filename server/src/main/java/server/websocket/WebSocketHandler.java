package server.websocket;

import chess.*;
import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.*;
import websocket.messages.*;
import websocket.messages.ServerMessage.ServerMessageType;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            String username = getUsername(session, command.getAuthToken());
            if (username == null) {
                return;
            }

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(username, new Gson().fromJson(message, MoveCommand.class));
                case LEAVE -> leaveGame(username, command);
                case RESIGN -> resign(username, command);
                case DISPLAY -> display(username, new Gson().fromJson(message, DisplayCommand.class));
            }
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }


    private void display(String username, DisplayCommand command) throws DataAccessException, IOException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        LoadGameMessage gameMessage = new LoadGameMessage(
                ServerMessageType.LOAD_GAME, gameData.game(), command.getColor(), command.getPosition());
        connections.broadcastGameSingle(username, command.getGameID(), gameMessage);
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException {
        try {
            int gameID = command.getGameID();
            connections.add(username, gameID, session);
            if (! Objects.equals(username, authDAO.getAuthData(command.getAuthToken()).username())) {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, "Error: incorrect authorization token");
                connections.broadcastError(username, gameID, errorMessage);
                return;
            }
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, "Error: incorrect game ID");
                connections.broadcastError(username, gameID, errorMessage);
                return;
            }

            NotificationMessage notificationMessage = getConnectMessage(username, command);
            connections.broadcastNotification(username, true, gameID, notificationMessage);

            LoadGameMessage gameMessage = new LoadGameMessage(
                    ServerMessageType.LOAD_GAME, gameData.game(), command.getColor(), null);
            connections.broadcastGameSingle(username, gameID, gameMessage);

        } catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, ex.getMessage());
            connections.broadcastError(username, command.getGameID(), errorMessage);
        }

    }
    private static NotificationMessage getConnectMessage(String username, UserGameCommand command) {
        String message;
        if (command.getColor() == null) {
            message = String.format("%s is observing the game", username);
        }
        else if (command.getColor() == TeamColor.WHITE) {
            message = String.format("%s joined the game as white", username);
        }
        else {
            message = String.format("%s joined the game as black", username);
        }
        return new NotificationMessage(ServerMessageType.NOTIFICATION, message);
    }

    private void resign(String username, UserGameCommand command) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        GameData gameData = gameDAO.getGame(gameID);
        if (!Objects.equals(username, gameData.whiteUsername()) && !Objects.equals(username, gameData.blackUsername())) {
            String message = "Error: observers can't resign";
            ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, message);
            connections.broadcastError(username, command.getGameID(), errorMessage);
            return;
        }
        if (gameData.game().gameOver()) {
            String message = "Error: game is already over";
            ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, message);
            connections.broadcastError(username, command.getGameID(), errorMessage);
            return;
        }
        String winningColor = command.getColor() == TeamColor.WHITE ? "black" : "white";
        gameData.game().endGame();
        gameDAO.updateGame(gameData.gameID(), gameData.game());

        String stringMessage = String.format("%s resigned. %s wins!", username, winningColor);
        NotificationMessage message = new NotificationMessage(ServerMessageType.NOTIFICATION, stringMessage);
        connections.broadcastNotification(username, false, command.getGameID(), message);
    }
    private void leaveGame(String username, UserGameCommand command) throws IOException, DataAccessException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        TeamColor color = Objects.equals(username, gameData.whiteUsername()) ? TeamColor.WHITE :
                (Objects.equals(username, gameData.blackUsername()) ? TeamColor.BLACK : null);
        if (color == TeamColor.WHITE) {
            gameDAO.addPlayer(gameData.gameID(), "WHITE", null);
        }
        else if (color == TeamColor.BLACK) {
            gameDAO.addPlayer(gameData.gameID(), "BLACK", null);
        }

        connections.remove(command.getGameID(), username);
        NotificationMessage message = new NotificationMessage(ServerMessageType.NOTIFICATION, String.format("%s left the game", username));
        connections.broadcastNotification(username, true, command.getGameID(), message);
    }
    private void makeMove(String username, MoveCommand command) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameDAO.getGame(gameID).game();
        TeamColor teamTurn = game.getTeamTurn();
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        if (game.gameOver()) {
            String message = "The game has ended";
            ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, message);
            connections.broadcastError(username, command.getGameID(), errorMessage);
            return;
        }
        if (!Objects.equals(username, teamTurn == TeamColor.WHITE ? whiteUsername : blackUsername)) {
            String message = "It's not your turn";
            ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, message);
            connections.broadcastError(username, command.getGameID(), errorMessage);
            return;
        }
        TeamColor playerAttacked = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;

        // Handles errors such as invalid move
        try {
            game.makeMove(command.getMove());
            gameDAO.updateGame(gameID, game);

            LoadGameMessage gameMessage = new LoadGameMessage(ServerMessageType.LOAD_GAME, game, command.getColor(), null);
            if (teamTurn == TeamColor.WHITE) {
                connections.broadcastGame(gameID, gameMessage, blackUsername);
                LoadGameMessage blackGameMessage = new LoadGameMessage(ServerMessageType.LOAD_GAME, game, TeamColor.BLACK, null);
                connections.broadcastGameSingle(blackUsername, gameID, blackGameMessage);
            }
            else {
                connections.broadcastGameSingle(username, gameID, gameMessage);
                LoadGameMessage otherGameMessage = new LoadGameMessage(ServerMessageType.LOAD_GAME, game, TeamColor.WHITE, null);
                connections.broadcastGame(gameID, otherGameMessage, blackUsername);
            }

            NotificationMessage notificationMessage = getMoveMessage(username, command);
            connections.broadcastNotification(username, true, gameID, notificationMessage);

            checkGameStatusAndNotify(playerAttacked, username, game, gameID);

        } catch (InvalidMoveException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, ex.getMessage());
            connections.broadcastError(username, command.getGameID(), errorMessage);
        }

    }

    private void checkGameStatusAndNotify(TeamColor playerAttacked, String username, ChessGame game, int gameID) throws IOException, DataAccessException {
        if (game.isInCheckmate(playerAttacked)) {
            NotificationMessage gameStatusMessage = getGameStatusMessage(playerAttacked, false, true);
            connections.broadcastNotification(username, false, gameID, gameStatusMessage);
            game.endGame();
            gameDAO.updateGame(gameID, game);
        }
        else if (game.isInCheck(playerAttacked)) {
            NotificationMessage gameStatusMessage = getGameStatusMessage(playerAttacked, true, false);
            connections.broadcastNotification(username, false, gameID, gameStatusMessage);
        }
        else if (game.isInStalemate(playerAttacked)) {
            NotificationMessage gameStatusMessage = getGameStatusMessage(playerAttacked, false, false);
            connections.broadcastNotification(username, false, gameID, gameStatusMessage);
        }
    }

    private NotificationMessage getGameStatusMessage(TeamColor playerAttacked, boolean check, boolean checkmate) {
        if (checkmate) {
            String winningColor = playerAttacked == TeamColor.WHITE ? "Black" : "White";
            String message = String.format("%s is in checkmate. %s wins! The game is over.", playerAttacked, winningColor);
            return new NotificationMessage(ServerMessageType.NOTIFICATION, message);
        }
        else if (check) {
            String message = String.format("%s is in check", playerAttacked);
            return new NotificationMessage(ServerMessageType.NOTIFICATION, message);
        }
        else {
            String message = String.format("%s is in stalemate", playerAttacked);
            return new NotificationMessage(ServerMessageType.NOTIFICATION, message);
        }
    }

    private NotificationMessage getMoveMessage(String username, MoveCommand command) {
        ChessMove move = command.getMove();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        String message = String.format("%s moved from %s to %s", username, start.toString(), end.toString());
        return new NotificationMessage(ServerMessageType.NOTIFICATION, message);
    }

    private String getUsername(Session session, String authToken) throws DataAccessException {
        try {
            AuthData authData = authDAO.getAuthData(authToken);
            if (authData== null) {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, "Error: incorrect authorization token");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
                return null;
            }
            return authData.username();
        } catch (Exception ex) {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
