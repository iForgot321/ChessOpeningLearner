package model;

import model.board.Board;
import model.board.Position;

import java.util.ArrayList;

// Representation of move
public class Move {
    private int moveNum;
    private boolean isWhite;
    private int piece;
    private Position start;
    private Position end;
    private ArrayList<Move> nextMoves;
    private Board board;

    public Move(int moveNum, boolean isWhite, int piece, Position start, Position end) {
        this.moveNum = moveNum;
        this.isWhite = isWhite;
        this.piece = piece;
        this.start = new Position(start);
        this.end = new Position(end);
    }

    public boolean addMove(Move m) {
        nextMoves.add(m);
        return true;
    }

    public int getMoveNum() {
        return moveNum;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getPiece() {
        return piece;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public boolean equals(Move m) {
        return m != null && moveNum == m.moveNum && isWhite == m.isWhite && piece == m.piece;
    }
}
