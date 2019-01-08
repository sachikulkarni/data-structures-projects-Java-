package enigma;
import java.util.ArrayList;
import java.util.HashMap;
import static enigma.EnigmaException.*;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Sachi Kulkarni
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    /** A hashmap of permutations.*/
    private HashMap<Character, Character> perm
            = new HashMap<Character, Character>();
    /** A hashmap of inverted permutations. */
    private HashMap<Character, Character> inv
            = new HashMap<Character, Character>();
    /** An array of serperated cycles. */
    private String[] separate;
    /** An array of serperated characters within a String cycles. */
    private String[] sepChar;
    /** An array of whether a character is repeated. */
    private ArrayList<Character> contained = new ArrayList<>();

    /** Creating a Permutation.
     * @param cycles a String containing cycle
     * @param alphabet the alphabet */
    Permutation(String cycles, Alphabet alphabet) {
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) >= 'A' && cycles.charAt(i) <= 'Z') {
                if (!(contained.contains(cycles.charAt(i)))) {
                    contained.add(cycles.charAt(i));
                } else {
                    throw error("Repeated characters"
                           + " are not allowed within a cycle");
                }
            }
        }
        _alphabet = alphabet;
        separate = cycles.split("\\)");
        for (int letter = 0; letter < alphabet.size(); letter++) {
            perm.put(alphabet.toChar(letter), alphabet.toChar(letter));
            inv.put(alphabet.toChar(letter), alphabet.toChar(letter));
        }
        if (!cycles.equals("")) {
            for (String oneCycle : separate) {
                if (!oneCycle.equals("")) {
                    String cycleWO = oneCycle.trim().toUpperCase()
                            .replaceAll("\\(", "")
                            .replaceAll("\\)", "")
                            .replaceAll(" ", "");
                    addCycle(cycleWO);
                }
            }
        }
    }
    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    public void addCycle(String cycle) {
        cycle = cycle.trim().toUpperCase().replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll(" ", "");
        String cycleWO = cycle;
        for (int i = 0; i < cycleWO.length(); i++) {
            if (i == cycleWO.length() - 1) {
                perm.put(cycleWO.charAt(i), cycleWO.charAt(0));
            } else {
                perm.put(cycleWO.charAt(i), cycleWO.charAt(i + 1));
            }
        }
        for (int i = 0; i < cycleWO.length(); i++) {
            if (i == 0) {
                inv.put(cycleWO.charAt(i),
                        cycleWO.charAt(cycleWO.length() - 1));
            } else {
                inv.put(cycleWO.charAt(i), cycleWO.charAt(i - 1));
            }
        }
    }
    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }
    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }
    /** Return the result of applying this permutation to P modulo the
         *  alphabet size. */
    int permute(int p) {
        int moddedKey = wrap(p);
        char val = _alphabet.toChar(moddedKey);
        if (perm.containsKey(val)) {
            char permI = perm.get(val);
            return _alphabet.toInt(permI);
        }
        return _alphabet.toInt(val);
    }
    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int moddedKey = wrap(c);
        char val = _alphabet.toChar(moddedKey);
        if (inv.containsKey(val)) {
            char permI = inv.get(val);
            return _alphabet.toInt(permI);
        }
        return wrap(c);
    }
    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int i = _alphabet.toInt(p);
        int permI = permute(i);
        return _alphabet.toChar(permI);
    }
    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        int val = _alphabet.toInt(c);
        final int num = 65;
        return invert(val) + num;
    }
    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }
    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (Character c: perm.keySet()) {
            if (perm.get(c) == c) {
                return false;
            }
        }
        return true;
    }
    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
}

