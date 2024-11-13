package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class PrintBoard {
    private static final String[] LETTERS={" a", " b", " c", " d", " e", " f", " g", " h"};

    public static void main(String[] args) {
        System.out.println("White perspective:\n");
        drawWhitePerspective();
        System.out.println("Black perspective:\n");
        drawBlackPerspective();
    }

    public static void drawBlackPerspective() {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawTopOrBottomRow(out, false);
        for (int row=1; row <= 8; row++) {
            drawChessRow(out, row, false);
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
        drawTopOrBottomRow(out, false);
        out.print("\n");
    }

    public static void drawWhitePerspective() {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawTopOrBottomRow(out, true);
        for (int row=8; row > 0; row--) {
            drawChessRow(out, row, true);
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
        drawTopOrBottomRow(out, true);
        out.print("\n");
    }

    private static void drawChessRow(PrintStream out, int row, boolean whitePerspective) {
        for (int col=0; col < 10; col++) {
            if (col == 0 || col == 9) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
                out.printf(" %s\u3000", row);
            }
            boolean isWhiteSquare=whitePerspective ? (col + row) % 2 == 1 : (col + row) % 2 == 0;
            out.print(isWhiteSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);

            if (col > 0 && col < 9) {
                if (row == 8) {
                    printBackRow(out, col, SET_TEXT_COLOR_MAGENTA, BLACK_ROOK,
                            BLACK_KNIGHT, BLACK_BISHOP);
                    printKingsAndQueens(out, col, whitePerspective, BLACK_QUEEN, WHITE_KING, BLACK_KING, WHITE_QUEEN);

                } else if (row == 7) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    out.print(BLACK_PAWN);
                } else if (row == 2) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(WHITE_PAWN);
                } else if (row == 1) {
                    printBackRow(out, col, SET_TEXT_COLOR_BLUE, WHITE_ROOK,
                            WHITE_KNIGHT, WHITE_BISHOP);
                    printKingsAndQueens(out, col, whitePerspective, WHITE_QUEEN, BLACK_KING, WHITE_KING, BLACK_QUEEN);

                } else {
                    out.print(EMPTY);
                }
            }
        }
    }

    private static void printKingsAndQueens(PrintStream out, int col, boolean whitePerspective,
                                            String queen1, String king1, String king2, String queen2) {
        if (col == 4 && whitePerspective) { out.print(queen1); }
        else if (col == 4) { out.print(king1); }
        else if (col == 5 && whitePerspective) { out.print(king2); }
        else if (col == 5) { out.print(queen2); }
    }

    private static void printBackRow(PrintStream out, int col, String textColor,
                                     String rook, String knight, String bishop) {
        out.print(textColor);
        switch (col) {
            case (1), (8) -> out.print(rook);
            case (2), (7) -> out.print(knight);
            case (3), (6) -> out.print(bishop);
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
