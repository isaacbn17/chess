package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;

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
                break;
            case QUEEN:
                possibleMoves = queenMoves(board, myPosition);
                break;
            case PAWN:
                possibleMoves = pawnMoves(board, myPosition);
                break;
            case KNIGHT:
                possibleMoves = knightMoves(board, myPosition);
        }
        return possibleMoves;
    }

    private ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessMove forwardLeft = forwardLeft(board, myPosition);
        if (forwardLeft != null) {
            moves.add(forwardLeft);
        }
        ChessMove forwardRight = forwardRight(board, myPosition);
        if (forwardRight != null) {
            moves.add(forwardRight);
        }
        ChessMove leftForward = leftForward(board, myPosition);
        if (leftForward != null) {
            moves.add(leftForward);
        }
        ChessMove rightForward = rightForward(board, myPosition);
        if (rightForward != null) {
            moves.add(rightForward);
        }
        ChessMove leftBackward = leftBackward(board, myPosition);
        if (leftBackward != null) {
            moves.add(leftBackward);
        }
        ChessMove rightBackward = rightBackward(board, myPosition);
        if (rightBackward != null) {
            moves.add(rightBackward);
        }
        ChessMove backwardLeft = backwardLeft(board, myPosition);
        if (backwardLeft != null) {
            moves.add(backwardLeft);
        }
        ChessMove backwardRight = backwardRight(board, myPosition);
        if (backwardRight != null) {
            moves.add(backwardRight);
        }


        return moves;
    }

    private ChessMove backwardRight(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row>2 && col<8) {
            ChessPosition newPosition = new ChessPosition(row-2, col+1);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                return new ChessMove(myPosition, newPosition, null);
            }
        }
        return null;
    }

    private ChessMove backwardLeft(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row>2 && col>1) {
            ChessPosition newPosition = new ChessPosition(row-2, col-1);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                return new ChessMove(myPosition, newPosition, null);
            }
        }
        return null;
    }

    private ChessMove rightBackward(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row>1 && col<7) {
            ChessPosition newPosition = new ChessPosition(row-1, col+2);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                return new ChessMove(myPosition, newPosition, null);
            }
        }
        return null;
    }

    private ChessMove leftBackward(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row>1 && col>2) {
            ChessPosition newPosition = new ChessPosition(row-1, col-2);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                return new ChessMove(myPosition, newPosition, null);
            }
        }
        return null;
    }

    private ChessMove rightForward(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row<8 && col<7) {
            ChessPosition newPosition = new ChessPosition(row+1, col+2);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                return new ChessMove(myPosition, newPosition, null);
            }
        }
        return null;
    }

    private ChessMove leftForward(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row<8 && col>2) {
            ChessPosition newPosition = new ChessPosition(row+1, col-2);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                return new ChessMove(myPosition, newPosition, null);
            }
        }
        return null;
    }

    private ChessMove forwardRight(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row<7 && col<7) {
            ChessPosition newPosition = new ChessPosition(row+2, col+1);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                return new ChessMove(myPosition, newPosition, null);
            }
        }
        return null;
    }

    private ChessMove forwardLeft(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row<7 && col>1) {
            ChessPosition newPosition = new ChessPosition(row+2, col-1);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                return new ChessMove(myPosition, newPosition, null);
            }
        }
        return null;
    }

    private ArrayList<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (color == ChessGame.TeamColor.WHITE) {
            moves.addAll(whitePawn(board, myPosition));
        }
        else {
            moves.addAll(blackPawn(board, myPosition));
        }
        return moves;
    }
    private ArrayList<ChessMove> captureBlack(int row, int col, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (col > 1) {
            ChessPosition captureLeft = new ChessPosition(row-1, col-1);
            if (board.getPiece(captureLeft) != null && board.getPiece(captureLeft).getTeamColor() != color) {
                if (row-1 == 1) {
                    moves.addAll(promotionPieces(myPosition, captureLeft));
                } else {
                    moves.add(new ChessMove(myPosition, captureLeft,null));
                }
            }
        }
        if (col < 8) {
            ChessPosition captureRight=new ChessPosition(row-1, col+1);
            if (board.getPiece(captureRight) != null && board.getPiece(captureRight).getTeamColor() != color) {
                if (row-1 == 1) {
                    moves.addAll(promotionPieces(myPosition, captureRight));
                } else {
                    moves.add(new ChessMove(myPosition, captureRight, null));
                }
            }
        }
        return moves;
    }
    private ArrayList<ChessMove> blackPawn(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ArrayList<ChessMove> moves=new ArrayList<>(captureBlack(row, col, board, myPosition));
        ChessPosition newPosition = new ChessPosition(row-1, col);
        if (board.getPiece(newPosition) == null) {
            if (row==7) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                ChessPosition secondPosition = new ChessPosition(row-2, col);
                if (board.getPiece(secondPosition) == null) {
                    moves.add(new ChessMove(myPosition, secondPosition, null));
                }
            }
            if (row==2) {
                moves.addAll(promotionPieces(myPosition, newPosition));
                return moves;
            }
            moves.add(new ChessMove(myPosition, newPosition, null));
            return moves;
        }
        return moves;
    }
    private ArrayList<ChessMove> captureWhite(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (col > 1) {
            ChessPosition captureLeft = new ChessPosition(row+1, col-1);
            if (board.getPiece(captureLeft) != null && board.getPiece(captureLeft).getTeamColor() != color) {
                if (row+1 == 8) {
                    moves.addAll(promotionPieces(myPosition, captureLeft));
                } else {
                    moves.add(new ChessMove(myPosition, captureLeft,null));
                }
            }
        }
        if (col < 8) {
            ChessPosition captureRight=new ChessPosition(row+1, col + 1);
            if (board.getPiece(captureRight) != null && board.getPiece(captureRight).getTeamColor() != color) {
                if (row+1 == 8) {
                    moves.addAll(promotionPieces(myPosition, captureRight));
                } else {
                    moves.add(new ChessMove(myPosition, captureRight, null));
                }
            }
        }
        return moves;
    }
    private ArrayList<ChessMove> whitePawn(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves=new ArrayList<>(captureWhite(board, myPosition));
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition newPosition = new ChessPosition(row+1, col);
        if (board.getPiece(newPosition) == null) {
            if (row==2) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                ChessPosition secondPosition = new ChessPosition(row+2, col);
                if (board.getPiece(secondPosition) == null) {
                    moves.add(new ChessMove(myPosition, secondPosition, null));
                }
            }
            if (row==7) {
                moves.addAll(promotionPieces(myPosition, newPosition));
                return moves;
            }
            moves.add(new ChessMove(myPosition, newPosition, null));
            return moves;
        }
        return moves;
    }
    private ArrayList<ChessMove> promotionPieces(ChessPosition myPosition, ChessPosition newPosition) {
        ArrayList<ChessMove> pieces = new ArrayList<>();
        pieces.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
        pieces.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
        pieces.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
        pieces.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
        return pieces;
    }

    private ArrayList<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(Right(board, myPosition));
        moves.addAll(Left(board, myPosition));
        moves.addAll(Up(board, myPosition));
        moves.addAll(Down(board, myPosition));
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        moves.addAll(upRight(row, col, myPosition, board));
        moves.addAll(upLeft(row, col, myPosition, board));
        moves.addAll(downRight(row, col, myPosition, board));
        moves.addAll(downLeft(row, col, myPosition, board));

        return moves;
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
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            else if (board.getPiece(newPosition).getTeamColor() != color) {
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
    private Collection<? extends ChessMove> Up(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (row<8) {
            row++;
            ChessPosition newPosition = new ChessPosition(row, col);
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            else if (board.getPiece(newPosition).getTeamColor() != color) {
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
    private Collection<? extends ChessMove> Left(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (col>1) {
            col--;
            ChessPosition newPosition = new ChessPosition(row, col);
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            else if (board.getPiece(newPosition).getTeamColor() != color) {
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
    private Collection<? extends ChessMove> Right(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (col<8) {
            col++;
            ChessPosition newPosition = new ChessPosition(row, col);
            if (board.getPiece(newPosition) == null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            else if (board.getPiece(newPosition).getTeamColor() != color) {
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
