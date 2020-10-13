package model;

import model.board.Board;
import model.board.Position;

import java.util.ArrayList;

// Representation of a single move in a chess game
public class Move {
    private int moveNum;
    private boolean isWhite;
    private int piece;
    private Position start;
    private Position end;
    private Move parentMove;
    private ArrayList<Move> nextMoves;
    private Board board;

    public Move(int moveNum, boolean isWhite, int piece, Position start, Position end) {
        this.moveNum = moveNum;
        this.isWhite = isWhite;
        this.piece = piece;
        this.start = new Position(start);
        this.end = new Position(end);
        //this.parentMove = parentMove;
    }

    public Move(Move m) {
        this.moveNum = m.moveNum;
        this.isWhite = m.isWhite;
        this.piece = m.piece;
        this.start = new Position(m.start);
        this.end = new Position(m.end);
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

    public Board getBoard() {
        return board;
    }

    public boolean equals(Move m) {
        return m != null && moveNum == m.moveNum && isWhite == m.isWhite && piece == m.piece;
    }
}
