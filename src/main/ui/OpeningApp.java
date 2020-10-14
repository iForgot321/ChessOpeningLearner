package ui;

import java.util.Scanner;

import model.board.*;
import model.Move;
import static model.board.Board.*;

public class OpeningApp {
    Move currentMove;
    Move root;
    Scanner scan = new Scanner(System.in);
    boolean keepGoing = true;

    public OpeningApp() {
        init();
        useOpenings();
    }

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

        root = new Move(0, 0, new Position(-1, -1), new Position(-1, -1), null, b);
        currentMove = root;
    }

    private void useOpenings() {
        while (keepGoing) {
            inputMessage();
            processAction();
        }
    }

    private void processAction() {
        while (true) {
            System.out.print("Action: ");
            String action = scan.next();
            System.out.println();
            switch (action) {
                case "a":
                    addMove();
                    return;
                case "d":
                    deleteMove();
                    return;
                case "v":
                    viewMove();
                    return;
                case "q":
                    quit();
                    return;
                default:
                    System.out.println("Action not recognized");
                    break;
            }
        }
    }

    private void addMove() {
        System.out.println("Input the move with the beginning position and end position separated by a space.");
        System.out.println("E.g. e2 e4");
        while (true) {
            Position start = Position.notationToPosition(scan.next());
            Position end = Position.notationToPosition(scan.next());

            if (start != null && end != null) {
                int newNum = currentMove.getMoveNum() + 1;
                int newPiece =  currentMove.getBoard().get(start.getRow(), start.getCol());
                Board temp = currentMove.getBoard().move(start, end);
                boolean b = currentMove.addChildMove(new Move(newNum, newPiece, start, end, currentMove, temp));
                if (b) {
                    System.out.println("Move successfully added");
                    currentMove = currentMove.getChildMove(currentMove.length() - 1);
                } else {
                    System.out.println("Move already exists");
                }
                return;
            } else {
                System.out.println("Positions not recognized");
            }
        }
    }

    private void deleteMove() {
        System.out.println("Specify the index of the move to be deleted");
        System.out.println("E.g. 1");
        while (true) {
            String input = scan.next();
            try {
                int i = Integer.parseInt(input);
                if (i >= 0 && i < currentMove.length()) {
                    currentMove.removeChildMove(i);
                    System.out.println("Move removed");
                    return;
                } else {
                    System.out.println("Index out of range");
                }
            } catch (Exception e) {
                System.out.println("Position not recognized");
            }
        }
    }

    private void viewMove() {
        System.out.println("Specify the index of the move to be viewed");
        System.out.println("E.g. 1");
        while (true) {
            String input = scan.next();
            try {
                int i = Integer.parseInt(input);
                if (i >= 0 && i < currentMove.length()) {
                    Move temp = currentMove.getChildMove(i);
                    String piece = toNotation[Math.abs(temp.getPiece()) - 1];
                    System.out.println("Selected " + piece + temp.getEnd().toChessNotation());
                    currentMove = temp;
                    return;
                } else {
                    System.out.println("Index out of range");
                }
            } catch (Exception e) {
                System.out.println("Position not recognized");
            }
        }
    }

    private void quit() {
        if (currentMove.getMoveNum() == 0) {
            System.out.println("Goodbye!");
            keepGoing = false;
        } else {
            currentMove = currentMove.getParentMove();
            System.out.println("Returning to previous move");
        }
    }

    private void inputMessage() {
        if (currentMove.getMoveNum() != 0) {
            System.out.println("Move number " + currentMove.getMoveNum());
        }
        System.out.println("Current board state: ");
        printBoard(currentMove.getBoard());

        if (currentMove.length() == 0) {
            System.out.println("There are no moves in this line");
        } else {
            System.out.print("Next moves are:\n\t");
            for (int i = 0; i < currentMove.length(); i++) {
                Move temp = currentMove.getChildMove(i);
                System.out.print(toNotation[Math.abs(temp.getPiece()) - 1] + temp.getEnd().toChessNotation());
                if (i != currentMove.length() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
        displayActions();
    }

    private void displayActions() {
        System.out.println("Please select an action from the list below");
        System.out.println("\ta - Add a new opening line from current board state");
        System.out.println("\td - Delete an existing opening line");
        System.out.println("\tv - View an existing opening line");
        if (currentMove.getMoveNum() == 0) {
            System.out.println("\tq - Exit the application");
        } else {
            System.out.println("\tq - Return to previous move");
        }
        System.out.println("\n");
    }

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
        System.out.println("     A   B   C   D   E   F   G   H");
        System.out.println("\n\n");
    }
}
