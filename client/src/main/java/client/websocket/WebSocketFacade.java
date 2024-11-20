package client.websocket;


//import org.glassfish.grizzly.http.server.Session;
import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(serverMessage);
                }
            });

        }
        catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void joinGame(String authToken, Integer gameID, ChessGame.TeamColor color) throws Exception {
        try {
            UserGameCommand userGameCommand = new UserGameCommand
                    (UserGameCommand.CommandType.CONNECT, authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        }
        catch (Exception ex) {

        }
    }

//    @Override
//    public void onOpen(Session session, EndpointConfig endpointConfig) {
//    }


}
