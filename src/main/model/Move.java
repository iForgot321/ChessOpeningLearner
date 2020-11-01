package model;

import model.board.Board;
import model.board.Position;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Representation of a single move in a chess game
public class Move implements Writable {
    private final int moveNum;
    private final int piece; // defined from Board.java class
    private final boolean isCaptures;
    private final boolean isCheck;
    private final Position start;
    private final Position end;
    private final Move parentMove;
    private final ArrayList<Move> childMoves = new ArrayList<>();
    private final Board board;

    // EFFECTS: creates new move with parameters given
    public Move(int moveNum, int piece, boolean cap, boolean check, Position start, Position end, Move pmove, Board b) {
        this.moveNum = moveNum;
        this.piece = piece;
        this.isCaptures = cap;
        this.isCheck = check;
        this.start = new Position(start);
        this.end = new Position(end);
        this.parentMove = pmove;
        this.board = b;
    }

    // EFFECTS: creates a deep copy of the input Move m
    public Move(Move m) {
        this.moveNum = m.moveNum;
        this.piece = m.piece;
        this.isCaptures = m.isCaptures;
        this.isCheck = m.isCheck;
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

    public boolean isCheck() {
        return isCheck;
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

    // REQUIRES: board represents chess state before move made
    // EFFECTS: returns -1 if move is illegal, -2 if input is wrong. returns 1 if move is pawn promotion,
    //          2 if move is castle, 3 if move is en passant, 0 if regular move.
    public int isLegal() {
        if (board.get(start.getRow(), start.getCol()) != piece || board.move(start, end).isInCheck(isWhite())) {
            return -1;
        }
        
        int type = 0;
        int absPiece = Math.abs(piece);
        if (absPiece == Board.P) {
            type = pawnMove();
        } else if (absPiece == Board.N) {
            type = knightMove();
        } else if (absPiece == Board.B) {
            type = bishopMove();
        } else if (absPiece == Board.R) {
            type = rookMove();
        } else if (absPiece == Board.Q) {
            type = queenMove();
        } else if (absPiece == Board.K) {
            type = kingMove();
        } else {
            return -2;
        }

        return type;
    }

    private int pawnMove() {
        int sr = start.getRow();
        int sc = start.getCol();
        int er = end.getRow();
        int ec = end.getCol();
        if (sc == ec) {
            return pawnForward(sr, sc, er, ec);
        } else {
            return pawnCapture(sr, sc, er, ec, parentMove);
        }
    }

    private int pawnForward(int sr, int sc, int er, int ec) {
        if (sr - er == 2) {
            if (sr != 6 || board.get(er, ec) != Board.E || board.get(er - 1, ec) != Board.E) {
                return -1;
            }
        } else if (sr - er == 1) {
            if (board.get(er, ec) != Board.E) {
                return -1;
            }
        } else {
            return -1;
        }

        if (er == 0) {
            return 1;
        }
        return 0;
    }

    private int pawnCapture(int sr, int sc, int er, int ec, Move pm) {
        if (Math.abs(ec - sc) != 1 || sr - er != 1) {
            return -1;
        } else if (board.get(er, ec) == Board.E) {
            if (sr != 3 || board.get(er + 1, ec) != Board.P || pm.getPiece() != Board.P
                    || pm.getStart().getRow() != 1 || pm.getStart().getCol() != ec
                    || pm.getEnd().getRow() != 3) {
                return -1;
            } else {
                return 3;
            }
        }

        if (er == 0) {
            return 1;
        }
        return 0;
    }

    private int knightMove() {
        int colDiff = Math.abs(end.getCol() - start.getCol());
        int rowDiff = Math.abs(end.getRow() - start.getRow());
        if (!((colDiff == 2 && rowDiff == 1) || (colDiff == 1 && rowDiff == 2))) {
            return -1;
        }
        return 0;
    }

    private int bishopMove() {
        return -1;
    }

    private int rookMove() {
        return -1;
    }

    private int queenMove() {
        if (bishopMove() == 0 || rookMove() == 0) {
            return 0;
        }
        return -1;
    }

    private int kingMove() {
        int sr = start.getRow();
        int sc = start.getCol();
        int er = end.getRow();
        int ec = end.getCol();
        Board b = board;
        boolean isWhite = piece > 0;
        if (er == sr && ec - sc == 2) {
            if (b.getMoved(isWhite ? 0 : 1) || b.getMoved(isWhite ? 3 : 5)) {
                return -1;
            } else if (b.get(er, 5) != Board.E || b.get(er, 6) != Board.E) {
                return -1;
            }
        } else if (er == sr && ec - sc == -2) {
            if (b.getMoved(isWhite ? 0 : 1) || b.getMoved(isWhite ? 2 : 4)) {
                return -1;
            } else if (b.get(er, 1) != Board.E || b.get(er, 2) != Board.E || b.get(er, 3) != Board.E) {
                return -1;
            }
        } else if (Math.abs(ec - sc) > 2 || Math.abs(er - sr) > 2) {
            return -1;
        }
        return 0;
    }

    // EFFECTS: return move as JSON Object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("moveNum", moveNum);
        json.put("piece", piece);
        json.put("isCaptures", isCaptures);
        json.put("isCheck", isCheck);
        json.put("start", start.toJson());
        json.put("end", end.toJson());
        json.put("board", board.toJson());
        addChildrenJson(json);
        return json;
    }

    // MODIFIES: child
    // EFFECTS: adds children moves as JSON Objects to json
    private void addChildrenJson(JSONObject json) {
        JSONArray jsonArray = new JSONArray();
        for (Move child : childMoves) {
            jsonArray.put(child.toJson());
        }
        json.put("childMoves", jsonArray);
    }
}
