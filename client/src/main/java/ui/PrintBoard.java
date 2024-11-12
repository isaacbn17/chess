package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class PrintBoard {
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final String[] letters = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
    private static final String[] pieces = {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN,
            BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};
    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawWhitePerspective(out);
    }

    private static void drawWhitePerspective(PrintStream out) {
        drawTopOrBottomRow(out);
        for (int boardRow = 8; boardRow > 0; boardRow--) {
            drawChessRow(out, boardRow);
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
        drawTopOrBottomRow(out);

    }

    private static void drawChessRow(PrintStream out, int boardRow) {
        for (int boardCol = 0; boardCol < 10; boardCol++) {
            if (boardCol == 0 || boardCol == 9) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
                out.printf(" %s ", boardRow);
            }
            else if ((boardCol + boardRow) % 2 == 1) {
                out.print(SET_BG_COLOR_WHITE);

                if (boardRow == 8) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    switch (boardCol) {
                        case(1) -> out.print(BLACK_ROOK);
                        case(3) -> out.print(BLACK_BISHOP);
                        case(5) -> out.print(BLACK_KING);
                        case(7) -> out.print(BLACK_KNIGHT);
                    }
                }
                else if (boardRow == 7) {
                    out.print(SET_TEXT_COLOR_MAGENTA);
                    out.print(BLACK_PAWN);
                }
                else if (boardRow == 2) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    out.print(WHITE_PAWN);
                }
                else if (boardRow == 1) {
                    out.print(SET_TEXT_COLOR_BLUE);
                    switch (boardCol) {
                        case(2) -> out.print(WHITE_KNIGHT);
                        case(4) -> out.print(WHITE_QUEEN);
                        case(6) -> out.print(WHITE_BISHOP);
                        case(8) -> out.print(WHITE_ROOK);
                    }
                }
                else {
                    out.print(EMPTY);
                }
            }
            else {
                out.print(SET_BG_COLOR_BLACK);
                switch (boardRow) {
                    case (8) -> {
                        out.print(SET_TEXT_COLOR_MAGENTA);
                        switch (boardCol) {
                            case(2) -> out.print(BLACK_KNIGHT);
                            case(4) -> out.print(BLACK_QUEEN);
                            case(6) -> out.print(BLACK_BISHOP);
                            case(8) -> out.print(BLACK_ROOK);
                        }
                    }
                    case (7) -> {
                        out.print(SET_TEXT_COLOR_MAGENTA);
                        out.print(BLACK_PAWN);
                    }
                    case (2) -> {
                        out.print(SET_TEXT_COLOR_BLUE);
                        out.print(WHITE_PAWN);
                    }
                    case (1) -> {
                        out.print(SET_TEXT_COLOR_BLUE);
                        switch (boardCol) {
                            case(1) -> out.print(WHITE_ROOK);
                            case(3) -> out.print(WHITE_BISHOP);
                            case(5) -> out.print(WHITE_KING);
                            case(7) -> out.print(WHITE_KNIGHT);
                        }
                    }
                    default -> out.print(EMPTY);
                }
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
