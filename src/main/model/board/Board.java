package model.board;

// representation of chess board
public class Board {
    public static final int WHITE = 1;
    public static final int BLACK = -1;

    public static final int PAWN = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int ROOK = 4;
    public static final int QUEEN = 5;
    public static final int KING = 6;

    private boolean blackKingMoved;
    private boolean whiteKingMoved;
    private boolean blackRookMoved1;
    private boolean blackRookMoved8;
    private boolean whiteRookMoved1;
    private boolean whiteRookMoved8;
    private int[][] board;

    public Board() {
        blackKingMoved = false;
        whiteKingMoved = false;
        blackRookMoved1 = false;
        blackRookMoved8 = false;
        whiteRookMoved1 = false;
        whiteRookMoved8 = false;
        board = new int[8][8];
    }

    public Board(Board board) {
        this.blackKingMoved = board.blackKingMoved;
        this.whiteKingMoved = board.whiteKingMoved;
        this.blackRookMoved1 = board.blackRookMoved1;
        this.blackRookMoved8 = board.blackRookMoved8;
        this.whiteRookMoved1 = board.whiteRookMoved8;
        this.whiteRookMoved8 = board.whiteRookMoved8;
        this.board = board.board;
    }
}
