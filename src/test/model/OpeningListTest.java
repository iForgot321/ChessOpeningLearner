package model;

import model.board.Position;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OpeningListTest {
    OpeningList test1;

    @BeforeEach
    void runBefore(){
        test1 = new OpeningList();
        test1.addOpening(new Move(1,true, 1, new Position(5, 2), new Position(5, 4)));
        test1.addOpening(new Move(1,true, 1, new Position(5, 2), new Position(5, 3)));
    }

    @Test
    void testGetOpening() {
        Move m = test1.getOpening(0);
        assertEquals(1, m.getMoveNum()); // use Move.equals
        assertEquals(true, m.isWhite());
        assertEquals(1, m.getPiece());
        assertTrue(m.getStart().equals(new Position(5, 2)));
        assertTrue(m.getEnd().equals(new Position(5, 4)));
    }

    @Test
    void testAddOpening() {
        assertFalse(test1.addOpening(new Move(1,true, 1, new Position(5, 2), new Position(5, 4))));
        assertTrue(test1.addOpening(new Move(1,true, 1, new Position(4, 2), new Position(4, 3))));
        assertTrue(test1.addOpening(new Move(1,true, 2, new Position(5, 2), new Position(5, 4))));
        assertEquals(4,test1.length());
    }

    @Test
    void testRemoveOpening() {
        assertFalse(test1.removeOpening(1, new Position(4, 3)));
        assertFalse(test1.removeOpening(3, new Position(5, 3)));

        assertTrue(test1.removeOpening(1, new Position(5, 4)));
        assertEquals(1, test1.length());
    }

    @Test
    void testLength() {
        assertEquals(2, test1.length());
    }
}
