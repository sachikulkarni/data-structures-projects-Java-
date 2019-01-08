package enigma;
import static enigma.EnigmaException.*;
/** An extension of Alphabet with a string of Characters given.
 * @author Sachi Kulkarni */
class ExtraCredit extends Alphabet {
    /** the string of letters. */
    private String _alpha;
    /** The string split into chars.*/
    private String[] divided;
    /** The constructor for EC.
     * @param alpha the alphabet. */
    ExtraCredit(String alpha) {
        _alpha = alpha;
        divided = _alpha.split("");
    }

    @Override
    int size() {
        return _alpha.length();
    }

    @Override
    boolean contains(char ch) {
        for (int i = 0; i < _alpha.length(); i++) {
            if (_alpha.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    @Override
    char toChar(int index) {
        if (index > size() || index < 0) {
            throw error("Character index out of bounds.");
        }
        return _alpha.charAt(index);
    }

    @Override
    int toInt(char ch) {
        int index = 0;
        if (!contains(ch)) {
            throw error("character out of range");
        }
        for (int i = 0; i < _alpha.length(); i++) {
            if (_alpha.charAt(i) == ch) {
                index = i;
            }
        }
        return index;
    }

}
