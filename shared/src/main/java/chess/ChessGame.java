package chess;

import java.util.Collection;
import java.util.ArrayList;

public class ChessGame {
    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();
    public ChessGame() {
    }

    public TeamColor getTeamTurn() {
        return teamTurn;
    }
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor color = piece.getTeamColor();
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        for (ChessMove move : potentialMoves) {

            // If the move causes the king to be in check, remove it.
        }

        return potentialMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves.contains(move)) {
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            ChessPiece piece = board.getPiece(start);
            board.addPiece(end, piece);
            board.removePiece(start, piece);
        }
        else {
            throw new InvalidMoveException("Invalid move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        ArrayList<ChessMove> moves = getOpposingTeamMoves(teamColor);
        for (ChessMove move : moves) {
            if (move.getEndPosition() == kingPosition) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<ChessMove> getOpposingTeamMoves(TeamColor teamColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (int i=1; i<9; i++) {
            for (int j=1; j<9; j++) {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    ChessPosition position = new ChessPosition(i, j);
                    moves.addAll(piece.pieceMoves(board, position));
                }
            }
        }
        return moves;
    }
    private ChessPosition findKing(TeamColor teamColor) {
        for (int i=1; i<9; i++) {
            for (int j=1; j<9; j++) {
                if (board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING &&
                        board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    return new ChessPosition(i, j);
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) { throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        ChessPiece king = board.getPiece(kingPosition);
        Collection<ChessMove> kingMoves = king.pieceMoves(board, kingPosition);
        ArrayList<ChessMove> opposingMoves = getOpposingTeamMoves(teamColor);
        for (ChessMove kingMove : kingMoves) {
            if (! opposingMoves.contains(kingMove)) {
                return false;
            }
        }
        return true;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }
    public ChessBoard getBoard() {
        return board;
    }
}
