package make;

import java.util.ArrayList;
import java.util.List;

import static make.Main.error;

/** Represents the rules concerning a single target in a makefile.
 *  @author P. N. Hilfinger
 */
class Rule {
    /** A new Rule for TARGET. Adds corresponding vertex to MAKER's dependence
     *  graph. */
    Rule(Maker maker, String target) {
        _maker = maker;
        _depends = _maker.getGraph();
        _target = target;
        _vertex = _depends.add(this);
        _time = _maker.getInitialAge(target);
        _finished = false;
    }

    /** Add the target of DEPENDENT to my dependencies. */
    void addDependency(Rule dependent) {
        if (!_depends.contains(dependent.getVertex())) {
            int x = _depends.add();
            _depends.add(_vertex, x);
            _depends.add(_vertex, dependent.getVertex());
        } else {
            _depends.add(_vertex, dependent.getVertex());
        }
    }

    /** Add COMMANDS to my command set.  Signals IllegalStateException if
     *  COMMANDS is non-empty, but I already have a non-empty command set.
     */
    void addCommands(List<String> commands) {
        if (!commands.isEmpty() && !_commands.isEmpty()) {
            throw new IllegalStateException("error: cannot add"
                    + " commands when commands"
                    + " have already been added.");
        } else {
            _commands.addAll(commands);
        }
    }

    /** Return the vertex representing me. */
    int getVertex() {
        return _vertex;
    }

    /** Return my target. */
    String getTarget() {
        return _target;
    }

    /** Return my target's current change time. */
    Integer getTime() {
        return _time;
    }

    /** Return true iff I have not yet been brought up to date. */
    boolean isUnfinished() {
        return !_finished;
    }

    /** Check that dependencies are in fact built before it's time to rebuild
     *  a node. */
    private void checkFinishedDependencies() {
        try {
            for (int success : _depends.successors(getVertex())) {
                if (_depends.getLabel(success).isUnfinished()) {
                    _built = false;
                }
            }
        } catch (StackOverflowError err) {
            error("error: cycles are present");
        }
    }

    /** Return true iff I am out of date and need to be rebuilt (including the
     *  case where I do not exist).  Assumes that my dependencies are all
     *  successfully rebuilt. */
    private boolean outOfDate() {
        if (this.getTime() == null) {
            return true;
        } else {
            for (int success: _depends.successors(_vertex)) {
                if (_depends.getLabel(success).getTime() != null) {
                    if (_time < _depends.getLabel(success).getTime()) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /** Rebuild me, if needed, after checking that all dependencies are rebuilt
     *  (error otherwise). */
    void rebuild() {
        checkFinishedDependencies();
        if (!_built) {
            error("error: dependencies need to be built before rebuilding");
        }
        if (outOfDate()) {
            if (_commands.isEmpty()) {
                error("error: %s needs to be rebuilt, but has no commands",
                      _target);
            }
            for (String comm : _commands) {
                System.out.println(comm);
            }
            _time = _maker.getCurrentTime();
        }
        _finished = true;
    }

    /** The Maker that created me. */
    private Maker _maker;
    /** The Maker's dependency graph. */
    private Depends _depends;
    /** My target. */
    private String _target;
    /** The vertex corresponding to my target. */
    private int _vertex;
    /** My command list. */
    private ArrayList<String> _commands = new ArrayList<>();
    /** True iff I have been brought up to date. */
    private boolean _finished;
    /** My change time, or null if I don't exist. */
    private Integer _time;
    /** Checks if dependencies are built. */
    private boolean _built = true;
}
