package model.board;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

// Row and column values of a piece; uses zero based indexing
public class Position implements Writable {
    final int col;
    final int row;

    // REQUIRES: col and row in the range [0, 7]
    // EFFECTS: creates position for given column and row
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // REQUIRES: position p to not be null
    // EFFECTS: creates new object with same row and col as input position
    public Position(Position p) {
        this.col = p.getCol();
        this.row = p.getRow();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // EFFECTS: returns whether current position is a valid position in chess
    public boolean isValid() {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }

    // EFFECTS: returns whether row and column are valid
    public static boolean isValid(int row, int col) {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return col == position.col &&  row == position.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    // EFFECTS: returns position coordinates into chess notation as a String
    public String toChessNotation() {
        return (char) ('a' + col) + Integer.toString(8 - row);
    }

    // EFFECTS: returns a position object with given row and column values from s, else returns null
    public static Position notationToPosition(String s) {
        if (s.length() != 2) {
            return null;
        }

        String[] sa = s.split("");
        try {
            int row = 8 - Integer.parseInt(sa[1]);
            int col = sa[0].charAt(0) - 'a';

            if (row <= 7 && row >= 0 && col <= 7 && col >= 0) {
                return new Position(row, col);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    // EFFECTS: return position as JSON Object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("row", row);
        json.put("col", col);
        return json;
    }
}
