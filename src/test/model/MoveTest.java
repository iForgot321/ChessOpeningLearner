package model;

import model.board.Position;
import model.board.Board;

import static model.board.Board.*;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
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
        test = new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -1), null, b);

        int[][] board2 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b2 = new Board(moved, board2);
        test2 = new Move(1, P, false, false, new Position(6, 4), new Position(4, 4), test, b2);

        int[][] board3 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, P, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b3 = new Board(moved, board3);
        test3 = new Move(1, P, false, false, new Position(6, 4), new Position(5, 4), test, b3);

        int[][] board4 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, N, E, E},
                {P, P, P, P, P, P, P, P},
                {R, N, B, Q, K, B, E, R}};
        Board b4 = new Board(moved, board4);
        test4 = new Move(1, N, false, false, new Position(7, 6), new Position(5, 5), test, b4);
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
        Board b2 = new Board(new boolean[6], board2);
        Move m = new Move(1, P, false, false, new Position(6, 4), new Position(4, 4), test, b2);

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
        test.removeChildMove(0);
        assertEquals(0, test.length());
    }

    @Test
    void testLength() {
        assertEquals(0, test.length());
    }

    @Test
    void testGetIndexOfChild() {
        test.addChildMove(test2);
        test.addChildMove(test3);
        assertEquals(0, test.getIndexOfChild(test2));
        assertEquals(1, test.getIndexOfChild(test3));
        assertEquals(-1, test.getIndexOfChild(test4));
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
    void testIsCaptures() {
        assertFalse(test.isCaptures());
    }

    @Test
    void testIsCheck() {
        assertFalse(test.isCheck());
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
        Board b = new Board(new boolean[6], board);
        Move m = new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -1), null, b);

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
        Board b = new Board(new boolean[6], board);

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
        Board b = new Board(new boolean[6], board);

        test.addChildMove(test2);
        Move m = new Move(test);
        assertTrue(test.equals(m));

        assertFalse(test.equals(null));
        assertFalse(test.equals(test2));
        assertFalse(test.equals(new Move(0, K, false, false, new Position(-1, -1), new Position(-1, -1), null, b)));
        assertFalse(test.equals(new Move(0, 0, false, false, new Position(-2, -1), new Position(-1, -1), null, b)));
        assertFalse(test.equals(new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -2), null, b)));
        assertFalse(test.equals(new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -1), null, null)));
    }

    @Test
    void testPawnIsLegal() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, E, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, P, E, E, E, E, -P, E},
                {P, E, P, P, P, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, P, false, false, new Position(6, 4), new Position(5, 4), null, b);
        Move m2 = new Move(1, P, false, false, new Position(6, 4), new Position(4, 4), null, b);
        Move m3 = new Move(1, -P, false, false, new Position(1, 4), new Position(2, 4), null, b);
        Move m4 = new Move(1, P, false, false, new Position(5, 1), new Position(3, 1), null, b);
        Move m5 = new Move(1, -P, false, false, new Position(5, 6), new Position(6, 6), null, b);
        Move m6 = new Move(1, -P, false, false, new Position(1, 4), new Position(3, 4), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(0, m2.isLegal());
        assertEquals(0, m3.isLegal());
        assertEquals(-1, m4.isLegal());
        assertEquals(-1, m5.isLegal());
        assertEquals(0, m6.isLegal());
    }

    @Test
    void testPawnCapture() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, E, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, -P, E, E, E, E},
                {E, E, E, E, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, P, true, false, new Position(4, 4), new Position(3, 3), null, b);
        Move m2 = new Move(1, -P, true, false, new Position(3, 3), new Position(4, 4), null, b);
        Move m3 = new Move(1, P, false, false, new Position(4, 4), new Position(3, 6), null, b);
        Move m4 = new Move(1, -P, false, false, new Position(3, 3), new Position(4, 2), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(0, m2.isLegal());
        assertEquals(-1, m3.isLegal());
        assertEquals(-1, m4.isLegal());
    }

    @Test
    void testPawnPromotion() {
        int[][] board = {{-R, E, E, E, E, E, E, E},
                {E, P, E, E, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, -P, E, E, E},
                {E, E, E, E, E, E, E, E}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, P, false, false, new Position(1, 4), new Position(0, 4), null, b);
        Move m2 = new Move(1, -P, false, false, new Position(6, 4), new Position(7, 4), null, b);
        Move m3 = new Move(1, P, false, false, new Position(1, 1), new Position(0, 0), null, b);
        assertEquals(1, m.isLegal());
        assertEquals(1, m2.isLegal());
        assertEquals(1, m3.isLegal());
    }

    @Test
    void testEnPassant() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, E, -P, -P, -P, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, -P, P, E, P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, E, P, E, P},
                {R, N, B, Q, K, B, N, R}};
        Board b = new Board(new boolean[6], board);
        Move before = new Move(1, -P, false, false, new Position(1, 3), new Position(3, 3), null, b);
        Move m = new Move(2, P, false, false, new Position(3, 4), new Position(2, 3), before, b);
        Move m2 = new Move(2, P, false, false, new Position(3, 6), new Position(2, 7), before, b);
        assertEquals(3, m.isLegal());
        assertEquals(-1, m2.isLegal());

        int[][] board2 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, E, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, -P, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b2 = new Board(new boolean[6], board2);
        Move before2 = new Move(1, P, false, false, new Position(6, 4), new Position(4, 4), null, b2);
        Move m3 = new Move(2, -P, false, false, new Position(4, 3), new Position(5, 4), before2, b2);
        assertEquals(3, m3.isLegal());
    }

    @Test
    void testKnightIsLegal() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, P, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, N, false, false, new Position(7, 6), new Position(5, 5), null, b);
        Move m2 = new Move(1, N, false, false, new Position(7, 6), new Position(5, 6), null, b);
        Move m3 = new Move(1, N, false, false, new Position(7, 6), new Position(6, 5), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(-1, m2.isLegal());
        assertEquals(-1, m3.isLegal());
    }

    @Test
    void testBishopIsLegal() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {E, -P, -P, -P, -P, -P, -P, -P},
                {-P, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, P, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, B, true, false, new Position(7, 5), new Position(2, 0), null, b);
        Move m2 = new Move(1, B, false, false, new Position(7, 5), new Position(6, 6), null, b);
        Move m3 = new Move(1, B, false, false, new Position(7, 5), new Position(5, 7), null, b);
        Move m4 = new Move(1, B, false, false, new Position(7, 5), new Position(5, 6), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(-1, m2.isLegal());
        assertEquals(-1, m3.isLegal());
        assertEquals(-1, m4.isLegal());
    }

    @Test
    void testRookIsLegal() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, P, P, P, E},
                {R, N, B, Q, K, B, N, R}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, R, false, false, new Position(7, 7), new Position(1, 7), null, b);
        Move m2 = new Move(1, R, false, false, new Position(7, 7), new Position(0, 7), null, b);
        Move m3 = new Move(1, R, false, false, new Position(7, 7), new Position(7, 4), null, b);
        Move m4 = new Move(1, R, false, false, new Position(7, 7), new Position(5, 6), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(-1, m2.isLegal());
        assertEquals(-1, m3.isLegal());
        assertEquals(-1, m4.isLegal());
    }

    @Test
    void testQueenIsLegal() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, Q, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, P, P, P, E},
                {R, N, B, E, K, B, N, R}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, Q, false, false, new Position(4, 3), new Position(1, 3), null, b);
        Move m2 = new Move(1, Q, false, false, new Position(4, 3), new Position(1, 0), null, b);
        Move m3 = new Move(1, Q, false, false, new Position(4, 3), new Position(7, 4), null, b);
        Move m4 = new Move(1, Q, false, false, new Position(4, 3), new Position(6, 1), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(0, m2.isLegal());
        assertEquals(-1, m3.isLegal());
        assertEquals(-1, m4.isLegal());
    }

    @Test
    void testKingIsLegal() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, P, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, E, E, P, P, E},
                {R, N, B, Q, K, B, N, R}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, K, false, false, new Position(7, 4), new Position(6, 3), null, b);
        Move m2 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 3), null, b);
        Move m3 = new Move(1, K, false, false, new Position(7, 4), new Position(5, 4), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(-1, m2.isLegal());
        assertEquals(-1, m3.isLegal());
    }

    @Test
    void testCastle() {
        int[][] board = {{-R, E, E, E, -K, -B, E, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, P, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, E, E, P, P, E},
                {R, E, E, E, K, E, E, R}};
        boolean[] moved = {false, false, true, false, false, false};
        Board b = new Board(moved, board);
        Move m = new Move(1, K, false, false, new Position(7, 4), new Position(7, 6), null, b);
        Move m2 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 2), null, b);
        Move m3 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 2), null, b);
        Move m4 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 6), null, b);
        assertEquals(2, m.isLegal());
        assertEquals(-1, m2.isLegal());
        assertEquals(2, m3.isLegal());
        assertEquals(-1, m4.isLegal());
    }

    @Test
    void testIllegalMoveInCheck() {
        int[][] board = {{E, -K, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, -Q, E, E, E, E, E, E},
                {E, E, E, E, E, K, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, N, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, R, E, E, E, E, E, E}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, -Q, false, false, new Position(2, 1), new Position(7, 1), null, b);
        Move m2 = new Move(1, -Q, false, false, new Position(2, 1), new Position(5, 4), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(-1, m2.isLegal());
    }

    @Test
    void testToChessNotation() {
        assertEquals("e4", test2.toChessNotation());
        assertEquals("Nf3", test4.toChessNotation());
        assertEquals("Opening List", test.toChessNotation());
    }

    @Test
    void testCaptureToNotation() {
        int[][] example = {{E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, -K, E, E, E, E, E, E},
                {E, E, E, E, -P, E, E, E},
                {P, E, E, P, E, P, E, E},
                {E, E, E, E, E, E, E, E},
                {E, -N, E, E, E, E, E, E},
                {E, E, E, -B, E, E, E, E}};
        Board test = new Board(new boolean[6], example);
        Move m = new Move(1, -N, true, false, new Position(6, 1), new Position(4, 0), test2, test);
        Move m2 = new Move(1, P, true, false, new Position(4, 5), new Position(3, 4), test2, test);
        assertEquals("Nxa4", m.toChessNotation());
        assertEquals("fxe5", m2.toChessNotation());
    }

    @Test
    void testCheckToNotation() {
        int[][] example = {{E, E, E, E, E, E, E, E},
                {E, K, E, E, E, E, -Q, E},
                {E, E, E, E, E, E, E, E},
                {-K, E, E, E, -B, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, Q, E, E, E},
                {E, E, E, E, E, E, E, E}};
        Board test = new Board(new boolean[6], example);
        Move m = new Move(1, Q, false, true, new Position(6, 4), new Position(7, 4), test2, test);
        Move m2 = new Move(1, Q, true, true, new Position(6, 4), new Position(3, 4), test2, test);
        assertEquals("Qe1+", m.toChessNotation());
        assertEquals("Qxe5+", m2.toChessNotation());
    }

    @Test
    void testLineToString() {
        int[][] board2 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, E, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, -P, E, E, E},
                {E, E, E, E, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b2 = new Board(new boolean[6], board2);

        Move m = new Move(2, -P, false, false, new Position(1, 4), new Position(3, 4), test2, b2);
        assertEquals("  1. e4 e5", m.lineToString());
    }

    @Test
    void testToJson() {
        JSONObject json = test.toJson();

        assertEquals(test.getMoveNum(), json.getInt("moveNum"));
        assertEquals(test.getPiece(), json.getInt("piece"));
        assertEquals(test.isCaptures(), json.getBoolean("isCaptures"));
        assertEquals(test.length(), json.getJSONArray("childMoves").length());
    }
}
