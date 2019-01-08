package enigma;
import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Sachi Kulkarni
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    /** List of Notches. */
    private String _nots;
    /** The permutation. */
    private Permutation _perms;
    /** Creating a MovingRotor.
     * @param name the name of the rotor
     * @param perm the permutation of the rotor
     * @param notches a string containing notches */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _nots = notches;
        _perms = perm;
    }
    @Override
    boolean rotates() {
        return true;
    }
    @Override
    boolean atNotch() {
        return _nots.indexOf(permutation().alphabet()
                .toChar(setting())) != -1;
    }
    @Override
    void advance() {
        super.set(_perms.wrap(this.setting() + 1));
    }

}
