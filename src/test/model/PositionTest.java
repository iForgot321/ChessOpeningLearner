package model;

import model.board.Position;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PositionTest {
    Position pos;

    @BeforeEach
    void runBefore() {
        pos = new Position(4, 5);
    }

    @Test
    void testGetCol() {
        assertEquals(4, pos.getCol());
    }

    @Test
    void testGetRow() {
        assertEquals(5, pos.getRow());
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
}
