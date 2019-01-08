package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Sachi Kulkarni
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    /** Number of pawls. */
    private int _pawls;
    /** Number of Rotors. */
    private int _numRotors;
    /** List of all rotors. */
    private Rotor[] _allRotors;
    /** Permutation Plugboard. */
    private Permutation _plugboard;

    /** Creating a new machine.
     * @param alpha the alphabet
     * @param numRotors the number of rotors
     * @param pawls the number of pawls
     * @param allRotors a collection containing the rotors*/
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _pawls = pawls;
        _numRotors = numRotors;
        _allRotors = allRotors.toArray(new Rotor[allRotors.size()]);
        _plugboard = new Permutation("", alpha);
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }
    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        int moving = 0;
        if (rotors.length > numRotors()) {
            throw error("too many rotors given");
        }
        _neededRotors = new ArrayList<>();
        for (String rotor : rotors) {
            for (Rotor in : _allRotors) {
                if (in.name().toUpperCase().equals(rotor.toUpperCase())) {
                    if (neededRotors().contains(in)) {
                        throw error("Cannot insert the same rotor twice.");
                    }
                    _neededRotors.add(in);
                    if (in instanceof MovingRotor) {
                        moving += 1;
                    }
                }
            }
        }
        if (!(_neededRotors.get(0).reflecting())) {
            throw error("First rotor must be a reflector");
        }
        if (_neededRotors.size() > _allRotors.length) {
            throw error("wrong number of rotors");
        }
        if (numPawls() != moving) {
            throw error("wrong number of moving rotors");
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 upper-case letters. The first letter refers to the
     * leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        setting = setting.replaceAll(" ", "");
        for (int h = 1; h < _neededRotors.size(); h++) {
            _neededRotors.get(h).set(_alphabet.toInt(setting.charAt(h - 1)));
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * <p>
     * the machine.
     */
    int convert(int c) {
        boolean[] ifAdvance = new boolean[_neededRotors.size()];
        ifAdvance[_neededRotors.size() - 1] = true;
        int output = c;
        for (int i = _neededRotors.size() - 1; i > 0; i--) {
            if (i == _neededRotors.size() - 1) {
                if ((_neededRotors.get(i)).atNotch()) {
                    ifAdvance[i] = true;
                }
            } else if (_neededRotors.get(i + 1).atNotch()) {
                if (i + 1 < _neededRotors.size()) {
                    ifAdvance[i] = true;
                }
            }
            if (_neededRotors.get(i).atNotch()
                    && _neededRotors.get(i - 1).rotates()) {
                if (i - 1 > 0) {
                    ifAdvance[i] = true;
                }
            }
        }
        for (int j = 0; j < ifAdvance.length; j++) {
            if (ifAdvance[j]) {
                _neededRotors.get(j).advance();
            }
        }
        if (!(_plugboard == null)) {
            int encode = _plugboard.permute(c);
            output = encode;
        }
        for (int a = _neededRotors.size() - 1; a >= 0; a--) {
            output = ((Rotor) _neededRotors.get(a)).convertForward(output);
        }
        for (int b = 1; b < _neededRotors.size(); b++) {
            output = ((Rotor) _neededRotors.get(b)).convertBackward(output);
        }
        if (!(_plugboard == null)) {
            int encode = _plugboard.permute(output);
            output = encode;
        }
        return output;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        String out = "";
        msg = msg.replaceAll(" ", "");
        msg = msg.toUpperCase();
        String[] chars = msg.split("");
        String conv = "";
        for (int a = 0; a < msg.length(); a++) {
            conv += _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(a))));
        }
        return conv;
    }

    /** Return accessor for _neededRotors. */
    ArrayList<Rotor> neededRotors() {
        return _neededRotors;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Contains all Needed Rotors. */
    private ArrayList<Rotor> _neededRotors;
}
