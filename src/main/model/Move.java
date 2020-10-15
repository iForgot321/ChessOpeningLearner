package model;

import model.board.Board;
import model.board.Position;

import java.util.ArrayList;

// Representation of a single move in a chess game
public class Move {
    private int moveNum;
    private int piece; // defined from Board.java class
    private boolean isCaptures;
    private Position start;
    private Position end;
    private Move parentMove;
    private ArrayList<Move> childMoves = new ArrayList<>();
    private Board board;

    // EFFECTS: creates new move with parameters given
    public Move(int moveNum, int piece, boolean cap, Position start, Position end, Move parentMove, Board board) {
        this.moveNum = moveNum;
        this.piece = piece;
        this.isCaptures = cap;
        this.start = new Position(start);
        this.end = new Position(end);
        this.parentMove = parentMove;
        this.board = board;
    }

    // EFFECTS: creates a deep copy of the input Move m
    public Move(Move m) {
        this.moveNum = m.moveNum;
        this.piece = m.piece;
        this.isCaptures = m.isCaptures;
        this.start = new Position(m.start);
        this.end = new Position(m.end);
        this.parentMove = m.parentMove;
        for (Move child : m.childMoves) {
            this.childMoves.add(new Move(child));
        }
        this.board = new Board(m.board);
    }

    // REQUIRES: i is positive and less than length of list
    // EFFECTS: returns move at index i
    public Move getChildMove(int i) {
        return childMoves.get(i);
    }

    // REQUIRES: Move is legal in current position
    // MODIFIES: this
    // EFFECTS: adds move to list and returns true, false if move is already in list
    public boolean addChildMove(Move m) {
        for (Move i : childMoves) {
            if (m.getPiece() == i.getPiece() && m.getEnd().equals(i.getEnd())) {
                return false;
            }
        }
        childMoves.add(m);
        return true;
    }

    // REQUIRES: index i positive and less than length of list
    // MODIFIES: this
    // EFFECTS: removes a move from list and index i
    public void removeChildMove(int i) {
        childMoves.remove(i);
    }

    // EFFECTS: returns number of moves in list of children moves
    public int length() {
        return childMoves.size();
    }

    public int getMoveNum() {
        return moveNum;
    }

    public boolean isWhite() {
        return piece > 0;
    }

    public int getPiece() {
        return piece;
    }

    public boolean isCaptures() {
        return isCaptures;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public Move getParentMove() {
        return parentMove;
    }

    public Board getBoard() {
        return board;
    }

    // EFFECTS: returns true if moveNum, isWhite, piece, start, end, and board is equals for both moves, otherwise false
    public boolean equals(Move m) {
        return m != null && moveNum == m.moveNum && piece == m.piece && start.equals(m.start) && end.equals(m.end)
                && board.equals(m.board);
    }

    // REQUIRES: all input parameters to fit their class restrictions
    // EFFECTS: returns -1 if move is illegal, -2 if input is wrong. returns 1 if move is pawn promotion,
    //          2 if move is castle, 3 if move is en passant, 0 if regular move.
    //use Move object as parameter
//    public static int isMoveLegal(Move pm, Board b, int piece, Position start, Position end) {
//        int sr = start.getRow();
//        int sc = start.getCol();
//        int er = end.getRow();
//        int ec = end.getCol();
//
//        if (b.get(sr, sc) != piece) {
//            return -1;
//        }
//        if (start.equals(end)) {
//            return -1;
//        }
//
//        int type = 0;
//        if (piece == Board.P) {
//            if (sc == ec) {
//                if (sr - er == 2) {
//                    if (sr != 6 || b.get(er, ec) != Board.E || b.get(er - 1, ec) != Board.E) {
//                        return -1;
//                    }
//                } else if (sr - er == 1) {
//                    if (b.get(er, ec) != Board.E) {
//                        return -1;
//                    }
//                } else {
//                    return -1;
//                }
//            } else {
//                if (Math.abs(ec - sc) != 1 || sr - er != 1) {
//                    return -1;
//                } else if (b.get(er, ec) == Board.E) {
//                    if (sr != 3 || b.get(er + 1, ec) != Board.P || pm.getPiece() != Board.P
//                            || pm.getStart().getRow() != 1 || pm.getStart().getCol() != ec
//                            || pm.getEnd().getRow() != 3) {
//                        return -1;
//                    } else {
//                        type = 3;
//                    }
//                }
//            }
//            if (er == 0) {
//                type = 1;
//            }
//        } else if (piece == -Board.P) {
//
//        } else if (Math.abs(piece) == Board.N) {
//            if (!((Math.abs(ec - sc) == 2 && Math.abs(er - sr) == 1)
//                    || (Math.abs(ec - sc) == 1 && Math.abs(er - sr) == 2))) {
//                return -1;
//            }
//        } else if (Math.abs(piece) == Board.B) {
//
//        } else if (Math.abs(piece) == Board.R) {
//
//        } else if (Math.abs(piece) == Board.Q) {
//
//        } else if (Math.abs(piece) == Board.K) {
//            if (er == sr && ec - sc == 2) {
//                if (b.getMoved(piece > 0 ? 0 : 1) || b.getMoved(piece > 0 ? 3 : 5)) {
//                    return -1;
//                } else if (b.get(er, 5) != Board.E || b.get(er, 6) != Board.E) {
//                    return -1;
//                }
//            } else if (er == sr && ec - sc == -2) {
//                if (b.getMoved(piece > 0 ? 0 : 1) || b.getMoved(piece > 0 ? 2 : 4)) {
//                    return -1;
//                } else if (b.get(er, 1) != Board.E || b.get(er, 2) != Board.E || b.get(er, 3) != Board.E) {
//                    return -1;
//                }
//            } else if (Math.abs(ec - sc) > 2 || Math.abs(er - sr) > 2) {
//                return -1;
//            }
//        } else {
//            return -2;
//        }
//
//        if (inCheck(b.move(start, end), piece < 0)) {
//            return -1;
//        }
//        return type;
//    }
//
//    public static boolean inCheck(Board b, boolean whiteTurn) {
//        return false;
//    }
}
