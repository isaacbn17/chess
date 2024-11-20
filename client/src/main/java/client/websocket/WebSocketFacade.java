package client.websocket;

import org.glassfish.grizzly.http.server.Session;
import websocket.messages.ServerMessage;

import java.net.URI;

public class WebSocketFacade {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
