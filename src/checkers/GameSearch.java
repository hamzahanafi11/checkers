package checkers;

/**
 * Contains the rules of Checkers. It also has methods for making the computer
 * think using the minimax algorithm.
 */
public class GameSearch {

    public static Board executeMove(final Move oldMove, final Board oldBoard) {
        if (oldMove.isJump()) {
            return executeJump(oldMove, oldBoard);
        } else {
            Board newBoard = oldBoard.copy();
            Move newMove = oldMove.copy(newBoard);
            moveChecker(newMove, newBoard);
            newBoard.addMoveToHistory(oldMove);
            return newBoard;
        }
    }

    private static Board executeJump(final Move oldMove, final Board oldBoard) {
        Board newBoard = oldBoard.copy();
        Move newMove = oldMove.copy(newBoard);
        removeCapturedChecker((MoveJump) newMove, newBoard);
        moveChecker(newMove, newBoard);
        newBoard.addMoveToHistory(oldMove);

        // Check for multiple jumps.
        MoveList movelist = new MoveList();
        newMove.getChecker().findValidJumps(movelist, newBoard);
        if (movelist.size() == 0) {
            return newBoard;   // No more jumps.
        } else if (movelist.size() == 1) {
            return executeJump(movelist.first(), newBoard);
        } else {
            MoveIterator iterator = movelist.getIterator();
            BoardList boardlist = new BoardList();
            while (iterator.hasNext()) {
                boardlist.add(executeJump(iterator.next(), newBoard));
            }
            return boardlist.findBestBoard(oldMove.getChecker().getColor());
        }
    }

    // Returns a new board where oldMove has been executed. Since it is the 
    // user that makes the move, the jump should not continue with multiple
    // jumps as is done in executeJump.
    public static Board executeUserJump(final Move oldMove,
            final Board oldBoard) {
        Board newBoard = oldBoard.copy();
        Move newMove = oldMove.copy(newBoard);
        removeCapturedChecker((MoveJump) newMove, newBoard);
        moveChecker(newMove, newBoard);
        return newBoard;
    }

    private static void moveChecker(Move move, Board board) {
        board.removeChecker(move.getChecker());
        board.setChecker(move.getChecker(), move.getDestination());
        if ((move.getChecker().kingRow()) && (!move.getChecker().isKing())) {
            move.getChecker().makeKing();
        }
    }

    private static void removeCapturedChecker(MoveJump move, Board board) {
        board.removeChecker(board.getChecker(move.capturedCoordinate()));
    }

    /**
     * Returns the list of all valid moves for player "color" on "board". If a
     * jump is possible, only jumps will be returned, since jumps are mandatory.
     */
    public static MoveList findAllValidMoves(final Board board, int color) {
        boolean jumpExist = false;
        MoveList movelist = new MoveList();
        Coordinate c = null;
        for (int i = 1; i < 33; i++) {
            c = new Coordinate(i);
            if ((board.getChecker(c) != null)
                    && (board.getChecker(c).getColor() == color)) {
                if (jumpExist) {
                    board.getChecker(c).findValidJumps(movelist, board);
                } else if (board.getChecker(c).findValidMoves(movelist, board)) {
                    jumpExist = true;
                }
            }
        }
        // Remove normal moves, in case a normal move has been added to the list 
        // before the first jump.
        if (jumpExist) {
            removeNormalMoves(movelist);
        }
        return movelist;
    }

