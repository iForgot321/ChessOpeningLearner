package model;

import model.board.Position;
import model.board.Board;

import static model.board.Board.*;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONArray;
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

        assertEquals(m, test.getChildMove(0));
    }

    @Test
    void testAddMove() {
        assertTrue(test.addChildMove(test2));
        assertTrue(test.addChildMove(test3));
        assertEquals(2, test.childCount());
        assertFalse(test.addChildMove(test3));

        assertTrue(test.addChildMove(test4));
        assertFalse(test.addChildMove(test4));
    }

    @Test
    void testRemoveMove() {
        test.addChildMove(test2);
        test.removeChildMove(0);
        assertEquals(0, test.childCount());

        test.addChildMove(test2);
        test.removeChildMove(test2);
        assertEquals(0, test.childCount());
    }

    @Test
    void testLength() {
        assertEquals(0, test.childCount());
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
    void testGetIndexOfChildFromPos() {
        test.addChildMove(test2);
        test.addChildMove(test3);
        assertEquals(0, test.getIndexOfChild(test2.getStart(), test2.getEnd()));
        assertEquals(1, test.getIndexOfChild(test3.getStart(), test3.getEnd()));
        assertEquals(-1, test.getIndexOfChild(test4.getStart(), test4.getEnd()));
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
        assertEquals(new Position(6, 4), test2.getStart());
    }

    @Test
    void testGetEnd() {
        assertEquals(new Position(4, 4), test2.getEnd());
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

        assertEquals(m, test2.getParentMove());
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
        assertEquals(m, test);

        assertNotEquals(test, null);
        assertNotEquals(test2, test);
        assertNotEquals(new Move(0, K, false, false, new Position(-1, -1), new Position(-1, -1), null, b), test);
        assertNotEquals(new Move(0, 0, false, false, new Position(-2, -1), new Position(-1, -1), null, b), test);
        assertNotEquals(new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -2), null, b), test);
        assertNotEquals(test, new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -1), null, null));
    }

    @Test
    void testHashCode() {
        assertEquals(925573633, test.hashCode());
    }

    @Test
    void testNoPieceAtStartPos() {
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
        Move m = new Move(0, 0, false, false, new Position(5, 5), new Position(5, 6), null, b);
        assertEquals(-1, m.isLegal());
    }

    @Test
    void testPawnIsLegal() {
        int[][] board = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, E, -P},
                {E, P, E, E, E, E, E, E},
                {P, E, E, E, E, E, E, E},
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
        Move m7 = new Move(1, -P, false, false, new Position(1, 4), new Position(4, 4), null, b);
        Move m8 = new Move(1, -P, false, false, new Position(1, 0), new Position(3, 0), null, b);
        Move m9 = new Move(1, -P, false, false, new Position(1, 1), new Position(3, 1), null, b);
        Move m10 = new Move(1, -P, false, false, new Position(6, 6), new Position(5, 6), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(0, m2.isLegal());
        assertEquals(0, m3.isLegal());
        assertEquals(-1, m4.isLegal());
        assertEquals(-1, m5.isLegal());
        assertEquals(0, m6.isLegal());
        assertEquals(-1, m7.isLegal());
        assertEquals(-1, m8.isLegal());
        assertEquals(-1, m9.isLegal());
        assertEquals(-1, m10.isLegal());
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
        Move m5 = new Move(1, -P, false, false, new Position(3, 3), new Position(5, 2), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(0, m2.isLegal());
        assertEquals(-1, m3.isLegal());
        assertEquals(-1, m4.isLegal());
        assertEquals(-1, m5.isLegal());
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
        Move m4 = new Move(2, P, false, false, new Position(6, 5), new Position(5, 4), before, b);
        Move m5 = new Move(2, P, false, false, new Position(3, 6), new Position(2, 5), before, b);
        Move wrongBefore = new Move(1, -K, false, false, new Position(1, 3), new Position(3, 3), null, b);
        Move m6 = new Move(2, P, false, false, new Position(3, 4), new Position(2, 3), wrongBefore, b);
        Move wrongBefore2 = new Move(1, -P, false, false, new Position(2, 3), new Position(3, 3), null, b);
        Move m7 = new Move(2, P, false, false, new Position(3, 4), new Position(2, 3), wrongBefore2, b);
        Move wrongBefore3 = new Move(1, -P, false, false, new Position(1, 3), new Position(4, 3), null, b);
        Move m8 = new Move(2, P, false, false, new Position(3, 4), new Position(2, 3), wrongBefore3, b);
        assertEquals(3, m.isLegal());
        assertEquals(-1, m2.isLegal());
        assertEquals(-1, m4.isLegal());
        assertEquals(-1, m5.isLegal());
        assertEquals(-1, m6.isLegal());
        assertEquals(-1, m7.isLegal());
        assertEquals(-1, m8.isLegal());

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
        int[][] board = {{-R, E, E, E, E, E, E, -R},
                {E, E, E, E, E, E, E, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {R, E, E, E, E, E, E, R}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, R, false, false, new Position(7, 7), new Position(1, 7), null, b);
        Move m2 = new Move(1, R, false, false, new Position(7, 7), new Position(0, 7), null, b);
        Move m3 = new Move(1, R, false, false, new Position(7, 7), new Position(7, 4), null, b);
        Move m4 = new Move(1, R, false, false, new Position(7, 7), new Position(5, 6), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(-1, m2.isLegal());
        assertEquals(0, m3.isLegal());
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
        Move m3 = new Move(1, Q, false, false, new Position(4, 3), new Position(5, 6), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(0, m2.isLegal());
        assertEquals(-1, m3.isLegal());
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
                {R, N, B, Q, K, E, E, E}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, K, false, false, new Position(7, 4), new Position(6, 3), null, b);
        Move m2 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 3), null, b);
        Move m3 = new Move(1, K, false, false, new Position(7, 4), new Position(5, 4), null, b);
        Move m4 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 7), null, b);
        assertEquals(0, m.isLegal());
        assertEquals(-1, m2.isLegal());
        assertEquals(-1, m3.isLegal());
        assertEquals(-1, m4.isLegal());
    }

    @Test
    void testCastle() {
        int[][] board = {{-R, E, E, E, -K, E, E, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, P, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, E, E, P, P, E},
                {R, E, E, E, K, E, E, R}};
        int[][] board2 = {{-R, E, E, -P, -K, -P, E, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, P, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, E, E, P, P, E},
                {R, E, E, B, K, P, E, R}};
        int[][] board3 = {{-R, E, N, E, -K, E, N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, P, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, E, E, P, P, E},
                {R, E, -N, E, K, E, -N, R}};
        int[][] board4 = {{-R, -P, E, E, -K, E, -P, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, P, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, E, E, P, P, E},
                {R, P, E, E, K, E, P, R}};
        boolean[] moved = {true, true, true, false, false, false};
        boolean[] moved2 = {true, false, true, true, true, true};

        Board b = new Board(new boolean[6], board);
        Board b2 = new Board(moved, board);
        Board b3 = new Board(moved2, board);
        Board b4 = new Board(new boolean[6], board2);
        Board b5 = new Board(new boolean[6], board3);
        Board b6 = new Board(new boolean[6], board4);

        Move m1 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 2), null, b);
        Move m2 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 6), null, b);
        Move m3 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 6), null, b2);
        Move m4 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 2), null, b2);
        Move m5 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 2), null, b5);
        Move m6 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 6), null, b5);
        Move m7 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 2), null, b6);
        Move m8 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 2), null, b3);
        Move m9 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 6), null, b3);
        Move m10 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 2), null, b4);
        Move m11 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 6), null, b4);

        assertEquals(2, m1.isLegal());
        assertEquals(2, m2.isLegal());
        assertEquals(-1, m3.isLegal());
        assertEquals(-1, m4.isLegal());
        assertEquals(-1, m5.isLegal());
        assertEquals(-1, m6.isLegal());
        assertEquals(-1, m7.isLegal());
        assertEquals(-1, m8.isLegal());
        assertEquals(-1, m9.isLegal());
        assertEquals(-1, m10.isLegal());
        assertEquals(-1, m11.isLegal());

    }

    @Test
    void testCastleInCheck() {
        int[][] board = {{-R, E, E, E, -K, -R, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, R, K, E, E, R}};
        int[][] board2 = {{-R, E, E, E, -R, -K, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, R, K, E, E, R}};
        Board b = new Board(new boolean[6], board);
        Board b2 = new Board(new boolean[6], board2);
        Move m = new Move(1, K, false, false, new Position(7, 4), new Position(7, 6), null, b);
        Move m2 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 2), null, b);
        Move m3 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 6), null, b2);
        Move m4 = new Move(1, -K, false, false, new Position(0, 4), new Position(0, 2), null, b);
        assertEquals(-1, m.isLegal());
        assertEquals(-1, m2.isLegal());
        assertEquals(-1, m3.isLegal());
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
        int[][] board = {{-R, E, E, E, -K, -B, E, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, P, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, E, E, P, P, E},
                {R, E, E, E, K, E, E, R}};
        Board b = new Board(new boolean[6], board);
        Move m = new Move(1, K, false, false, new Position(7, 4), new Position(7, 6), test, b);
        Move m2 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 2), test, b);
        Move m3 = new Move(1, K, false, false, new Position(7, 4), new Position(7, 3), test, b);

        assertEquals("e4", test2.toChessNotation());
        assertEquals("Nf3", test4.toChessNotation());
        assertEquals("Opening List", test.toChessNotation());
        assertEquals("0-0", m.toChessNotation());
        assertEquals("0-0-0", m2.toChessNotation());
        assertEquals("Kd1", m3.toChessNotation());

        assertEquals("e4", test2.toString());
        assertEquals("Nf3", test4.toString());
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
    void testGetPath() {
        Object[] objArray = test.getPath();
        Object[] objArray2 = test4.getPath();
        assertEquals(objArray, null);
        assertEquals(2, objArray2.length);
        assertEquals(test, objArray2[0]);
        assertEquals(test4, objArray2[1]);
    }

    @Test
    void testToJson() {
        test.addChildMove(test2);
        test.addChildMove(test3);
        JSONObject json = test.toJson();

        assertEquals(test.getMoveNum(), json.getInt("moveNum"));
        assertEquals(test.getPiece(), json.getInt("piece"));
        assertEquals(test.isCaptures(), json.getBoolean("isCaptures"));
        assertEquals(test.childCount(), json.getJSONArray("childMoves").length());
    }
}
