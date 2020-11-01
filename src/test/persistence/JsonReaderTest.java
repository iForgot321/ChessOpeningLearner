package persistence;

import model.Move;
import model.board.Position;
import model.board.Board;
import static model.board.Board.*;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JsonReaderTest {

    @Test
    void testReaderNoFile() {
        JsonReader reader = new JsonReader("./data/nothing.json");
        try {
            Move m = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // expected result
        }
    }

    @Test
    void testReaderEmptyFile() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFile.json");
        try {
            Move m = reader.read();
            fail("IOException expected");
        } catch (JSONException e) {
            // expected result
        } catch (Exception e) {
            fail("Wrong exception made");
        }
    }

    @Test
    void testReaderEmptyMoveList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyMoves.json");

        try {
            Move m = reader.read();

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

            assertTrue(m.equals(new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -1), null, b)));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderMoveList() {
        JsonReader reader = new JsonReader("./data/testReaderMoveList.json");
        try {
            Move m = reader.read();

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

            int[][] board2 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                    {-P, -P, -P, -P, -P, -P, -P, -P},
                    {E, E, E, E, E, E, E, E},
                    {E, E, E, E, E, E, E, E},
                    {E, E, E, E, P, E, E, E},
                    {E, E, E, E, E, E, E, E},
                    {P, P, P, P, E, P, P, P},
                    {R, N, B, Q, K, B, N, R}};
            Board b2 = new Board(moved, board2);

            int[][] board3 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                    {-P, -P, -P, -P, -P, -P, -P, -P},
                    {E, E, E, E, E, E, E, E},
                    {E, E, E, E, E, E, E, E},
                    {E, E, E, E, E, E, E, E},
                    {E, E, E, E, E, N, E, E},
                    {P, P, P, P, P, P, P, P},
                    {R, N, B, Q, K, B, E, R}};
            Board b3 = new Board(moved, board3);

            assertTrue(m.equals(new Move(0, 0, false, false, new Position(-1, -1), new Position(-1, -1), null, b)));
            assertTrue(m.getChildMove(0).equals(new Move(1, P, false, false, new Position(6, 4), new Position(4, 4), m, b2)));
            assertTrue(m.getChildMove(1).equals(new Move(1, N, false, false, new Position(7, 6), new Position(5, 5), m, b3)));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
