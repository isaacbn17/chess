package server.websocket;

//import javax.websocket.Session;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public Session session;
    public String username;

    public Connection(String username, Session session) {
        this.session = session;
        this.username = username;
    }

    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
