package chess;

import java.util.Objects;

public class ChessPosition {

  public String toString() {
    String c = "";
    switch (col) {
      case 1 -> c = "a";
      case 2 -> c = "b";
      case 3 -> c = "c";
      case 4 -> c = "d";
      case 5 -> c = "e";
      case 6 -> c = "f";
      case 7 -> c = "g";
      case 8 -> c = "h";
    }
    return c + row;
  }
  public boolean equals(Object o) {
    if (this == o) {return true;}
    if (o == null || getClass() != o.getClass()) {return false;}
    ChessPosition that=(ChessPosition) o;
    return row == that.row && col == that.col;
  }
  public int hashCode() {
    return Objects.hash(row, col);
  }

  private final int row;
  private final int col;

  public ChessPosition(int row, int col) {
    this.row=row;
    this.col=col;
  }

    public int getRow() {
        return row;
    }
    public int getColumn() {
        return col;
    }
}
