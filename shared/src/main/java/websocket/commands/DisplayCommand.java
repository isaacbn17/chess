package websocket.commands;

import chess.ChessGame;
import chess.ChessPosition;

public class DisplayCommand extends UserGameCommand {

    ChessPosition position;
    public DisplayCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor color, ChessPosition position) {
        super(commandType, authToken, gameID, color);
        this.position = position;
    }

    public ChessPosition getPosition() {
        return position;
    }
}
