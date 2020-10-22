package model;

import model.board.Position;
import model.board.Board;
import static model.board.Board.*;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {
    Board test;
    int[][] board2 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
            {-P, -P, -P, -P, -P, -P, -P, -P},
            {E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E},
            {P, P, P, P, P, P, P, P},
            {R, N, B, Q, K, B, N, R}};

    @BeforeEach
    void runBefore() {
        int[][] board = {{-R, E, E, E, -K, E, E, E},
                {E, E, E, E, E, E, E, P},
                {E, E, -N, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, -P, P, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, E, P, P, P, P},
                {R, E, E, E, K, E, E, R}};
        boolean[] moved = new boolean[6];
        test = new Board(moved, board);
    }

    @Test
    void testGet() {
        assertEquals(-R, test.get(0, 0));
    }

    @Test
    void testGetMoved() {
        assertFalse(test.getMoved(3));
    }

    @Test
    void testMove() {
        Board res = test.move(new Position(2, 2), new Position(3, 4));
        assertEquals(E, res.get(2, 2));
        assertEquals(-N, res.get(3, 4));
    }

    @Test
    void testPromote() {
        Board res = test.promotePawn(new Position(1, 7), new Position(0, 7), Q);
        assertEquals(E, res.get(1, 7));
        assertEquals(Q, res.get(0, 7));
    }

    @Test
    void testCastle() {
        Board res = test.castle(false, false);
        assertEquals(E, res.get(0, 0));
        assertEquals(E, res.get(0, 4));
        assertEquals(-K, res.get(0, 2));
        assertEquals(-R, res.get(0, 3));

        res = test.castle(true, true);
        assertEquals(E, res.get(7, 7));
        assertEquals(E, res.get(7, 4));
        assertEquals(K, res.get(7, 6));
        assertEquals(R, res.get(7, 5));
    }

    @Test
    void testEnPassant() {
        Board res = test.enPassant(new Position(4, 2), new Position(5, 3));
        assertEquals(E, res.get(4, 2));
        assertEquals(E, res.get(4, 3));
        assertEquals(-P, res.get(5, 3));
    }

    @Test
    void testEquals() {
        Board test2 = new Board(new boolean[6], board2);
        Board test3 = new Board(test);
        Board test4 = null;
        Board test5 = new Board(new boolean[]{false, true, false, false, false, false}, board2);

        assertTrue(test.equals(test3));
        assertFalse(test.equals(test2));
        assertFalse(test.equals(test4));
        assertFalse(test.equals(test5));
    }

    @Test
    void testStringToPiece() {
        assertEquals(P, stringToPiece(""));
        assertEquals(N, stringToPiece("N"));
        assertEquals(B, stringToPiece("B"));
        assertEquals(R, stringToPiece("R"));
        assertEquals(Q, stringToPiece("Q"));
        assertEquals(K, stringToPiece("K"));
        assertEquals(-1, stringToPiece("T"));
    }

    @Test
    void testToJson() {
        JSONObject json = test.toJson();
        JSONArray jsonMovePieces = json.getJSONArray("movedPieces");
        boolean[] movedPieces = new boolean[jsonMovePieces.length()];
        for (int i = 0; i < jsonMovePieces.length(); i++) {
            movedPieces[i] = jsonMovePieces.getBoolean(i);
        }

        JSONArray jsonBoard = json.getJSONArray("pieceBoard");
        int[][] board = new int[jsonBoard.length()][jsonBoard.length()];
        for (int i = 0; i < jsonBoard.length(); i++) {
            JSONArray rows = jsonBoard.getJSONArray(i);
            for (int j = 0; j < jsonBoard.length(); j++) {
                board[i][j] = rows.getInt(j);
            }
        }

        assertTrue(test.equals(new Board(movedPieces, board)));
    }
}
