package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ChessPiece {
    public String toString() {
        return "" + type;
    }
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return type == that.type && color == that.color;
    }
    public int hashCode() {
        return 17*Objects.hash(type, color);
    }

    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.color = pieceColor;
    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }
    public PieceType getPieceType() {
        return type;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
          case BISHOP -> bishopMoves(board, myPosition);
          case ROOK -> rookMoves(board, myPosition);
          case QUEEN -> queenMoves(board, myPosition);
          case PAWN -> pawnMoves(board, myPosition);
          case KNIGHT -> knightMoves(board, myPosition);
          case KING -> kingMoves(board, myPosition);
        };
    }

    private ArrayList<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (color) {
            case WHITE -> pawn(board, myPosition, 2, 7, 1);
            case BLACK -> pawn(board, myPosition, 7, 2, -1);
        };
    }
    public ArrayList<ChessMove> capture(ChessBoard board, ChessPosition myPosition, int direction) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (col > 1) {
            ChessPosition captureLeft = new ChessPosition(row + direction, col-1);
            getCaptures(board, myPosition, direction, moves, row, captureLeft);
        }
        if (col < 8) {
            ChessPosition captureRight = new ChessPosition(row + direction, col+1);
            getCaptures(board, myPosition, direction, moves, row, captureRight);
        }
        return moves;
    }

    private void getCaptures(ChessBoard board, ChessPosition myPosition, int direction, ArrayList<ChessMove> moves, int row, ChessPosition capturePosition) {
        if (board.getPiece(capturePosition) != null && board.getPiece(capturePosition).getTeamColor() != color) {
            if (row + direction == 1 || row + direction == 8) {
                moves.addAll(promotionPieces(myPosition, capturePosition));
            } else {
                moves.add(new ChessMove(myPosition, capturePosition, null));
            }
        }
    }

    private ArrayList<ChessMove> pawn(ChessBoard board, ChessPosition myPosition, int start, int end, int direction) {
        ArrayList<ChessMove> moves=new ArrayList<>(capture(board, myPosition, direction));
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition newPosition = new ChessPosition(row + direction, col);
        if (board.getPiece(newPosition) == null) {
            if (row == start) {
                ChessPosition secondPosition = new ChessPosition(row + direction*2, col);
                if (board.getPiece(secondPosition) == null) {
                    moves.add(new ChessMove(myPosition, secondPosition, null));
                }
            }
            else if (row == end) {
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

    private void singleStep(ChessBoard board, ChessPosition myPosition, int x, int y, ArrayList<ChessMove> move) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if ((0 < row+x && row+x < 9) && (0 < col+y && col+y <9)) {
            ChessPosition newPosition = new ChessPosition(row+x, col+y);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                move.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }
    private void severalSteps(ChessBoard board, ChessPosition myPosition, int x, int y, ArrayList<ChessMove> moves) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while ((0 < row+x && row+x < 9) && (0 < col+y && col+y < 9)) {
            row += x;
            col += y;
            ChessPosition newPosition = new ChessPosition(row, col);
            if (board.getPiece(newPosition)==null) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
            }
            else if (board.getPiece(newPosition).color != color) {
                ChessMove move = new ChessMove(myPosition, newPosition, null);
                moves.add(move);
                break;
            }
            else { break; }
        }
    }
    
    private ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (int i=-1; i<=1; i++) {
            for (int j=-1; j<=1; j++) {
                if (i != 0 || j != 0) {
                    singleStep(board, myPosition, i, j, moves);
                }
            }
        }
        return moves;
    }
    private ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (int step : new int[]{-2, -1, 1, 2}) {
            for (int step2 : new int[]{-2, -1, 1, 2}) {
                if (Math.abs(step) != Math.abs(step2)) {
                    singleStep(board, myPosition, step, step2, moves);
                }
            }
        }
        return moves;
    }
    private ArrayList<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(rookMoves(board, myPosition));
        moves.addAll(bishopMoves(board, myPosition));
        return moves;
    }
    private ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        severalSteps(board, myPosition, -1, 0, moves);
        severalSteps(board, myPosition, 1, 0, moves);
        severalSteps(board, myPosition, 0, -1, moves);
        severalSteps(board, myPosition, 0, 1, moves);
        return moves;
    }
    private ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        severalSteps(board, myPosition, -1, -1, moves);
        severalSteps(board, myPosition, -1, 1, moves);
        severalSteps(board, myPosition, 1, -1, moves);
        severalSteps(board, myPosition, 1, 1, moves);
        return moves;
    }
}
