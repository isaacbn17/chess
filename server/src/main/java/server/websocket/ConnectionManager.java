package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class ConnectionManager {
//    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();


    public void add(String username, int gameID, Session session) {
        Connection connection = new Connection(username, session);
        connections.computeIfAbsent(gameID, key -> new ArrayList<>()).add(connection);
    }
    public void remove(int gameID, String username) {
        ArrayList<Connection> connectionList = connections.get(gameID);
        if (connectionList != null) {
            connectionList.removeIf(c -> Objects.equals(c.getUsername(), username));
        }
    }
    public void broadcastNotification(String excludeUser, int gameID, NotificationMessage message, Session session) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        ArrayList<Connection> connectionList = connections.get(gameID);
        for (Connection c : connectionList) {
            if (c.session.isOpen()) {
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
            connectionList.remove(c);
        }
    }
    public void broadcastGame(int gameID, LoadGameMessage gameMessage, Session session) throws IOException {
        ArrayList<Connection> connectionList = connections.get(gameID);
        for (Connection c : connectionList) {
            if (c.session.isOpen() && c.session == session) {
                String jsonMessage = new Gson().toJson(gameMessage);
                c.send(jsonMessage);
            }
        }
    }
    public void broadcastGameSelf(String username, int gameID, LoadGameMessage gameMessage) throws IOException {
        ArrayList<Connection> connectionList = connections.get(gameID);
        for (Connection c : connectionList) {
            if (Objects.equals(c.getUsername(), username)) {
                c.send(new Gson().toJson(gameMessage));
            }
        }
    }

    public void broadcastError(String username, int gameID, ErrorMessage errorMessage) throws IOException {
        ArrayList<Connection> connectionList = connections.get(gameID);
        for (Connection c : connectionList) {
            if (Objects.equals(c.getUsername(), username)) {
                c.send(new Gson().toJson(errorMessage));
            }
        }
    }

}
