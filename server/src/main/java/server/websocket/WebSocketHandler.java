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
                ServerMessageType.LOAD_GAME, gameData.game(), command.getColor(), command.getPosition());
        connections.broadcastGameSelf(username, command.getGameID(), gameMessage);
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
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
            connections.broadcastNotification(username, true, gameID, notificationMessage, session);

            LoadGameMessage gameMessage = new LoadGameMessage(
                    ServerMessageType.LOAD_GAME, gameData.game(), command.getColor(), null);
            connections.broadcastGameSelf(username, gameID, gameMessage);

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

    private void resign(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
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

        NotificationMessage message = new NotificationMessage(ServerMessageType.NOTIFICATION, String.format("%s resigned. %s wins!", username, winningColor));
        connections.broadcastNotification(username, false, command.getGameID(), message, session);
        connections.remove(command.getGameID(), username);
    }
    private void leaveGame(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
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
        connections.broadcastNotification(username, true, command.getGameID(), message, session);
    }
    private void makeMove(Session session, String username, MoveCommand command) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameDAO.getGame(gameID).game();
        TeamColor teamTurn = game.getTeamTurn();

        if (!Objects.equals(username, teamTurn == TeamColor.WHITE ? gameData.whiteUsername() : gameData.blackUsername())) {
            String message = "Error: illegal to make a move for another player";
            ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, message);
            connections.broadcastError(username, command.getGameID(), errorMessage);
            return;
        }
        TeamColor playerAttacked = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;

        // Handles errors such as invalid move and not your turn
        try {
            game.makeMove(command.getMove());
            gameDAO.updateGame(gameID, game);
            LoadGameMessage gameMessage = new LoadGameMessage(ServerMessageType.LOAD_GAME, game, command.getColor(), null);
            connections.broadcastGame(gameID, gameMessage, session);

            NotificationMessage notificationMessage = getMoveMessage(username, command);
            connections.broadcastNotification(username, true, gameID, notificationMessage, session);

            checkGameStatusAndNotify(playerAttacked, username, game, gameID, session);

        } catch (InvalidMoveException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ServerMessageType.ERROR, ex.getMessage());
            connections.broadcastError(username, command.getGameID(), errorMessage);
        }

    }

    private void checkGameStatusAndNotify(TeamColor playerAttacked, String username, ChessGame game, int gameID, Session session) throws IOException {
        if (game.isInCheck(playerAttacked)) {
            NotificationMessage gameStatusMessage = getGameStatusMessage(playerAttacked, true, false);
            connections.broadcastNotification(username, false, gameID, gameStatusMessage, session);
        }
        else if (game.isInCheckmate(playerAttacked)) {
            NotificationMessage gameStatusMessage = getGameStatusMessage(playerAttacked, false, true);
            connections.broadcastNotification(username, false, gameID, gameStatusMessage, session);
        }
        else if (game.isInStalemate(playerAttacked)) {
            NotificationMessage gameStatusMessage = getGameStatusMessage(playerAttacked, false, false);
            connections.broadcastNotification(username, false, gameID, gameStatusMessage, session);
        }
    }

    private NotificationMessage getGameStatusMessage(TeamColor playerAttacked, boolean check, boolean checkmate) {
        if (check) {
            String message = String.format("%s is in check", playerAttacked);
            return new NotificationMessage(ServerMessageType.NOTIFICATION, message);
        }
        else if (checkmate) {
            String message = String.format("%s is in checkmate", playerAttacked);
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
