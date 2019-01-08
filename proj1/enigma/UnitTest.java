package enigma;

import org.junit.Test;
import ucb.junit.textui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/** The suite of all JUnit tests for the enigma package.
 *  @Sachi Kulkarni
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(PermutationTest.class, MovingRotorTest.class);
    }

    /** Helper method to get the String representation
     *  of the current Rotor settings -From LAB 8 */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation(
                "(PNH) (ABDFIKLZYXW) "
                        + "(JC)", new CharacterRange('A', 'Z'));
        assertEquals(p.invert('B'), 'A');
        assertEquals(p.invert('G'), 'G');
        assertEquals(p.invert('F'), 'D');
        assertEquals(p.invert('A'), 'W');
    }

    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW)"
                + " (JC)", new CharacterRange('A', 'Z'));
        assertEquals(p.permute('H'), 'P');
        assertEquals(p.permute('G'), 'G');

    }

    @Test
    public void testDerangement() {
        Permutation p = new Permutation("(PNH)"
                + " (ABDFIKLZYXW) (JC)",
                new CharacterRange('A', 'Z'));
        assertFalse(p.derangement());
        Permutation q = new Permutation("(PNHE) (ABDFIKMOQRSTUVLZYXW)"
                + " (JGC)", new CharacterRange('A', 'Z'));
        assertTrue(q.derangement());
    }



}


