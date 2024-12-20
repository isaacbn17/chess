package client;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;

import static client.EscapeSequences.*;

public class PrintBoard {
    private static final String[] LETTERS={" a", " b", " c", " d", " e", " f", " g", " h"};

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        System.out.println("White perspective:\n");
        drawWhitePerspective(game, new ChessPosition(2, 2));
        System.out.println(SET_TEXT_COLOR_WHITE + "Black perspective:\n");
        drawBlackPerspective(game, new ChessPosition(8, 2));
    }
    private static HashSet<ChessPosition> getValidEndPositions(ChessGame game, ChessPosition position) {
        if (position != null) {
            HashSet<ChessPosition> positions = new HashSet<>();
            Collection<ChessMove> validMoves = game.validMoves(position);
            for (ChessMove move : validMoves) {
                positions.add(move.getEndPosition());
            }
            return positions;
        }
        return null;
    }

    public static void drawBlackPerspective(ChessGame game, ChessPosition position) {
        ChessBoard board = game.getBoard();
        HashSet<ChessPosition> validEndPositions = getValidEndPositions(game, position);

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawTopOrBottomRow(out, false);
        for (int row=1; row <= 8; row++) {
            for (int col=9; col>=0; col--) {
                drawChessRow(out, board, row, col, validEndPositions);
            }
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
        drawTopOrBottomRow(out, false);
        out.print("\n");
    }

    public static void drawWhitePerspective(ChessGame game, ChessPosition position) {
        ChessBoard board = game.getBoard();
        HashSet<ChessPosition> validEndPositions = getValidEndPositions(game, position);

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawTopOrBottomRow(out, true);
        for (int row=8; row >=1; row--) {
            for (int col=0; col<=9; col++) {
                drawChessRow(out, board, row, col, validEndPositions);
            }
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
        drawTopOrBottomRow(out, true);
        out.print("\n");
    }


    private static void drawChessRow(PrintStream out, ChessBoard board, int row, int col, HashSet<ChessPosition> validMoves) {
        if (col == 0 || col == 9) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(" %s\u3000", row);
        }
        boolean isWhiteSquare = (col + row) % 2 == 1;
        if (validMoves != null && validMoves.contains(new ChessPosition(row, col))) {
            out.print(isWhiteSquare ? SET_BG_COLOR_LIGHT_ORANGE : SET_BG_COLOR_ORANGE);
        }
        else {
            out.print(isWhiteSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);
        }

        if (col > 0 && col < 9) {
            ChessPiece piece=board.getPiece(new ChessPosition(row, col));
            if (piece == null) {
                out.print(EMPTY);
            } else {
                drawPiece(out, piece);
            }
        }
    }

    private static void drawPiece(PrintStream out, ChessPiece piece) {
        ChessGame.TeamColor pieceColor=piece.getTeamColor();
        out.print(pieceColor == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_BLUE : SET_TEXT_COLOR_MAGENTA);

        ChessPiece.PieceType type=piece.getPieceType();
        if (type == ChessPiece.PieceType.PAWN) {
            out.print(pieceColor == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN);
        } else if (type == ChessPiece.PieceType.ROOK) {
            out.print(pieceColor == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK);
        } else if (type == ChessPiece.PieceType.KNIGHT) {
            out.print(pieceColor == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT);
        } else if (type == ChessPiece.PieceType.BISHOP) {
            out.print(pieceColor == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP);
        } else if (type == ChessPiece.PieceType.QUEEN) {
            out.print(pieceColor == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN);
        } else {
            out.print(pieceColor == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING);
        }
    }

    private static void drawTopOrBottomRow(PrintStream out, boolean colorWhite) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(EMPTY);
        out.print(SET_TEXT_COLOR_BLACK);
        if (colorWhite) {
            for (int col=0; col < 8; col++) {
                out.print(LETTERS[col] + "\u3000");
            }
        } else {
            for (int col=7; col >= 0; col--) {
                out.print(LETTERS[col] + "\u3000");
            }
        }
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.print("\n");
    }
}
