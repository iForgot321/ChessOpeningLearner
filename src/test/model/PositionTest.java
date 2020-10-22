package model;

import model.board.Position;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class PositionTest {
    Position pos = new Position(4, 5);

    @Test
    void testGetRow() {
        assertEquals(4, pos.getRow());
    }

    @Test
    void testGetCol() {
        assertEquals(5, pos.getCol());
    }

    @Test
    void testEquals() {
        Position pos2 = new Position(pos);
        Position pos3 = new Position(3,5);
        Position pos4 = new Position(4,3);
        Position pos5 = null;

        assertTrue(pos.equals(pos2));
        assertFalse(pos.equals(pos3));
        assertFalse(pos.equals(pos4));
        assertFalse(pos.equals(pos5));
    }

    @Test
    void testToNotation() {
        assertEquals("f4", pos.toChessNotation());
    }

    @Test
    void testNotationToPosition() {
        assertTrue(pos.equals(Position.notationToPosition("f4")));
        assertFalse(pos.equals(Position.notationToPosition("f45")));
        assertFalse(pos.equals(Position.notationToPosition("fg")));
        assertFalse(pos.equals(Position.notationToPosition("k4")));
        assertFalse(pos.equals(Position.notationToPosition("f9")));
        assertFalse(pos.equals(Position.notationToPosition("A4")));
        assertFalse(pos.equals(Position.notationToPosition("f0")));
        assertFalse(pos.equals(Position.notationToPosition("f5")));
    }

    @Test
    void testToJson() {
        JSONObject json = pos.toJson();
        assertEquals(json.getInt("row"), 4);
        assertEquals(json.getInt("col"), 5);
    }
}
