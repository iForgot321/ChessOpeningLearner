package model;

import model.board.Position;
import model.board.Board;

import static model.board.Board.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoveTest {
    Move test;
    Move test2;
    Move test3;
    Move test4;

    @BeforeEach
    void runBefore() {
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
        test = new Move(0, 0, new Position(-1, -1), new Position(-1, -1), null, b);

        int[][] board2 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b2 = new Board(moved, board2);
        test2 = new Move(1, P, new Position(6, 4), new Position(4, 4), test, b2);

        int[][] board3 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, P, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b3 = new Board(moved, board3);
        test3 = new Move(1, P, new Position(6, 4), new Position(5, 4), test, b3);

        int[][] board4 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, N, E, E},
                {P, P, P, P, P, P, P, P},
                {R, N, B, Q, K, B, E, R}};
        Board b4 = new Board(moved, board4);
        test4 = new Move(1, N, new Position(7, 6), new Position(5, 5), test, b4);
    }

    @Test
    void testGetMove() {
        test.addChildMove(test2);

        int[][] board2 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        boolean[] moved = new boolean[6];
        Board b2 = new Board(moved, board2);
        Move m = new Move(1, P, new Position(6, 4), new Position(4, 4), test, b2);

        assertTrue(test.getChildMove(0).equals(m));
    }

    @Test
    void testAddMove() {
        assertTrue(test.addChildMove(test2));
        assertTrue(test.addChildMove(test3));
        assertEquals(2, test.length());
        assertFalse(test.addChildMove(test3));

        assertTrue(test.addChildMove(test4));
        assertFalse(test.addChildMove(test4));
    }

    @Test
    void testRemoveMove() {
        test.addChildMove(test2);
        assertTrue(test.removeChildMove(1, new Position(4, 4)));
        assertEquals(0, test.length());

        test.addChildMove(test3);
        test.addChildMove(test4);
        assertFalse(test.removeChildMove(1, new Position(4, 4)));
    }

    @Test
    void testLength() {
        assertEquals(0, test.length());
    }

    @Test
    void testGetMoveNum() {
        assertEquals(1, test2.getMoveNum());
    }

    @Test
    void testIsWhite() {
        assertTrue(test2.isWhite());
        assertFalse(test.isWhite());
    }

    @Test
    void testGetPiece() {
        assertEquals(P, test2.getPiece());
    }

    @Test
    void testGetStart() {
        assertTrue(test2.getStart().equals(new Position(6, 4)));
    }

    @Test
    void testGetEnd() {
        assertTrue(test2.getEnd().equals(new Position(4, 4)));
    }

    @Test
    void testGetParentMove() {
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
        Move m = new Move(0, 0, new Position(-1, -1), new Position(-1, -1), null, b);

        assertTrue(test2.getParentMove().equals(m));
    }

    @Test
    void testGetBoard() {
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

        assertTrue(test.getBoard().equals(b));
    }

    @Test
    void testEquals() {
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

        test.addChildMove(test2);
        Move m = new Move(test);
        assertTrue(test.equals(m));

        assertFalse(test.equals(null));
        assertFalse(test.equals(test2));
        assertFalse(test.equals(new Move(0, K, new Position(-1, -1), new Position(-1, -1), null, b)));
        assertFalse(test.equals(new Move(0, 0, new Position(-2, -1), new Position(-1, -1), null, b)));
        assertFalse(test.equals(new Move(0, 0, new Position(-1, -1), new Position(-1, -2), null, b)));
        assertFalse(test.equals(new Move(0, 0, new Position(-1, -1), new Position(-1, -1), null, null)));

    }
}
