package model.board;


public class Position {
    private int col;
    private int row;

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

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

    // EFFECTS: returns true if all parameters are equal with p, false otherwise
    public boolean equals(Position p) {
        return p != null && getCol() == p.getCol() && getRow() == p.getRow();
    }
}
