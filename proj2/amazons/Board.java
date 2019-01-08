package amazons;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.ArrayList;
import static amazons.Piece.*;
import static amazons.Move.mv;


/** The state of an Amazons Game.
 *  @author Sachi Kulkarni
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;
    /** Sets up an array or a board of pieces. */
    private Piece[][] boardArr = new Piece[SIZE][SIZE];
    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.put(model.get(i, j), i, j);
            }
        }
        this._turn = model.turn();
        this._moves = model._moves;
        this._winner = model._winner;

    }

    /** Clears the board to the initial position. */
    void init() {
        boardArr = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boardArr[i][j] = EMPTY;
            }
        }
        boardArr[6][0] = BLACK;
        boardArr[9][3] = BLACK;
        boardArr[9][6] = BLACK;
        boardArr[6][9] = BLACK;
        boardArr[3][0] = WHITE;
        boardArr[0][3] = WHITE;
        boardArr[0][6] = WHITE;
        boardArr[3][9] = WHITE;
        _moves = new Stack<Move>();
        _turn = WHITE;
        _winner = EMPTY;
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return _count;
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        if (_winner == EMPTY) {
            return null;
        }

        return _winner;

    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return boardArr[s.row()][s.col()];

    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW < 9. */
    final Piece get(int col, int row) {
        return boardArr[row][col];

    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        put(p, s.col(), s.row());

    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        if (!Square.exists(col, row)) {
            throw new
                    java.lang.Error("cannot put piece there"
                    + " because square position is invalid");
        }
        boardArr[row][col] = p;

        _winner = EMPTY;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        int dir = from.direction(to);
        if (from != to) {
            while (from != to) {
                if (this.get(from.queenMove(dir, 1)) != EMPTY
                        && from.queenMove(dir, 1) != asEmpty) {
                    return false;
                }
                from = from.queenMove(dir, 1);
            }
            return true;
        }
        return false;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        if (from.row() >= 0 && from.col() >= 0
                && from.row() < Board.SIZE
                && from.col() < Board.SIZE) {
            if (this.get(from) == _turn) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        if (isLegal(from)) {
            if (isUnblockedMove(from, to, from)) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        if (Square.exists(spear.col(), spear.row())) {
            if (isLegal(from, to)) {
                if (isUnblockedMove(to, spear, from)) {
                    return true;
                }
            }
        }
        return false;

    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to(), move.spear());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        if (isLegal(from, to, spear) && this.get(from) == _turn) {
            this.put(this.get(from), to);
            this.put(EMPTY, from);
            this.put(SPEAR, spear);
            if (turn() == WHITE) {
                _turn = BLACK;
            } else {
                _turn = WHITE;
            }
            _count += 1;
            _moves.add(Move.mv(from, to, spear));
        }

    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (!_moves.empty()) {
            Move last = _moves.pop();
            this.boardArr[last.spear().row()][last.spear().col()] = EMPTY;
            this.boardArr[last.from().row()][last.from().col()]
                    = this.boardArr[last.to().row()][last.to().col()];
            this.boardArr[last.to().row()][last.to().col()] = EMPTY;
            _count--;
            _winner = null;
            _turn = turn().opponent();
        }
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {
        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = 0;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            Square curr = _from.queenMove(_dir, _steps);
            toNext();
            return curr;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            _steps += 1;
            if (_from.queenMove(_dir, _steps) != null
                    && isUnblockedMove(_from,
                    _from.queenMove(_dir, _steps), _asEmpty)) {
                return;
            } else {
                while (_from.queenMove(_dir, _steps) == null && _dir < 8
                        || _dir < 8
                        && !isUnblockedMove(_from,
                        _from.queenMove(_dir, _steps),
                        _asEmpty)) {
                    _dir += 1;
                    _steps = 1;
                }
            }
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /**
         * All legal moves for SIDE (WHITE or BLACK).
         */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return !end;
        }

        @Override
        public Move next() {
            Move curr = mv(_start, _nextSquare, sp);
            toNext();
            return curr;

        }

        /**
         * Advance so that the next valid Move is
         * _start-_nextSquare(sp), where sp is the next value of
         * _spearThrows.
         */
        private void toNext() {
            if (!_spearThrows.hasNext()
                    && !_pieceMoves.hasNext()
                    && !_startingSquares.hasNext()) {
                end = true;
                return;
            }
            if (_spearThrows.hasNext()) {
                sp = _spearThrows.next();

            } else if (_pieceMoves.hasNext()) {
                _nextSquare = _pieceMoves.next();
                _spearThrows = reachableFrom(_nextSquare, _start);
                toNext();
            } else {
                while (_startingSquares.hasNext()) {
                    Square tempStart = _startingSquares.next();
                    Piece x = get(tempStart);
                    if (get(tempStart) == _fromPiece
                            && !pieceLocations.contains(tempStart)) {

                        _start = tempStart;
                        pieceLocations.add(_start);
                        _pieceMoves = reachableFrom(_start, null);
                        toNext();
                        break;
                    }
                }
                if (!_startingSquares.hasNext())  {
                    end = true;
                }
            }
        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
        /** Indicates when to end the legal moves iterator. */
        private boolean end = false;
        /** Tracks the square where the spear was thrown. */
        private Square sp;
        /** Makes sure the same piece of a
         *  certain turn color is not checked again. */
        private ArrayList<Square> pieceLocations = new ArrayList<Square>();
    }

    @Override
    public String toString() {
        Piece[][] x = boardArr;
        String store = "";
        for (int i = 9; i >= 0; i--) {
            store += "   ";
            for (int j = 0; j < SIZE; j++) {
                store += boardArr[i][j];
                if (!(j == SIZE - 1)) {
                    store += " ";
                }
            }
            store += "\n";
        }
        return store;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
    /** Count of how many turns have been taken. */
    private int _count;
    /** Stack of moves. */
    private Stack<Move> _moves;
}
