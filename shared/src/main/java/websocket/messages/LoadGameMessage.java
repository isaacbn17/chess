package websocket.messages;

import chess.ChessGame;
import chess.ChessPosition;

public class LoadGameMessage extends ServerMessage {
    ChessGame game;
    ChessGame.TeamColor color;
    ChessPosition highlightPosition;

    public LoadGameMessage(ServerMessageType type, ChessGame game, ChessGame.TeamColor color, ChessPosition highlightPosition) {
        super(type);
        this.game = game;
        this.color = color;
        this.highlightPosition = highlightPosition;
    }

    public ChessGame getGame() {
        return game;
    }
    public ChessGame.TeamColor getColor() {
        return color;
    }
    public ChessPosition highlightPosition() {
        return highlightPosition;
    }
}
