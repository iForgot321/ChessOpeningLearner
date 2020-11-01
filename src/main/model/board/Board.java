package model.board;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Representation of state of a chess board
public class Board implements Writable {
    // Positive values represent white pieces, negative values represent black pieces
    public static final int E = 0;      // Empty
    public static final int P = 1;      // Pawn
    public static final int N = 2;      // Knight
    public static final int B = 3;      // Bishop
    public static final int R = 4;      // Rook
    public static final int Q = 5;      // Queen
    public static final int K = 6;      // King

    public static final String[] toString = {"k","q","r","b","n","p"," ","P","N","B","R","Q","K"};
    public static final String[] toNotation = {"","N","B","R","Q","K"};

    /*
    movedPieces[0] is whether white king has moved
    movedPieces[1] is whether black king has moved
    movedPieces[2] is whether white rook on first file has moved
    movedPieces[3] is whether white rook on eighth file has moved
    movedPieces[4] is whether black rook on first file has moved
    movedPieces[5] is whether black rook on eighth rank has moved
     */
    private final boolean[] movedPieces = new boolean[6];
    private final int[][] board = new int[8][8];

    // EFFECTS: creates class with same values as input arrays moved and b
    public Board(boolean[] moved, int[][] b) {
        System.arraycopy(moved, 0, this.movedPieces, 0, 6);
        for (int i = 0; i < 8; i++) {
            System.arraycopy(b[i], 0, this.board[i], 0, 8);
        }
    }

    // EFFECTS: creates deep copy of input Board b
    public Board(Board b) {
        System.arraycopy(b.movedPieces, 0, this.movedPieces, 0, 6);
        for (int i = 0; i < 8; i++) {
            System.arraycopy(b.board[i], 0, this.board[i], 0, 8);
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
        Board newBoard = new Board(movedPieces, board);
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

    // EFFECTS: coverts string into the corresponding piece value, else returns -1
    public static int stringToPiece(String s) {
        switch (s) {
            case "":
                return P;
            case "N":
                return N;
            case "B":
                return B;
            case "R":
                return R;
            case "Q":
                return Q;
            case "K":
                return K;
            default:
                return -1;
        }
    }

    // EFFECTS: returns whether the given colour is in check
    public boolean isInCheck(boolean checkWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieceCheck(i, j, checkWhite)) {
                    return true;
                }
            }
        }
        return false;
    }

    // EFFECTS: returns whether the piece at position (i, j) is giving check to white if w is true, black if false
    private boolean pieceCheck(int i, int j, boolean w) {
        if (board[i][j] != 0 && board[i][j] < 0 == w) {
            int absPiece = Math.abs(board[i][j]);
            boolean inCheck;
            if (absPiece == P) {
                inCheck = pawnCheck(i, j, w);
            } else if (absPiece == N) {
                inCheck = knightCheck(i, j, w);
            } else if (absPiece == B) {
                inCheck = bishopCheck(i, j, w);
            } else if (absPiece == R) {
                inCheck = rookCheck(i, j, w);
            } else if (absPiece == Q) {
                inCheck = queenCheck(i, j, w);
            } else {
                inCheck = kingCheck(i, j, w);
            }
            return inCheck;
        }
        return false;
    }

    // EFFECTS: returns whether the pawn at (i, j) is giving check
    private boolean pawnCheck(int i, int j, boolean w) {
        int side = w ? 1 : -1;
        return (j + 1 <= 7 && board[i + side][j + 1] == side * K) || (j - 1 >= 0 && board[i + side][j - 1] == side * K);
    }

    // EFFECTS: returns whether the knight at (i, j) is giving check
    private boolean knightCheck(int i, int j, boolean w) {
        int[][] knightMoves = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};
        for (int k = 0; k < 8; k++) {
            int row = i + knightMoves[k][0];
            int col = j + knightMoves[k][1];
            if (row >= 0 && row <= 7 && col >= 0 && col <= 7 && board[row][col] == (w ? 1 : -1) * K) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns whether the bishop at (i, j) is giving check
    private boolean bishopCheck(int i, int j, boolean w) {
        int[][] bishopDirs = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        for (int d = 0; d < 4; d++) {
            for (int k = 1;; k++) {
                int row = i + bishopDirs[d][0] * k;
                int col = j + bishopDirs[d][1] * k;
                if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
                    if (board[row][col] == (w ? 1 : -1) * K) {
                        return true;
                    } else if (board[row][col] != E) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }

    // EFFECTS: returns whether the rook at (i, j) is giving check
    private boolean rookCheck(int i, int j, boolean w) {
        int[][] rookDirs = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};
        for (int d = 0; d < 4; d++) {
            for (int k = 1;; k++) {
                int row = i + rookDirs[d][0] * k;
                int col = j + rookDirs[d][1] * k;
                if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
                    if (board[row][col] == (w ? 1 : -1) * K) {
                        return true;
                    } else if (board[row][col] != E) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }

    // EFFECTS: returns whether the queen at (i, j) is giving check
    private boolean queenCheck(int i, int j, boolean w) {
        return bishopCheck(i, j, w) || rookCheck(i, j, w);
    }

    // EFFECTS: returns whether the king at (i, j) is giving check
    private boolean kingCheck(int i, int j, boolean w) {
        int[][] kingMoves = {{1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}};
        for (int k = 0; k < 8; k++) {
            int row = i + kingMoves[k][0];
            int col = j + kingMoves[k][1];
            if (row >= 0 && row <= 7 && col >= 0 && col <= 7 && board[row][col] == (w ? 1 : -1) * K) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: return board as JSON Object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        addMovedPieces(json);
        addBoard(json);
        return json;
    }

    // MODIFIES: json
    // EFFECTS: add movedPieces to json as JSONArray
    private void addMovedPieces(JSONObject json) {
        JSONArray jsonArray = new JSONArray();
        for (boolean moved : movedPieces) {
            jsonArray.put(moved);
        }
        json.put("movedPieces", jsonArray);
    }

    // MODIFIES: json
    // EFFECTS: add board to json as 2D JSONArray
    private void addBoard(JSONObject json) {
        JSONArray jsonArray = new JSONArray();
        for (int[] row : board) {
            JSONArray jsonRow = new JSONArray();
            for (int val : row) {
                jsonRow.put(val);
            }
            jsonArray.put(jsonRow);
        }
        json.put("pieceBoard", jsonArray);
    }
}
