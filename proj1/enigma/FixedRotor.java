package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Sachi Kulkarni
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    private int setting;
    /** Creating a fixed rotor.
     * @param name the name of the rotor
     * @param perm the permutation */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        setting = 0;
    }

}
