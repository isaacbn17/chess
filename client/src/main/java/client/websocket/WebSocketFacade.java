package client.websocket;


//import org.glassfish.grizzly.http.server.Session;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {
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
                    notificationHandler.notify(message);
                }
            });
        }
        catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void joinGame(String authToken, Integer gameID, ChessGame.TeamColor color) throws Exception {
        try {
            UserGameCommand userGameCommand = new UserGameCommand
                    (UserGameCommand.CommandType.CONNECT, authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void makeChessMove(String authToken, int gameID, ChessGame.TeamColor color, ChessMove move) {
        try {
            MoveCommand moveCommand = new MoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, color, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(moveCommand));
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
    public void highlightLegalMoves(ChessPosition piecePosition) {

    }
}
