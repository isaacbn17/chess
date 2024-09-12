package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    @Override
    public String toString() {
        return "ChessPiece{" +
                "piece=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        switch (type) {
            case BISHOP:
                possibleMoves = bishopMoves(board, myPosition);
                break;
            case ROOK:
                possibleMoves = rookMoves(board, myPosition);
        }
        return possibleMoves;
    }

    private ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(Right(board, myPosition));
        moves.addAll(Left(board, myPosition));
        moves.addAll(Up(board, myPosition));
        moves.addAll(Down(board, myPosition));

        return moves;
    }

    private Collection<? extends ChessMove> Down(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (row>1) {
            row--;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessMove move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
        }
        return moves;
    }

    private Collection<? extends ChessMove> Up(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (row<8) {
            row++;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessMove move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
        }
        return moves;
    }

    private Collection<? extends ChessMove> Left(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (col>1) {
            col--;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessMove move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
        }
        return moves;
    }

    private Collection<? extends ChessMove> Right(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (col<8) {
            col++;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessMove move = new ChessMove(myPosition, newPosition, null);
            moves.add(move);
        }
        return moves;
    }

    private ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        moves.addAll(upRight(row, col, myPosition, board));
        moves.addAll(upLeft(row, col, myPosition, board));
        moves.addAll(downRight(row, col, myPosition, board));
        moves.addAll(downLeft(row, col, myPosition, board));

        return moves;
    }

    private Collection<? extends ChessMove> downLeft(int row, int col, ChessPosition myPosition, ChessBoard board) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int r = row;
        int c = col;
        while (r>1 && c>1) {
            r--;
            c--;
            ChessPosition newPosition = new ChessPosition(r,c);
            if (board.getPiece(newPosition)==null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            else if (board.getPiece(newPosition).color != color) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
                break;
            }
            else {
                break;
            }
        }
        return moves;
    }
    private Collection<? extends ChessMove> downRight(int row, int col, ChessPosition myPosition, ChessBoard board) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int r = row;
        int c = col;
        while (r>1 && c<8) {
            r--;
            c++;
            ChessPosition newPosition = new ChessPosition(r,c);
            if (board.getPiece(newPosition)==null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            else if (board.getPiece(newPosition).color != color) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
                break;
            }
            else {
                break;
            }
        }
        return moves;
    }
    private Collection<? extends ChessMove> upLeft(int row, int col, ChessPosition myPosition, ChessBoard board) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int r = row;
        int c = col;
        while (r<8 && c>1) {
            r++;
            c--;
            ChessPosition newPosition = new ChessPosition(r,c);
//            ChessMove move = new ChessMove(myPosition, newPosition, null);
//            moves.add(move);
            if (board.getPiece(newPosition)==null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            else if (board.getPiece(newPosition).color != color) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
                break;
            }
            else {
                break;
            }
        }
        return moves;
    }
    private ArrayList<ChessMove> upRight(int row, int col, ChessPosition myPosition, ChessBoard board) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int r = row;
        int c = col;
        while (r<8 && c<8) {
            r++;
            c++;
            ChessPosition newPosition = new ChessPosition(r,c);
            if (board.getPiece(newPosition)==null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            else if (board.getPiece(newPosition).color != color) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
                break;
            }
            else {
                break;
            }
        }
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int i = row-1; i != row+1; i++) {
            for (int j = col-1; j != col+1; j++) {
                ChessPosition potentialPosition = new ChessPosition(i,j);
                if (board.getPiece(potentialPosition) == null) {
                    //
                }
            }
        }
        /**
         * 1. Get the row and column number
         * 2. Find moves
         * 3. Is there a piece there of the opposite color?
         * 4. Is the move on the board?
         */
        return new ArrayList<ChessMove>();
    }
}
