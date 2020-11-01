package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import model.board.*;
import model.Move;
import persistence.JsonReader;
import persistence.JsonWriter;

import static model.board.Board.*;

// User interface for the application
public class OpeningApp {
    private static final String OPENING_FILE = "./data/openingList.json";
    private Move root;
    private Move currentMove;
    private final Scanner scan = new Scanner(System.in);
    private boolean keepGoing = true;
    private JsonReader reader;
    private JsonWriter writer;

    // EFFECTS: Begins input process
    public OpeningApp() {
        init();
        useOpenings();
    }

    // MODIFIES: this
    // EFFECTS: initializes beginning variables and objects with default values
    private void init() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, P, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        boolean[] moved = new boolean[6];
        Board b = new Board(moved, board);

        root = new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -1), null, b);
        currentMove = root;

        reader = new JsonReader(OPENING_FILE);
        writer = new JsonWriter(OPENING_FILE);
    }

    // EFFECTS: loops action process until keepGoing == false
    private void useOpenings() {
        while (keepGoing) {
            inputMessage();
            processAction();
        }
    }

    // EFFECTS: Takes in input and uses corresponding function
    private void processAction() {
        System.out.print("Action: ");
        String action = scan.next();
        if (action.equals("a")) {
            addMove();
        } else if (action.equals("d")) {
            deleteMove();
        } else if (action.equals("v")) {
            viewMove();
        } else if (action.equals("e")) {
            exportMove();
        } else if (action.equals("q")) {
            quit();
        } else if (action.equals("l")) {
            loadMoves();
        } else if (action.equals("s")) {
            saveMoves();
        } else {
            System.out.println("Action not recognized");
            processAction();
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds child move to currentMove, with positions taken from console input
    private void addMove() {
        System.out.println("\nInput the move with the beginning and end position separated by a space.\nE.g. e2 e4");
        while (true) {
            Position start = Position.notationToPosition(scan.next());
            Position end = Position.notationToPosition(scan.next());
            if (start != null && end != null) {
                int newNum = currentMove.getMoveNum() + 1;
                int newPiece =  currentMove.getBoard().get(start.getRow(), start.getCol());

                Board temp = currentMove.getBoard().move(start, end);
                boolean cap = currentMove.getBoard().get(end.getRow(), end.getCol()) != E;
                if (currentMove.addChildMove(new Move(newNum, newPiece, cap, false, start, end, currentMove, temp))) {
                    System.out.println("Move successfully added\n");
                    currentMove = currentMove.getChildMove(currentMove.length() - 1);
                } else {
                    System.out.println("Move already exists\n");
                }
                return;
            } else {
                System.out.println("Positions not recognized\n");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes move at given index from input
    private void deleteMove() {
        displayMoveList();
        System.out.println("Specify the index of the move to be deleted");
        while (true) {
            String input = scan.next();
            try {
                int i = Integer.parseInt(input) - 1;
                if (i >= 0 && i < currentMove.length()) {
                    currentMove.removeChildMove(i);
                    System.out.println("Move removed\n");
                    return;
                } else {
                    System.out.println("Index out of range\n");
                }
            } catch (Exception e) {
                System.out.println("Position not recognized\n");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: references currentMove to a child move specified from input
    private void viewMove() {
        if (currentMove.length() == 0) {
            System.out.println("There are no moves to be viewed\n");
            return;
        }
        displayMoveList();
        System.out.println("Specify the index of the move to be viewed");
        while (true) {
            String input = scan.next();
            try {
                int i = Integer.parseInt(input) - 1;
                if (i >= 0 && i < currentMove.length()) {
                    Move temp = currentMove.getChildMove(i);
                    String piece = toNotation[Math.abs(temp.getPiece()) - 1] + (temp.isCaptures() ? "x" : "");
                    System.out.println("Selected " + piece + temp.getEnd().toChessNotation());
                    currentMove = temp;
                    return;
                } else {
                    System.out.println("Index out of range\n");
                }
            } catch (Exception e) {
                System.out.println("Position not recognized\n");
            }
        }
    }

    // EFFECTS: prints out current move list
    private void exportMove() {
        System.out.println("\nCurrent sequence of moves:");
        StringBuilder s = new StringBuilder();
        Move pointer = currentMove;
        while (pointer.getParentMove() != null) {
            s.insert(0, toNotation[Math.abs(pointer.getPiece()) - 1] + pointer.getEnd().toChessNotation() + " ");
            if (pointer.isWhite()) {
                s.insert(0, ((pointer.getMoveNum() + 1) / 2) + ". ");
            }
            pointer = pointer.getParentMove();
        }
        System.out.println(s);
    }

    // MODIFIES: this
    // EFFECTS: returns to parent move, quits application if already at root move
    private void quit() {
        if (currentMove.getMoveNum() == 0) {
            System.out.println("Goodbye!");
            keepGoing = false;
        } else {
            currentMove = currentMove.getParentMove();
            System.out.println("\nReturning to previous move\n");
        }
    }

    // MODIFIES: this
    // EFFECTS: load moves from json file
    private void loadMoves() {
        try {
            root = reader.read();
            currentMove = root;
            System.out.println("Successfully loaded moves from " + OPENING_FILE);
        } catch (IOException e) {
            System.out.println("Error loading moves. Check " + OPENING_FILE + " for issues");
        }
    }

    // EFFECTS: writes current move list to json file
    private void saveMoves() {
        try {
            writer.open();
            writer.write(root);
            writer.close();
            System.out.println("Successfully saved moves to " + OPENING_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Error saving moves. Check " + OPENING_FILE + " for issues");
        }
    }

    // EFFECTS: displays current board state and move list
    private void inputMessage() {
        if (currentMove.getMoveNum() != 0) {
            System.out.println("Move number " + currentMove.getMoveNum());
        }
        System.out.print("Current board state, ");
        System.out.println((currentMove.isWhite() ? "black" : "white") + " to move:");
        printBoard(currentMove.getBoard());

        if (currentMove.length() == 0) {
            System.out.println("There are no moves in this line\n");
        } else {
            displayMoveList();
        }
        displayActions();
    }

    // EFFECT: displays list of next moves with indexes
    public void displayMoveList() {
        System.out.print("Next moves are:\n\t");
        for (int i = 0; i < currentMove.length(); i++) {
            Move temp = currentMove.getChildMove(i);
            String index = "(" + (i + 1) + ") ";
            String piece = toNotation[Math.abs(temp.getPiece()) - 1];
            String cap = temp.isCaptures() ? "x" : "";
            String startFile = "";
            if (piece.equals("") && cap.equals("x")) {
                startFile = String.valueOf(temp.getStart().toChessNotation().charAt(0));
            }
            System.out.print(index + piece + startFile + cap + temp.getEnd().toChessNotation());
            if (i != currentMove.length() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("\n");
    }

    // EFFECT: displays next possible actions
    private void displayActions() {
        System.out.println("Please select an action from the list below");
        System.out.println("\ta - Add a new opening line from current board state");
        System.out.println("\td - Delete an existing opening line");
        System.out.println("\tv - View an existing opening line");
        System.out.println("\te - Export current opening line");
        System.out.println("\ts - Save current list to file");
        System.out.println("\tl - Load list from file");
        if (currentMove.getMoveNum() == 0) {
            System.out.println("\tq - Exit the application");
        } else {
            System.out.println("\tq - Return to previous move");
        }
        System.out.println();
    }

    // EFFECTS: prints out Board b from input in console
    private static void printBoard(Board b) {
        System.out.println("   _________________________________");
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + (8 - i) + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print("| " + toString[b.get(i, j) + 6] + " ");
            }
            System.out.println("|");
            System.out.println("   _________________________________");
        }
        System.out.println("     A   B   C   D   E   F   G   H\n");
    }
}