    /**
     * Returns true if there is a jump among the valid moves. It is used to test
     * that the user does not make a normal move, when she can make a jump.
     */
    public static boolean existJump(final Board board, int color) {
        MoveList movelist = findAllValidMoves(board, color);
        MoveIterator iterator = movelist.getIterator();
        while (iterator.hasNext()) {
            if (iterator.next().isJump()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes normal moves from movelist. This is neccesary if the list
     * contains jumps, since a jump is mandatory. A normal move should therefore
     * not be considered if a jump exist.
     */
    private static void removeNormalMoves(MoveList movelist) {
        //We cannot traverse and remove currently, since this will mess up the 
        //iterator. Therefore we use a temporary list, normalMoves, to store the
        // moves that needs to be removed.
        MoveList normalMoves = new MoveList();
        MoveIterator iterator = movelist.getIterator();
        Move current = null;
        while (iterator.hasNext()) {
            current = iterator.next();
            if (!current.isJump()) {
                normalMoves.add(current);
            }
        }
        iterator = normalMoves.getIterator();
        while (iterator.hasNext()) {
            movelist.remove(iterator.next());
        }
    }

    /**
     * Uses the "Minimax with alpha-beta cutoff" algorithm to find the best move
     * for a player. However, unlike the normal minimax algorithm, which
     * searches an already built game tree, this algorithm builds and evaluates
     * the game tree concurrent ly.
     */
    public static Board minimaxAB(Board board, int recursion, int player,
            Board alpha, Board beta) {
        if (recursion > 0) {
            MoveList validMoves = null;
            validMoves = findAllValidMoves(board, player);
            if (validMoves.size() == 0) {
                return board;
            }
            MoveIterator iterator = validMoves.getIterator();
            if (player == CheckerPosition.BLACK) {   // Black - min node.
                while (iterator.hasNext()) {
                    Board nextBoard = minimaxAB(executeMove(iterator.next(), board),
                            recursion - 1, opponent(player),
                            alpha, beta);
                    beta = minBoard(beta, nextBoard);
                    if (alpha.evaluate() >= beta.evaluate()) {
                        return alpha;
                    }
                }
                return beta;
            } else {      // White - max node.
                while (iterator.hasNext()) {
                    Board nextBoard = minimaxAB(executeMove(iterator.next(), board),
                            recursion - 1, opponent(player),
                            alpha, beta);
                    alpha = maxBoard(alpha, nextBoard);
                    if (alpha.evaluate() >= beta.evaluate()) {
                        return beta; //Cutoff. Return the best solution so far.
                    }
                }
                return alpha;
            }
        } else {
            return board;   // Recursion done -> leaf in game tree.
        }
    }

    // The minimax algorithm without alpha-beta cutoff.
    public static Board minimax(Board board, int recursion, int player) {
        if (recursion > 0) {
            MoveList validMoves = null;
            validMoves = findAllValidMoves(board, player);
            if (validMoves.size() == 0) {
                return board;
            }
            MoveIterator iterator = validMoves.getIterator();
            BoardList boardlist = new BoardList();
            if (player == CheckerPosition.BLACK) {   // Black - min node.
                while (iterator.hasNext()) {
                    boardlist.add(minimax(executeMove(iterator.next(), board),
                            recursion - 1, opponent(player)));
                }
                return boardlist.findBestBoard(CheckerPosition.BLACK);
            } else {      // White - max node.
                while (iterator.hasNext()) {
                    boardlist.add(minimax(executeMove(iterator.next(), board),
                            recursion - 1, opponent(player)));
                }
                return boardlist.findBestBoard(CheckerPosition.WHITE);
            }
        } else {
            return board;   // Recursion done -> leaf in game tree.
        }
    }

    public static int opponent(int player) {
        if (player == CheckerPosition.WHITE) {
            return CheckerPosition.BLACK;
        } else {
            return CheckerPosition.WHITE;
        }
    }

    // Returns the board with the smallest score.
    public static Board minBoard(Board a, Board b) {
        if (a.evaluate() <= b.evaluate()) {
            return a;
        } else {
            return b;
        }
    }

    // Returns the board with the largest score.
    public static Board maxBoard(Board a, Board b) {
        if (a.evaluate() >= b.evaluate()) {
            return a;
        } else {
            return b;
        }
    }

    // Returns a board that represents a +infinity score when evaluated. 
    public static Board plusInfinityBoard() {
        Board b = new Board();
        for (int i = 1; i < 33; i++) {
            Coordinate c = new Coordinate(i);
            CheckerPosition WhiteKing = new WhiteChecker(c);
            WhiteKing.makeKing();
            b.setChecker(WhiteKing, c);
        }
        return b;
    }

    // Returns a board that represents a -infinity score when evaluated.
    public static Board minusInfinityBoard() {
        Board b = new Board();
        for (int i = 1; i < 33; i++) {
            Coordinate c = new Coordinate(i);
            CheckerPosition BlackKing = new BlackChecker(c);
            BlackKing.makeKing();
            b.setChecker(BlackKing, c);
        }
        return b;
    }

    public static boolean validCoordinate(Coordinate c) {
        return ((c.get() >= 1) && (c.get() <= 32));
    }

    public static boolean validWhiteMove(Coordinate from, Coordinate to,
            Board board) {
        return validUpMove(from, to, board)
                && (board.getChecker(from).getColor() == CheckerPosition.WHITE);
    }

    public static boolean validBlackMove(Coordinate from, Coordinate to,
            Board board) {
        return validDownMove(from, to, board)
                && (board.getChecker(from).getColor() == CheckerPosition.BLACK);
    }

    public static boolean validKingMove(Coordinate from, Coordinate to,
            Board board) {
        return (validUpMove(from, to, board) || validDownMove(from, to, board));
    }

    public static boolean validWhiteJump(Coordinate from, Coordinate to,
            Board board) {
        return validUpJump(from, to, CheckerPosition.WHITE, board);
    }

    public static boolean validBlackJump(Coordinate from, Coordinate to,
            Board board) {
        return validDownJump(from, to, CheckerPosition.BLACK, board);
    }

    public static boolean validKingJump(Coordinate from, Coordinate to,
            Board board) {
        int color = board.getChecker(from).getColor();
        return validUpJump(from, to, color, board)
                || validDownJump(from, to, color, board);
    }

    private static boolean validUpMove(Coordinate from, Coordinate to,
            Board board) {
       

        return validCoordinate(from) && validCoordinate(to)
                && board.vacantCoordinate(to) && !board.vacantCoordinate(from)
                && (from.row() - to.row() == 1)
                && ((from.upLeftMove().equals(to))
                || (from.upRightMove().equals(to)));
    }

    private static boolean validDownMove(Coordinate from, Coordinate to,
            Board board) {
        return validCoordinate(from) && validCoordinate(to)
                && board.vacantCoordinate(to) && !board.vacantCoordinate(from)
                && (to.row() - from.row() == 1)
                && ((from.downLeftMove().equals(to))
                || (from.downRightMove().equals(to)));
    }

    private static boolean validUpJump(Coordinate from, Coordinate to, int color,
            Board board) {
        boolean basis = validCoordinate(from) && validCoordinate(to)
                && board.vacantCoordinate(to) && !board.vacantCoordinate(from)
                && (board.getChecker(from).getColor() == color)
                && (from.row() - to.row() == 2);
        if (basis) {
            if (from.upLeftJump().equals(to)) {
                return (!board.vacantCoordinate(from.upLeftMove())
                        && (board.getChecker(from.upLeftMove()).getColor()
                        == opponent(color)));
            } else if (from.upRightJump().equals(to)) {
                return (!board.vacantCoordinate(from.upRightMove())
                        && (board.getChecker(from.upRightMove()).getColor()
                        == opponent(color)));
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean validDownJump(Coordinate from, Coordinate to,
            int color, Board board) {
        boolean basis = validCoordinate(from) && validCoordinate(to)
                && board.vacantCoordinate(to) && !board.vacantCoordinate(from)
                && (board.getChecker(from).getColor() == color)
                && (to.row() - from.row() == 2);
        if (basis) {
            if (from.downLeftJump().equals(to)) {
                return (!board.vacantCoordinate(from.downLeftMove())
                        && (board.getChecker(from.downLeftMove()).getColor()
                        == opponent(color)));
            } else if (from.downRightJump().equals(to)) {
                return (!board.vacantCoordinate(from.downRightMove())
                        && (board.getChecker(from.downRightMove()).getColor()
                        == opponent(color)));
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean checkBoard(Board board) {
        for (int i = 1; i < 33; i++) {
            Coordinate temp = new Coordinate(i);
            if (board.getChecker(temp) != null) {
                if (!board.getChecker(temp).getPosition().equals(temp)) {
                    return false;
                }
            }
        }
        return true;
    }

    // For test.
    public static void placeChecker(Board board, int c, int color, boolean king) {
        Coordinate co = new Coordinate(c);
        CheckerPosition checker = null;
        if (color == CheckerPosition.WHITE) {
            checker = new WhiteChecker(co);
        } else {
            checker = new BlackChecker(co);
        }
        if (king) {
            checker.makeKing();
        }
        board.setChecker(checker, co);
    }
}
