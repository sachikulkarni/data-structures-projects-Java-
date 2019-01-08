package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Iterator;

/** The suite of all JUnit tests for the enigma package.
 *  @author Sachi Kulkarni
 */
public class UnitTest {

    /**
     * Run the JUnit tests in this package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }
    /**
     * A dummy test as a placeholder for real ones.
     */

    @Test
    public void dummyTest() {
        assertFalse("There are no unit tests!", false);
    }

    /**
     * Tests basic correctness of put and get on the initialized board.
     */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        assertEquals(Square.sq("c2"), Square.sq(2, 1));
        assertEquals(Square.sq("f", "2"),
                Square.sq(5, 1));
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /**
     * Tests proper identification of legal/illegal queen moves.
     */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(4, 5).isQueenMove(Square.sq(4, 6)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    /**
     * Tests toString for initial board state and a smiling board state. :)
     */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }
    public void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }
    @Test
    public void testDirection() {
        Board b = new Board();
        assertEquals(Square.sq(11).direction(Square.sq(0)), 5);
        assertEquals(Square.sq(11).direction(Square.sq(1)), 4);
        assertEquals(Square.sq(11).direction(Square.sq(2)), 3);
        assertEquals(Square.sq(11).direction(Square.sq(10)), 6);
        assertEquals(Square.sq(11).direction(Square.sq(12)), 2);
        assertEquals(Square.sq(11).direction(Square.sq(20)), 7);
        assertEquals(Square.sq(11).direction(Square.sq(21)), 0);
        assertEquals(Square.sq(11).direction(Square.sq(22)), 1);
    }
    @Test
    public void testLegal() {
        Board b = new Board();
        b.put(BLACK, 4, 5);
        b.put(WHITE, 3, 4);
        assertFalse(b.isUnblockedMove(Square.sq(3, 4),
                Square.sq(5, 6), Square.sq(3, 4)));
        assertTrue(b.isLegal(Square.sq(3, 4)));
        assertFalse(b.isLegal(Square.sq(4, 5)));
        assertFalse(b.isLegal(Square.sq(3, 4), Square.sq(5, 6)));
        assertFalse(b.isLegal(Square.sq(4, 5), Square.sq(2, 9)));
        assertTrue(b.isLegal(Square.sq(3, 4),
                Square.sq(2, 5), Square.sq(3, 4)));
        Board c = new Board();
        c.put(WHITE, 3, 0);
        assertTrue(c.isUnblockedMove(Square.sq(3, 0),
                Square.sq(3, 1), Square.sq(3, 3)));

    }
    @Test
    public void testUnblockedMove() {
        Board b = new Board();
        b.put(BLACK, Square.sq("d1"));
        b.put(WHITE, Square.sq("d3"));
        assertFalse(b.isUnblockedMove(Square.sq("d1"),
                Square.sq("d5"), Square.sq("d1")));
        assertTrue(b.isUnblockedMove(Square.sq("d1"),
                Square.sq("f3"), Square.sq("d2")));
        b.put(WHITE, Square.sq("d3"));
        assertFalse(b.isUnblockedMove(Square.sq("b1"),
                Square.sq("e4"), Square.sq("d2")));
    }
    @Test
    public void testMove() {
        Board b = new Board();
        b.put(WHITE, 3, 4);
        b.put(BLACK, 4, 5);
        b.put(WHITE, 3, 5);
        b.makeMove(Square.sq(3, 4), Square.sq(2, 5), Square.sq(3, 4));
        assertEquals(b.get(2, 5), WHITE);
        assertEquals(b.get(3, 4), SPEAR);
    }
    @Test
    public void testUndo() {
        Board b = new Board();
        b.put(WHITE, 0, 0);
        b.makeMove(Square.sq(0, 0), Square.sq(2, 2), Square.sq(3, 3));
        assertEquals(b.get(Square.sq(2, 2)), WHITE);
        assertEquals(b.get(Square.sq(0, 0)), EMPTY);
        b.undo();
        assertEquals(b.get(Square.sq(0, 0)), WHITE);
        assertEquals(b.get(Square.sq(2, 2)), EMPTY);
    }
    @Test
    public void testReachable() {
        Board b = new Board();
        b.put(WHITE, 3, 4);
        b.put(WHITE, 3, 5);
        b.put(WHITE, 4, 4);
        b.put(WHITE, 1,  8);
        b.put(WHITE, 5, 4);
        b.put(WHITE, 6, 5);
        b.put(WHITE, 5, 6);
        b.put(WHITE, 4, 7);
        Iterator<Square> reach = b.reachableFrom(Square.sq(4, 5), null);
        assertEquals(reach.next(), Square.sq(4, 6));
        assertEquals(reach.next(), Square.sq(5, 5));
        assertEquals(reach.next(), Square.sq(3, 6));
        assertEquals(reach.next(), Square.sq(2, 7));
        assertFalse(reach.hasNext());
    }
    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S - S - - S - S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";

}


