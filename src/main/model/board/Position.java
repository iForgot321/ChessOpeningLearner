package model.board;

// Row and column values of a piece; uses zero based indexing
public class Position {
    final int col;
    final int row;

    // REQUIRES: col and row in the range [0, 7]
    // EFFECTS: creates position for given column and row
    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

    // REQUIRES: position p to not be null
    // EFFECTS: creates new object with same row and col as input position
    public Position(Position p) {
        this.col = p.getCol();
        this.row = p.getRow();
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    // EFFECTS: returns true if all parameters are equal with input p, false otherwise
    public boolean equals(Position p) {
        return p != null && getCol() == p.getCol() && getRow() == p.getRow();
    }
}
