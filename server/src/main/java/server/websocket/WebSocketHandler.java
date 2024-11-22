package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.DisplayCommand;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public WebSocketHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
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
                case MAKE_MOVE -> makeMove(session, username, new Gson().fromJson(message, MoveCommand.class));
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);
                case DISPLAY -> display(session, username, new Gson().fromJson(message, DisplayCommand.class));
            }
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }


    private void display(Session session, String username, DisplayCommand command) throws DataAccessException, IOException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        LoadGameMessage gameMessage = new LoadGameMessage(
                ServerMessage.ServerMessageType.LOAD_GAME, gameData.game(), command.getColor(), command.getPosition());
        connections.broadcastGameSelf(username, command.getGameID(), gameMessage);
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        try {
            int gameID = command.getGameID();
            connections.add(username, gameID, session);
            if (! Objects.equals(username, authDAO.getAuthData(command.getAuthToken()).username())) {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: incorrect authorization token");
                connections.broadcastError(username, gameID, errorMessage);
                return;
            }
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: incorrect game ID");
                connections.broadcastError(username, gameID, errorMessage);
                return;
            }

            NotificationMessage notificationMessage = getConnectMessage(username, command);
            connections.broadcastNotification(username, gameID, notificationMessage, session);

            LoadGameMessage gameMessage = new LoadGameMessage(
                    ServerMessage.ServerMessageType.LOAD_GAME, gameData.game(), command.getColor(), null);
            connections.broadcastGameSelf(username, gameID, gameMessage);

        } catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.broadcastError(username, command.getGameID(), errorMessage);
        }

    }
    private static NotificationMessage getConnectMessage(String username, UserGameCommand command) {
        String message;
        if (command.getColor() == null) {
            message = String.format("%s is observing the game.", username);
        }
        else if (command.getColor() == ChessGame.TeamColor.WHITE) {
            message = String.format("%s joined the game as white.", username);
        }
        else {
            message = String.format("%s joined the game as black.", username);
        }
        return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    }

    private void resign(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        String winningColor = command.getColor() == ChessGame.TeamColor.WHITE ? "black" : "white";
        connections.remove(command.getGameID(), username);

        GameData gameData = gameDAO.getGame(command.getGameID());
        gameData.game().endGame();
        gameDAO.updateGame(gameData.gameID(), gameData.game());

        NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s resigned. %s wins!", username, winningColor));
        connections.broadcastNotification(username, command.getGameID(), message, session);
    }
    private void leaveGame(Session session, String username, UserGameCommand command) throws IOException {
        connections.remove(command.getGameID(), username);
        NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s left the game", username));
        connections.broadcastNotification(username, command.getGameID(), message, session);
    }
    private void makeMove(Session session, String username, MoveCommand command) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        GameData gameData = gameDAO.getGame(gameID);
        // Handles errors such as invalid move and not your turn
        try {
            gameData.game().makeMove(command.getMove());
            gameDAO.updateGame(gameData.gameID(), gameData.game());
            LoadGameMessage gameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game(), command.getColor(), null);
            connections.broadcastGame(gameID, gameMessage, session);

            NotificationMessage notificationMessage = getMoveMessage(username, command);
            connections.broadcastNotification(username, gameID, notificationMessage, session);

        } catch (InvalidMoveException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.broadcastError(username, command.getGameID(), errorMessage);
        }

    }
    private NotificationMessage getMoveMessage(String username, MoveCommand command) {
        ChessMove move = command.getMove();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        String message = String.format("%s moved from %s to %s.", username, start.toString(), end.toString());
        return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    }

    private String getUsername(Session session, String authToken) throws DataAccessException {
        try {
            AuthData authData = authDAO.getAuthData(authToken);
            if (authData== null) {
                ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: incorrect authorization token");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
                return null;
            }
            return authData.username();
        } catch (Exception ex) {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
