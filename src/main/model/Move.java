package model;

import model.board.Board;
import model.board.Position;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Objects;

import static model.board.Board.toNotation;

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
            if (m.getStart().equals(i.getStart()) && m.getEnd().equals(i.getEnd())) {
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

    // MODIFIES: this
    // EFFECTS: removes move m from list if exists
    public void removeChildMove(Move m) {
        childMoves.remove(m);
    }

    // MODIFIES: this
    // EFFECTS: removes all child moves from list
    public void removeAllMoves() {
        childMoves.clear();
    }

    // EFFECTS: returns number of moves in list of children moves
    public int childCount() {
        return childMoves.size();
    }

    // EFFECTS: returns index of move m in childMoves, if does not exist return -1
    public int getIndexOfChild(Move m) {
        for (int i = 0; i < childMoves.size(); i++) {
            if (childMoves.get(i).equals(m)) {
                return i;
            }
        }
        return -1;
    }

    // EFFECTS: returns index of move m from start and end positions in childMoves, if does not exist return -1
    public int getIndexOfChild(Position start, Position end) {
        for (int i = 0; i < childMoves.size(); i++) {
            if (childMoves.get(i).start.equals(start) && childMoves.get(i).end.equals(end)) {
                return i;
            }
        }
        return -1;
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
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move)) {
            return false;
        }
        Move m = (Move) o;
        return moveNum == m.moveNum
                && piece == m.piece
                && start.equals(m.start)
                && end.equals(m.end)
                && board.equals(m.board);
    }

    // EFFECTS: creates hashcode based on parameters
    @Override
    public int hashCode() {
        return Objects.hash(moveNum, piece, isCaptures, isCheck, start, end);
    }

    // REQUIRES: board represents chess state before move made
    // EFFECTS: returns -1 if move is illegal, 1 if move is pawn promotion,
    //          2 if move is castle, 3 if move is en passant, 0 if regular move.
    public int isLegal() {
        int piece = board.get(start);
        if (board.get(end) * piece > 0 || board.move(start, end, 0, 0).isInCheck(isWhite())) {
            return -1;
        }
        int absPiece = Math.abs(piece);
        if (absPiece == Board.P) {
            return pawnMove();
        } else if (absPiece == Board.N) {
            return knightMove();
        } else if (absPiece == Board.B) {
            return bishopMove();
        } else if (absPiece == Board.R) {
            return rookMove();
        } else if (absPiece == Board.Q) {
            return queenMove();
        } else if (absPiece == Board.K) {
            return kingMove();
        } else {
            return -1;
        }
    }

    // EFFECTS: returns 1 if pawn move is promotion, 3 if move is en passant, 0 if regular move. -1 if illegal.
    private int pawnMove() {
        int sr = start.getRow();
        int sc = start.getCol();
        int er = end.getRow();
        int ec = end.getCol();
        int side = isWhite() ? 1 : -1;
        int res;
        if (sc == ec) {
            res = pawnForward(sr, er, ec, side);
        } else {
            res = pawnCapture(sr, sc, er, ec, side, parentMove);
        }

        if (res == -1) {
            return -1;
        } else if (er == (isWhite() ? 0 : 7)) {
            return 1;
        }
        return res;
    }

    // EFFECTS: checks whether pawn can make the move forward
    private int pawnForward(int sr, int er, int ec, int side) {
        if (sr - er == side * 2) {
            int secondRow = isWhite() ? 6 : 1;
            if (sr != secondRow
                    || board.get(er, ec) != Board.E
                    || board.get(er + side, ec) != Board.E) {
                return -1;
            }
        } else if (sr - er == side) {
            if (board.get(er, ec) != Board.E) {
                return -1;
            }
        } else {
            return -1;
        }
        return 0;
    }

    // EFFECTS: checks whether pawn can make the capturing move
    private int pawnCapture(int sr, int sc, int er, int ec, int side, Move pm) {
        if (Math.abs(ec - sc) != 1 || sr - er != side) {
            return -1;
        } else if (board.get(er, ec) == Board.E) {
            if (pm != null
                    && sr == (isWhite() ? 3 : 4)
                    && board.get(er + side, ec) == -side * Board.P
                    && pm.getPiece() == -side * Board.P
                    && pm.getStart().getRow() == (isWhite() ? 1 : 6)
                    && pm.getStart().getCol() == ec
                    && pm.getEnd().getRow() == (isWhite() ? 3 : 4)) {
                return 3;
            } else {
                return -1;
            }
        }
        return 0;
    }

    // EFFECTS: checks whether knight move is legal
    private int knightMove() {
        int colDiff = end.getCol() - start.getCol();
        int rowDiff = end.getRow() - start.getRow();
        if (Math.abs(rowDiff * colDiff) == 2) {
            return 0;
        }
        return -1;
    }

    // EFFECTS: checks whether bishop move is legal
    private int bishopMove() {
        int rowDiff = end.getRow() - start.getRow();
        int colDiff = end.getCol() - start.getCol();
        if (Math.abs(rowDiff) == Math.abs(colDiff)) {
            int rowDir = Math.abs(rowDiff) / rowDiff;
            int colDir = Math.abs(colDiff) / colDiff;
            for (int i = 1; i < Math.max(Math.abs(rowDiff), Math.abs(colDiff)); i++) {
                if (board.get(start.getRow() + rowDir * i, start.getCol() + colDir * i) != Board.E) {
                    return -1;
                }
            }
            return 0;
        }
        return -1;
    }

    // EFFECTS: checks whether rook move is legal
    private int rookMove() {
        if (start.getCol() == end.getCol() || start.getRow() == end.getRow()) {
            int rowDiff = end.getRow() - start.getRow();
            int colDiff = end.getCol() - start.getCol();
            int rowDir = rowDiff == 0 ? 0 : Math.abs(rowDiff) / rowDiff;
            int colDir = colDiff == 0 ? 0 : Math.abs(colDiff) / colDiff;
            for (int i = 1; i < Math.max(Math.abs(rowDiff), Math.abs(colDiff)); i++) {
                if (board.get(start.getRow() + rowDir * i, start.getCol() + colDir * i) != Board.E) {
                    return -1;
                }
            }
            return 0;
        }
        return -1;
    }

    // EFFECTS: checks whether queen move is legal
    private int queenMove() {
        return (bishopMove() == 0
                || rookMove() == 0) ? 0 : -1;
    }

    // EFFECTS: checks whether king move is legal
    private int kingMove() {
        int sr = start.getRow();
        int sc = start.getCol();
        int er = end.getRow();
        int ec = end.getCol();
        Board b = board;
        boolean isW = isWhite();
        if (er == sr && ec - sc == 2) {
            if (b.getMoved(isW ? 0 : 1) || b.getMoved(isW ? 3 : 5) || b.get(er, 5) != Board.E
                    || b.get(er, 6) != Board.E || castleInCheck(true)) {
                return -1;
            }
            return 2;
        } else if (er == sr && ec - sc == -2) {
            if (b.getMoved(isW ? 0 : 1) || b.getMoved(isW ? 2 : 4) || b.get(er, 1) != Board.E
                    || b.get(er, 2) != Board.E || b.get(er, 3) != Board.E || castleInCheck(false)) {
                return -1;
            }
            return 2;
        } else if (Math.abs(ec - sc) >= 2 || Math.abs(er - sr) >= 2) {
            return -1;
        }
        return 0;
    }

    // EFFECTS: returns whether or not king would be in check at any point in the castle
    private boolean castleInCheck(boolean kingSide) {
        if (board.isInCheck(isWhite())) {
            return true;
        }
        if (kingSide) {
            return board.move(start, new Position(end.getRow(), start.getCol() + 1), 0, piece).isInCheck(isWhite());
        } else {
            return board.move(start, new Position(end.getRow(), start.getCol() - 1), 0, piece).isInCheck(isWhite());
        }
    }

    // EFFECTS: returns move as string int chess notation
    public String toChessNotation() {
        if (parentMove == null) {
            return "Opening List";
        } else if (Math.abs(piece) == Board.K && end.getCol() - start.getCol() == 2) {
            return "0-0";
        } else if (Math.abs(piece) == Board.K && end.getCol() - start.getCol() == -2) {
            return "0-0-0";
        }
        String piece = toNotation[Math.abs(this.piece) - 1];
        String cap = isCaptures ? "x" : "";
        String check = isCheck ? "+" : "";
        if (Math.abs(this.piece) == 1 && isCaptures) {
            cap = start.toChessNotation().charAt(0) + cap;
        }
        return piece + cap + end.toChessNotation() + check;
    }

    // EFFECTS: returns entire move line as string in chess notation
    public String lineToString() {
        StringBuilder s = new StringBuilder();
        Move pointer = this;
        while (pointer.getParentMove() != null) {
            StringBuilder temp = new StringBuilder(pointer.toChessNotation());
            s.append(temp.reverse());
            s.append(" ");
            if (pointer.isWhite()) {
                s.append(".");
                s.append((pointer.getMoveNum() + 1) / 2);
                s.append("  ");
            }
            pointer = pointer.getParentMove();
        }
        return s.reverse().toString();
    }

    // EFFECTS: creates object array of the path to get to move from root
    public Object[] getPath() {
        if (parentMove == null) {
            return null;
        }
        Object[] res = new Object[moveNum + 1];
        Move pointer = this;
        for (int i = moveNum; i >= 0; i--) {
            res[i] = pointer;
            pointer = pointer.parentMove;
        }
        return res;
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

    // EFFECTS: returns move in chess notation
    @Override
    public String toString() {
        return toChessNotation();
    }
}
