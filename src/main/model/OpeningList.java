package model;

import model.board.Position;

import java.util.ArrayList;
import java.util.List;

// List of all openings in database
public class OpeningList {
    private List<Move> openings;

    // EFFECTS: Constructs empty list of openings
    public OpeningList() {
        openings = new ArrayList<>();
    }

    // REQUIRES: i less than length of list
    // EFFECTS: returns move at index i
    public Move getOpening(int i) {
        return openings.get(i);
    }

    // REQUIRES: Move is legal in beginning position
    // MODIFIES: this
    // EFFECTS: adds move to list and returns true, false if move is already in list
    public boolean addOpening(Move m) {
        for (Move i : openings) {
            if (m.getPiece() == i.getPiece() && m.getEnd().equals(i.getEnd())) {
                return false;
            }
        }
        openings.add(m);
        return true;
    }

    // REQUIRES: piece and end to be of the move parameters
    // MODIFIES: this
    // EFFECTS: removes a move from list and returns ture, else if does not exists return false
    public boolean removeOpening(int piece, Position end) {
        for (Move i : openings) {
            if (piece == i.getPiece() && i.getEnd().equals(end)) {
                openings.remove(i);
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns number of moves in list
    public int length() {
        return openings.size();
    }
}
