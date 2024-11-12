package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class PrintBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final String[] letters = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
    private static final String[] pieces = {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN,
            BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawWhitePerspective(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawWhitePerspective(PrintStream out) {
        drawTopOrBottomRow(out);
        for (int boardRow = 8; boardRow > 0; boardRow--) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(" %s ", boardRow);

            drawChessRows(out, boardRow);

            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.printf(" %s ", boardRow);
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
        drawTopOrBottomRow(out);

    }

    private static void drawChessRows(PrintStream out, int boardRow) {
        for (int boardCol = 1; boardCol < 9; boardCol++) {
            if ((boardCol + boardRow) % 2 == 1) {
                out.print(SET_BG_COLOR_WHITE);
                out.print(SET_TEXT_COLOR_MAGENTA);
                out.print(BLACK_ROOK);
            }
            else {
                out.print(SET_BG_COLOR_BLACK);
                out.print(SET_TEXT_COLOR_MAGENTA);
                out.print(BLACK_ROOK);
            }
        }
    }

    private static void drawTopOrBottomRow(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(EMPTY);
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        for (int boardCol = 0; boardCol < 8; boardCol++) {
            out.print(letters[boardCol]);
        }
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.print("\n");
    }

}
