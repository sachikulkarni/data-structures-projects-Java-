package enigma;

import org.junit.Test;

import static enigma.TestUtils.UPPER;

import java.util.Collection;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class MachineTest {
    Permutation one = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
            + "(DFG) (IV) (JZ) (S)", UPPER);
    Permutation two = new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ)"
            + " (BJ) (GR) (NT) (A) (Q)", UPPER);
    Permutation three = new Permutation("(ABDHPEJT)"
            + " (CFLVMZOYQIRWUKXSG) (N)", UPPER);
    Permutation four = new Permutation("(AEPLIYWCOXMRFZBSTGJQNH)"
            + " (DV) (KU)", UPPER);
    Permutation beta = new Permutation("(ALBEVFCYODJWUGNMQTZSKPR)"
            + " (HIX)", UPPER);
    Permutation b = new Permutation("(AE) (BN) (CK) (DQ) (FU)"
            + " (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
    Permutation c = new Permutation("(AR) (BD) (CO) (EJ) (FN) (GT)"
            + " (HK) (IV) (LM) (PW) (QZ) (SX) (UY)", UPPER);
    Permutation plug = new Permutation("(YF) (ZH)", UPPER);
    Permutation plug2 = new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER);
    Alphabet ac = new CharacterRange('A', 'C');
    Permutation five = new Permutation("(ABC)", ac);
    Permutation six = new Permutation("(BAC)", ac);
    Permutation seven = new Permutation("(TD) (KC) (JZ)", UPPER);

    Rotor _one = new MovingRotor("I", one, "Q");
    Rotor _two = new MovingRotor("II", two, "E");
    Rotor _three = new MovingRotor("III", three, "V");
    Rotor _four = new MovingRotor("IV", four, "J");
    Rotor _beta = new FixedRotor("Beta", beta);
    Rotor _one2 = new MovingRotor("I2", five, "C");
    Rotor _two2 = new MovingRotor("II2", five, "C");
    Rotor _three2 = new MovingRotor("III2", five, "C");
    Rotor _four2 = new MovingRotor("IV2", six, "C");
    Rotor _A = new Reflector("A", five);
    Rotor _B = new Reflector("B", b);
    Rotor _C = new Reflector("C", c);
    public Collection<Rotor> all = new ArrayList<>();
    public Collection<Rotor> test = new ArrayList<>();
    public Collection<Rotor> all2 = new ArrayList<>();
    public Collection<Rotor> test2 = new ArrayList<>();
    @Test
    public void checkInsertRotors() {

        all.add(_C);
        all.add(_B);
        all.add(_beta);
        all.add(_four);
        all.add(_three);
        all.add(_two);
        all.add(_one);

        test.add(_B);
        test.add(_beta);
        test.add(_three);
        test.add(_four);
        test.add(_one);

        String[] names = {"B", "Beta", "III", "IV", "I"};
        Machine mac = new Machine(UPPER, 7, 3, all);
        mac.insertRotors(names);
        assertEquals(mac.neededRotors(), test);
        mac.setRotors("APLE");
        assertEquals(((Rotor) mac.neededRotors().toArray()[1]).setting(), 0);
        assertEquals(((Rotor) mac.neededRotors().toArray()[2]).setting(), 15);
        assertEquals(((Rotor) mac.neededRotors().toArray()[4]).setting(), 4);
        mac.setRotors("AXLE");
        mac.setPlugboard(plug);
        String x = "YZ";
        assertEquals(mac.convert(24), 25);
        mac.setPlugboard(plug2);

        all2.add(_A);
        all2.add(_one2);
        all2.add(_two2);
        all2.add(_four2);
        all2.add(_three2);

        test2.add(_A);
        test2.add(_one2);
        test2.add(_two2);
        test2.add(_three2);

        String[] names2 = {"A", "I2", "II2", "III2"};
        Machine mac2 = new Machine(ac, 4, 3, all2);
        mac2.insertRotors(names2);
        mac2.setRotors("AAAA");
        String[] names3 = {"B", "Beta", "I", "II", "III"};
        Machine mac3 = new Machine(UPPER, 5, 3, all);
        mac3.insertRotors(names3);
        mac3.setRotors("AAAA");
        mac3.setPlugboard(seven);
        assertEquals(mac3.convert("I was scared of coding in Java"),
                "HGJNBOKDWALBFKUCMUTJZUIO");

    }

}
