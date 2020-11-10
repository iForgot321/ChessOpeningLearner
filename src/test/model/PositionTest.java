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

        assertEquals(pos, pos);
        assertEquals(pos2, pos);
        assertNotEquals(pos3, pos);
        assertNotEquals(pos4, pos);
        assertNotEquals(pos, null);
        assertNotEquals(pos, new Object());
    }

    @Test
    void testHashCode() {
        assertEquals(1120, pos.hashCode());
    }

    @Test
    void testToNotation() {
        assertEquals("f4", pos.toChessNotation());
    }

    @Test
    void testNotationToPosition() {
        assertEquals(Position.notationToPosition("f4"), pos);
        assertNotEquals(Position.notationToPosition("f45"), pos);
        assertNotEquals(Position.notationToPosition("fg"), pos);
        assertNotEquals(Position.notationToPosition("k4"), pos);
        assertNotEquals(Position.notationToPosition("f9"), pos);
        assertNotEquals(Position.notationToPosition("A4"), pos);
        assertNotEquals(Position.notationToPosition("f0"), pos);
        assertNotEquals(Position.notationToPosition("f5"), pos);
    }

    @Test
    void testIsValidPosition() {
        assertTrue(pos.isValid());
        assertFalse(new Position(-1, 5).isValid());
        assertFalse(new Position(5, 10).isValid());
        assertFalse(new Position(10, 5).isValid());
        assertFalse(new Position(5, -1).isValid());
    }

    @Test
    void testIsValidRowCol() {
        assertTrue(Position.isValid(5, 4));
        assertFalse(Position.isValid(10, 5));
        assertFalse(Position.isValid(-1, 5));
        assertFalse(Position.isValid(5, -1));
        assertFalse(Position.isValid(5, 10));
    }

    @Test
    void testToJson() {
        JSONObject json = pos.toJson();
        assertEquals(json.getInt("row"), 4);
        assertEquals(json.getInt("col"), 5);
    }
}
