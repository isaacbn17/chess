package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class PrintBoard {
    private static final String[] letters = {" a", " b", " c", " d", " e", " f", " g", " h"};
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawWhitePerspective(out);
        out.print("\n");
        drawBlackPerspective(out);
    }

    private static void drawBlackPerspective(PrintStream out) {
        drawTopOrBottomRow(out, false);
        for (int boardRow = 1; boardRow <= 8; boardRow++) {
            drawChessRow(out, boardRow);
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
        drawTopOrBottomRow(out, false);
    }



    private static void drawWhitePerspective(PrintStream out) {
        drawTopOrBottomRow(out, true);
        for (int boardRow = 8; boardRow > 0; boardRow--) {
            drawChessRow(out, boardRow);
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
        drawTopOrBottomRow(out, true);

    }

    private static void drawChessRow(PrintStream out, int boardRow) {
        for (int boardCol = 0; boardCol < 10; boardCol++) {
            if (boardCol == 0 || boardCol == 9) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
                out.printf(" %s\u3000", boardRow);
            }

            boolean isWhiteSquare = (boardCol + boardRow) % 2 == 1;
            out.print(isWhiteSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);

            if (boardCol > 0 && boardCol < 9) {
                if (boardRow == 8) {
                    printBackRow(out, boardCol, SET_TEXT_COLOR_MAGENTA, BLACK_ROOK, 
                            BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING);
                } else if (boardRow == 7) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    out.print(BLACK_PAWN);
                } else if (boardRow == 2) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(WHITE_PAWN);
                } else if (boardRow == 1) {
                    printBackRow(out, boardCol, SET_TEXT_COLOR_BLUE, WHITE_ROOK, 
                            WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING);
                } else {
                    out.print(EMPTY);
                }
            }
        }
    }

    private static void printBackRow(PrintStream out, int boardCol, String textColor, String rook,
                                      String knight, String bishop, String queen, String king) {
        out.print(textColor);
        switch (boardCol) {
            case (1), (8) -> out.print(rook);
            case (2), (7) -> out.print(knight);
            case (3), (6) -> out.print(bishop);
            case (4) -> out.print(queen);
            case (5) -> out.print(king);
        }
    }

    private static void drawTopOrBottomRow(PrintStream out, boolean colorWhite) {

        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(EMPTY);
        out.print(SET_TEXT_COLOR_BLACK);
        if (colorWhite) {
            for (int boardCol = 0; boardCol < 8; boardCol++) {
                out.print(letters[boardCol] + "\u3000");
            }
        }
        else {
            for (int boardCol = 7; boardCol >= 0; boardCol--) {
                out.print(letters[boardCol] + "\u3000");
            }
        }
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.print("\n");
    }


}
