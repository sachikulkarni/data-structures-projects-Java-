package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Sachi Kulkarni
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);
        _copyConfig = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
            _copyInput = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
            _copyInput = getInput(args[1]);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output.*/
    private void process() {
        String output = "";
        mac = readConfig();
        String one = _input.nextLine();
        if (!one.substring(0, 1).equals("*")) {
            throw error("incorrect input");
        }
        setUp(mac, one.substring(1));

        while (_input.hasNextLine()) {
            String curr = _input.nextLine();
            if (curr.equals("")) {
                printMessageLine("");
            } else if (curr.substring(0, 1).equals("*")) {
                setUp(mac, curr.substring(1));
            } else {
                output = mac.convert(curr.replaceAll(" ", "").toUpperCase());
                printMessageLine(output);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String first = _config.next();
            if (first.contains("-")) {
                String firstLastRange = first;
                _alphabet = new CharacterRange
                (firstLastRange.charAt(0), firstLastRange.charAt(2));
            } else if (!(first.contains("*")) && !(first.contains("("))
                    && !(first.contains(")")) && !(first.contains(" "))) {
                _alphabet = new ExtraCredit(first);
            } else {
                throw error("Incorrect given alphabet");
            }
            _config.nextLine();
            int rotCount = 0;
            int numRot = _config.nextInt();
            int numPawl = _config.nextInt();
            _config.nextLine();
            while (_config.hasNextLine()) {
                String text = _config.nextLine();
                Scanner line = new Scanner(text);
                String name = line.next();
                String perm = "";
                if (!(name.substring(0, 1).equals("("))) {
                    names.add(name.toUpperCase());
                    String type = line.next();
                    String notches = "";
                    while (line.hasNext()) {
                        String ln = line.next();
                        if (!(ln.charAt(0) == ('('))
                                || !(ln.charAt(ln.length() - 1) == (')'))) {
                            throw error("Permutations are"
                                    + " in an incorrect format.");
                        }
                        perm += ln + " ";
                    }
                    rotCount = readRotor(type, name, perm, notches, rotCount);
                } else {
                    perm = perm + name + " ";
                    while (line.hasNext()) {
                        perm += line.next() + " ";
                    }
                    Scanner p = new Scanner(perm);
                    String t = p.next();
                    while (!(t.equals(""))) {
                        ((Rotor) allRotors.get
                                (rotCount - 1)).permutation().addCycle(t);
                        if (p.hasNext()) {
                            t = p.next();
                        } else {
                            t = "";
                        }
                    }
                }
            }
            return new Machine(_alphabet, numRot, numPawl, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config.
     * @param perm permutation
     * @param type the type of the rotor
     * @param name the name of the rotor
     * @param notches the String containing notches
     * @param rotCount the count of rotors
     * */
    private int readRotor(String type, String name,
                          String perm, String notches, int rotCount) {
        try {
            if (type.length() > 1) {
                notches = type.substring(1);
            }
            Permutation p = new Permutation(perm, _alphabet);
            Rotor i;
            if (type.charAt(0) == 'M') {
                i = new MovingRotor(name, p, notches);
            } else if (type.charAt(0) == 'N') {
                i = new FixedRotor(name, p);
            } else if (type.charAt(0) == 'R') {
                i = new Reflector(name, p);
                Scanner perms = new Scanner(perm);
                while (perms.hasNext()) {
                    String per = perms.next();
                    String[] pr = per.split("");
                    int count = 0;
                    for (int j = 0; j < pr.length; j++) {
                        if (pr[j].charAt(0) >= 'A' && pr[j].charAt(0) <= 'Z') {
                            count += 1;
                        }
                    }
                    if (count > 2) {
                        throw error("there can only be 2 elements"
                                + " in a permutation for a Reflector");
                    }
                }
            } else {
                throw error("invalid rotor type");
            }
            allRotors.add(i);
            rotCount += 1;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
        return rotCount;
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner first = new Scanner(settings);
        neededRotors = new String[M.numRotors()];
        String temp = "";
        if (names.size() == 0) {
            throw error("Not enough rotors..");
        }
        ArrayList<String> contained = new ArrayList<>();
        for (int j = 0; j < M.numRotors(); j += 1) {
            if (first.hasNext()) {
                temp = first.next().toUpperCase();
                for (String name: names) {
                    if (name.equals(temp)) {
                        if (contained.contains(temp)) {
                            throw error("Cannot repeat rotors.");
                        }
                        neededRotors[j] = temp;
                        contained.add(temp);
                        break;
                    } else {
                        neededRotors[j] = "";
                    }
                }
            }
        }
        M.insertRotors(neededRotors);
        String temp2 = "";
        if (M.neededRotors().size() < neededRotors.length) {
            temp2 = temp.trim();
        } else {
            temp2 = first.next().trim();
        }
        if (temp2 == null) {
            throw error("Rotor setting not established.");
        }
        if (temp2.length() != M.numRotors() - 1) {
            throw error("invalid setting");
        }
        M.setRotors(temp2.toUpperCase());
        String plugP = "";
        while (first.hasNext()) {
            plugP += first.next() + " ";
        }
        Scanner perms = new Scanner(plugP);
        while (perms.hasNext()) {
            String per = perms.next();
            String[] pr = per.split("");
            int count = 0;
            for (int j = 0; j < pr.length; j++) {
                if (pr[j].charAt(0) >= 'A' && pr[j].charAt(0) <= 'Z') {
                    count += 1;
                }
            }
            if (count > 2) {
                throw error("only 2 elements allowed in Permutation.");
            }
        }
        Permutation plug = new Permutation(plugP, _alphabet);
        M.setPlugboard(plug);
    }
    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String newMsg = "";
        if (msg.length() < 5) {
            _output.println(msg);
        } else {
            for (int a = 0; a < msg.length(); a += 5) {
                if (msg.length() - a < 5) {
                    newMsg += msg.substring(a);
                    break;
                }
                newMsg += msg.substring(a, a + 5);
                newMsg += " ";
            }
            _output.println(newMsg);
        }
    }
    /** new Machine called mac.*/
    private Machine mac;
    /** The rotors from used rotors that are needed. */
    private String[] neededRotors;
    /** Contains all possible rotors. */
    private ArrayList<Rotor> allRotors = new ArrayList<>();
    /** Contains names of all rotors. */
    private ArrayList<String> names = new ArrayList<>();
    /** Alphabet used in this machine. */
    private Alphabet _alphabet;
    /** Source of input messages. */
    private Scanner _input;
    /** Copy of input messages. */
    private Scanner _copyInput;
    /** Source of machine configuration. */
    private Scanner _config;
    /** Copy of machine configuration. */
    private Scanner _copyConfig;
    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
