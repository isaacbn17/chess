package server.websocket;

//import javax.websocket.Session;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
    public void broadcast(String excludeUser, ServerMessage serverMessage) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection c : connections.values()) {
            if (c.session.isOpen()) {
                if (! c.username.equals(excludeUser)) {
                    c.send(serverMessage.toString());
                }
            }
            else {
                removeList.add(c);
            }
        }
        for (Connection c : removeList) {
            connections.remove(c.username);
        }
    }
    public void selfBroadcast(String username, LoadGameMessage gameMessage) throws IOException {
        for (Connection c : connections.values()) {
            if (c.username.equals(username)) {
//                c.send("Sending a message to user");
                String jsonMessage = new Gson().toJson(gameMessage);
                c.send(jsonMessage);
            }
        }
    }
}
