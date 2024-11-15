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
            case WHITE -> whitePawn(board, myPosition);
            case BLACK -> blackPawn(board, myPosition);
        };
    }
    public ArrayList<ChessMove> captureBlack(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
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
        ArrayList<ChessMove> moves=new ArrayList<>(captureBlack(board, myPosition));
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
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
    public ArrayList<ChessMove> captureWhite(ChessBoard board, ChessPosition myPosition) {
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
            ChessPosition captureRight=new ChessPosition(row+1, col+1);
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
        ArrayList<ChessMove> moves = new ArrayList<>(captureWhite(board, myPosition));
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

    private void singleMove(ChessBoard board, ChessPosition myPosition, int x, int y, ArrayList<ChessMove> move) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if ((0 < row+x && row+x < 9) && (0 < col+y && col+y <9)) {
            ChessPosition newPosition = new ChessPosition(row+x, col+y);
            if (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != color) {
                move.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }
    private void severalMoves(ChessBoard board, ChessPosition myPosition, int x, int y, ArrayList<ChessMove> moves) {
        int row = myPosition.getRow();
        int col =myPosition.getColumn();
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
            else {
                break;
            }
        }


    }
    private ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        singleMove(board, myPosition, -1, -1, moves);
        singleMove(board, myPosition, -1, 0, moves);
        singleMove(board, myPosition, -1, 1, moves);
        singleMove(board, myPosition, 0, -1, moves);
        singleMove(board, myPosition, 0, 1, moves);
        singleMove(board, myPosition, 1, -1, moves);
        singleMove(board, myPosition, 1, 0, moves);
        singleMove(board, myPosition, 1, 1, moves);
        return moves;
    }
    private ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        singleMove(board, myPosition, 1, 2, moves);
        singleMove(board, myPosition, 1, -2, moves);
        singleMove(board, myPosition, 2, 1, moves);
        singleMove(board, myPosition, 2, -1, moves);
        singleMove(board, myPosition, -1, 2, moves);
        singleMove(board, myPosition, -1, -2, moves);
        singleMove(board, myPosition, -2, 1, moves);
        singleMove(board, myPosition, -2, -1, moves);
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
        severalMoves(board, myPosition, -1, 0, moves);
        severalMoves(board, myPosition, 1, 0, moves);
        severalMoves(board, myPosition, 0, -1, moves);
        severalMoves(board, myPosition, 0, 1, moves);
        return moves;
    }
    private ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        severalMoves(board, myPosition, -1, -1, moves);
        severalMoves(board, myPosition, -1, 1, moves);
        severalMoves(board, myPosition, 1, -1, moves);
        severalMoves(board, myPosition, 1, 1, moves);
        return moves;
    }
}
