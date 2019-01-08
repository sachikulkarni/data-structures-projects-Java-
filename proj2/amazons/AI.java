package amazons;


import java.util.Iterator;

import static java.lang.Math.*;

import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author Sachi Kulkarni
 */
class AI extends Player {

    /**
     * A position magnitude indicating a win (for white if positive, black
     * if negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /**
     * A magnitude greater than a normal value.
     */
    private static final int INFTY = Integer.MAX_VALUE;

    /**
     * A new AI with no piece or controller (intended to produce
     * a template).
     */
    AI() {
        this(null, null);
    }

    /**
     * A new AI playing PIECE under control of CONTROLLER.
     */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /**
     * Return a move for me from the current position, assuming there
     * is a move.
     */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /**
     * The move found by the last call to one of the ...FindMove methods
     * below.
     */
    private Move _lastFoundMove;

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _lastFoundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _lastMoveFound.
     */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        int bestVal;
        int score = 0;
        Move next = null;
        if (sense == 1) {
            bestVal = Integer.MIN_VALUE;
        } else {
            bestVal = Integer.MAX_VALUE;
        }
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        Iterator<Move> moves = board.legalMoves(myPiece());
        while (moves.hasNext()) {
            next = moves.next();
            board().makeMove(next);
            if (next == null) {
                break;
            }
            score = findMove(board, depth - 1, false, -sense, alpha, beta);
            board().undo();
        }
        if (sense == 1) {
            if (score >= bestVal) {
                bestVal = findMove(board, depth - 1, false, sense, alpha, beta);
                alpha = max(alpha, score);
                if (saveMove) {
                    _lastFoundMove = next;
                }
                if (alpha >= beta) {
                    board().undo();
                }
            }
        } else {
            if (score <= bestVal) {
                bestVal = score;
                beta = min(beta, score);
                if (saveMove) {
                    _lastFoundMove = next;
                }
                if (alpha >= beta) {
                    board().undo();
                }
            }
        }
        return bestVal;
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();
        return 1;
    }


    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {

        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }
        int myMoves = 0;
        Iterator<Move> myLM = board.legalMoves(myPiece());
        while (myLM.hasNext()) {
            myLM.next();
            myMoves += 1;
        }
        int otherMoves = 0;
        Iterator<Move> otherLM = board.legalMoves(myPiece());
        while (otherLM.hasNext()) {
            otherLM.next();
            otherMoves += 1;
        }
        return myMoves - otherMoves;
    }
}
