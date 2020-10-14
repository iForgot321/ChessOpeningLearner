package model.board;

// Row and column values of a piece; uses zero based indexing
public class Position {
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

    // EFFECTS: returns true if all parameters are equal with input p, false otherwise
    public boolean equals(Position p) {
        return p != null && getRow() == p.getRow() && getCol() == p.getCol();
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
}
