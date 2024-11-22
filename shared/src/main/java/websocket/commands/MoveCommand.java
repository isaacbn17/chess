package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MoveCommand extends UserGameCommand {

    ChessMove move;
    public MoveCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor color, ChessMove move) {
        super(commandType, authToken, gameID, color);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
