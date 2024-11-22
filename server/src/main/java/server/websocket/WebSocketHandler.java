package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private MoveCommand moveCommand;

    public WebSocketHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
                moveCommand = new Gson().fromJson(message, MoveCommand.class);
            }


            String username = getUsername(command.getAuthToken());
//            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, command);
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);
            }
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        connections.add(username, session);
        NotificationMessage notificationMessage = getNotificationMessage(username, command);
        connections.broadcast(username, notificationMessage);

        GameData gameData = gameDAO.getGame(command.getGameID());
        LoadGameMessage gameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game(), command.getColor());
        connections.selfBroadcast(username, gameMessage);
    }

    private static NotificationMessage getNotificationMessage(String username, UserGameCommand command) {
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

    private void resign(Session session, String username, UserGameCommand command) {
    }

    private void leaveGame(Session session, String username, UserGameCommand command) {
    }

    private void makeMove(Session session, String username, UserGameCommand command) {
    }

    private void saveSession(Integer gameID, Session session) {
//        try {
//
//        } catch (DataAccessException ex) {
//            throw new RuntimeException(ex);
//        }
    }

    private String getUsername(String authToken) throws DataAccessException {
        try {
            AuthData authData = authDAO.getAuthData(authToken);
            return authData.username();
        } catch (DataAccessException ex) {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
