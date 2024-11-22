package server.websocket;

//import javax.websocket.Session;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        Connection connection = new Connection(username, session);
        connections.put(username, connection);
    }
    public void remove(String username) {
        connections.remove(username);
    }
    public void broadcastNotification(String excludeUser, NotificationMessage message, Session session) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection c : connections.values()) {
            if (c.session.isOpen() && c.session == session) {
                if (! c.username.equals(excludeUser)) {
                    String jsonMessage = new Gson().toJson(message);
                    c.send(jsonMessage);
                }
            }
            else if (! c.session.isOpen()){
                removeList.add(c);
            }
        }
        for (Connection c : removeList) {
            connections.remove(c.username);
        }
    }
    public void broadcastGame(LoadGameMessage gameMessage, Session session) throws IOException {
        for (Connection c : connections.values()) {
            if (c.session.isOpen() && c.session == session) {
                String jsonMessage = new Gson().toJson(gameMessage);
                c.send(jsonMessage);
            }
        }
    }
    public void broadcastGameSelf(String username, LoadGameMessage gameMessage) throws IOException {
        Connection c = connections.get(username);
        c.send(new Gson().toJson(gameMessage));
    }

    public void broadcastError(String username, ErrorMessage errorMessage) throws IOException {
        Connection c = connections.get(username);
        c.send(new Gson().toJson(errorMessage));
    }
}
