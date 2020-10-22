package model;

import model.board.Board;
import model.board.Position;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Representation of a single move in a chess game
public class Move implements Writable {
    private final int moveNum;
    private final int piece; // defined from Board.java class
    private final boolean isCaptures;
    private final Position start;
    private final Position end;
    private final Move parentMove;
    private final ArrayList<Move> childMoves = new ArrayList<>();
    private final Board board;

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
    public static int isMoveLegal(Move m) {
        Move pm = m.parentMove;
        Board b = m.board;
        int piece = m.piece;
        int sr = m.getStart().getRow();
        int sc = m.getStart().getCol();
        int er = m.getEnd().getRow();
        int ec = m.getEnd().getCol();

        if (b.get(sr, sc) != piece || m.getStart().equals(m.getEnd())) {
            return -1;
        }
        
        int type = 0;
        if (piece == Board.P) {
            type = whitePawnMove(sr, sc, er, ec, b, pm);
        } else if (piece == -Board.P) {
            type = blackPawnMove(sr, sc, er, ec, b, pm);
        } else if (Math.abs(piece) == Board.N) {
            type = knightMove(sr, sc, er, ec);
        } else if (Math.abs(piece) == Board.B) {
            type = bishopMove();
        } else if (Math.abs(piece) == Board.R) {
            type = rookMove();
        } else if (Math.abs(piece) == Board.Q) {
            type = queenMove();
        } else if (Math.abs(piece) == Board.K) {
            type = kingMove(sr, sc, er, ec, piece > 0, b);
        } else {
            return -2;
        }

        if (inCheck(b.move(m.getStart(), m.getEnd()), piece < 0)) {
            return -1;
        }
        return type;
    }

    private static int whitePawnMove(int sr, int sc, int er, int ec, Board b, Move pm) {
        if (sc == ec) {
            if (sr - er == 2) {
                if (sr != 6 || b.get(er, ec) != Board.E || b.get(er - 1, ec) != Board.E) {
                    return -1;
                }
            } else if (sr - er == 1) {
                if (b.get(er, ec) != Board.E) {
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            if (Math.abs(ec - sc) != 1 || sr - er != 1) {
                return -1;
            } else if (b.get(er, ec) == Board.E) {
                if (sr != 3 || b.get(er + 1, ec) != Board.P || pm.getPiece() != Board.P
                        || pm.getStart().getRow() != 1 || pm.getStart().getCol() != ec
                        || pm.getEnd().getRow() != 3) {
                    return -1;
                } else {
                    return 3;
                }
            }
        }
        if (er == 0) {
            return 1;
        }
        return 0;
    }

    private static int blackPawnMove(int sr, int sc, int er, int ec, Board b, Move pm) {
        return -1;
    }

    private static int knightMove(int sr, int sc, int er, int ec) {
        if (!((Math.abs(ec - sc) == 2 && Math.abs(er - sr) == 1)
                || (Math.abs(ec - sc) == 1 && Math.abs(er - sr) == 2))) {
            return -1;
        }
        return 0;
    }

    private static int bishopMove() {
        return -1;
    }

    private static int rookMove() {
        return -1;
    }

    private static int queenMove() {
        return -1;
    }

    private static int kingMove(int sr, int sc, int er, int ec, boolean isWhite, Board b) {
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

    public static boolean inCheck(Board b, boolean whiteTurn) {
        return false;
    }

    // EFFECTS: return move as JSON Object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("moveNum", moveNum);
        json.put("piece", piece);
        json.put("isCaptures", isCaptures);
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
