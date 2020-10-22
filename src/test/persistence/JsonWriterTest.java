package persistence;

import model.Move;
import model.board.Position;
import model.board.Board;
import static model.board.Board.*;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JsonWriterTest {
    Move m;
    Move m2;
    Move m3;

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
        m = new Move(0, 0, false, new Position(-1, -1), new Position(-1, -1), null, b);

        int[][] board2 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, P, E, E, E},
                {E, E, E, E, E, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b2 = new Board(moved, board2);
        m2 = new Move(1, P, false, new Position(6, 4), new Position(4, 4), m, b2);

        int[][] board3 = {{-R, -N, -B, -Q, -K, -B, -N, -R},
                {-P, -P, -P, -P, -P, -P, -P, -P},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E},
                {E, E, E, E, P, E, E, E},
                {P, P, P, P, E, P, P, P},
                {R, N, B, Q, K, B, N, R}};
        Board b3 = new Board(moved, board3);
        m3 = new Move(1, P, false, new Position(6, 4), new Position(5, 4), m, b3);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // expected result
        }
    }

    @Test
    void testWriterEmptyMove() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMove.json");
            writer.open();
            writer.write(m);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMove.json");
            Move test = reader.read();
            assertTrue(m.equals(test));
        } catch (IOException e) {
            fail("Couldn't write to file");
        }
    }

    @Test
    void testWriterMoveList() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterMoveList.json");

            m.addChildMove(m2);
            m.addChildMove(m3);

            writer.open();
            writer.write(m);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterMoveList.json");
            Move test = reader.read();
            assertTrue(m.equals(test));
        } catch (IOException e) {
            fail("Couldn't write to file");
        }
    }
}
