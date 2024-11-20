package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import javax.websocket.Session;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            String username = getUsername(command.getAuthToken());
            saveSession(command.getGameID(), session);

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

    private void resign(Session session, String username, UserGameCommand command) {
    }

    private void leaveGame(Session session, String username, UserGameCommand command) {
    }

    private void makeMove(Session session, String username, UserGameCommand command) {
    }

    private void connect(Session session, String username, UserGameCommand command) {
        
    }

    private void saveSession(Integer gameID, Session session) {
    }

    private String getUsername(String authToken) {
        return "username";
    }
}
