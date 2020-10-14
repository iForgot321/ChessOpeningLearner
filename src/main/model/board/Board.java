package model.board;

// Representation of state of a chess board
public class Board {
    // Positive values represent white pieces, negative values represent black pieces
    public static final int E = 0;      // Empty
    public static final int P = 1;      // Pawn
    public static final int N = 2;      // Knight
    public static final int B = 3;      // Bishop
    public static final int R = 4;      // Rook
    public static final int Q = 5;      // Queen
    public static final int K = 6;      // King

    /*
    movedPieces[0] is whether white king has moved
    movedPieces[1] is whether black king has moved
    movedPieces[2] is whether white rook on first file has moved
    movedPieces[3] is whether white rook on eighth file has moved
    movedPieces[4] is whether black rook on first file has moved
    movedPieces[5] is whether black rook on eighth rank has moved
     */
    private boolean[] movedPieces = new boolean[6];
    private int[][] board = new int[8][8];

    // EFFECTS: creates class with same values as input arrays moved and b
    public Board(boolean[] moved, int[][] b) {
        for (int i = 0; i < 6; i++) {
            this.movedPieces[i] = moved[i];
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i] = b[i];
            }
        }
    }

    // EFFECTS: creates deep copy of input Board b
    public Board(Board b) {
        for (int i = 0; i < 6; i++) {
            this.movedPieces[i] = b.movedPieces[i];
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i] = b.board[i];
            }
        }
    }

    // REQUIRES: row and col to be within the range [0,7]
    // EFFECTS: returns value of board at row and column from input
    public int get(int row, int col) {
        return board[row][col];
    }

    // REQUIRES: i to be within the range [0, 5]
    // EFFECTS returns the value of movePieces at index i
    public boolean getMoved(int i) {
        return movedPieces[i];
    }

    // EFFECTS: returns board after move performed, replacing end position with original piece
    //          and start position with empty value
    public Board move(Position start, Position end) {
        Board newBoard = new Board(this);
        int piece = newBoard.board[start.row][start.col];
        newBoard.board[start.row][start.col] = E;
        newBoard.board[end.row][end.col] = piece;
        return newBoard;
    }

    // REQUIRES: piecePromotedTo is actual piece value
    // EFFECTS: returns board after move performed, replacing end position with promoted piece
    //          and start position with empty value
    public Board promotePawn(Position start, Position end, int piecePromotedTo) {
        Board newBoard = new Board(this);
        newBoard.board[start.row][start.col] = E;
        newBoard.board[end.row][end.col] = piecePromotedTo;
        return newBoard;
    }

    // EFFECTS: returns board after castling, replacing end positions with the king and rook
    //          and start positions with empty values
    public Board castle(boolean isWhite, boolean kingSide) {
        Board newBoard = new Board(this);
        int row;
        int colour;
        if (isWhite) {
            row = 7;
            colour = 1;
        } else {
            row = 0;
            colour = -1;
        }

        newBoard.board[row][4] = E;
        if (kingSide) {
            newBoard.board[row][7] = E;
            newBoard.board[row][6] = colour * K;
            newBoard.board[row][5] = colour * R;
        } else {
            newBoard.board[row][0] = E;
            newBoard.board[row][2] = colour * K;
            newBoard.board[row][3] = colour * R;
        }
        return newBoard;
    }

    // EFFECTS: returns board after en passant, replacing end position with pawn
    //          and both start position and captured pawn position with empty value
    public Board enPassant(Position start, Position end) {
        Board newBoard = new Board(this);
        int piece = newBoard.board[start.row][start.col];
        newBoard.board[start.row][start.col] = E;
        newBoard.board[end.row][end.col] = piece;
        newBoard.board[start.row][end.col] = E;
        return newBoard;
    }

    // EFFECTS: returns true if all values match with Board b, false otherwise
    public boolean equals(Board b) {
        if (b == null) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            if (this.movedPieces[i] != b.movedPieces[i]) {
                return false;
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.board[i][j] != b.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
