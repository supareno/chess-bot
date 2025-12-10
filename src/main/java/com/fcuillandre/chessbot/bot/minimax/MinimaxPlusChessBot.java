package com.fcuillandre.chessbot.bot.minimax;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.bot.zobrist.TranspositionTable;
import com.fcuillandre.chessbot.bot.zobrist.TranspositionTableEntry;
import com.fcuillandre.chessbot.bot.zobrist.ZobristHashing;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.checkers.MoveChecker;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.utils.ChessUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * MinimaxPlusChessBot: Minimax with iterative deepening, transposition tables, and quiescence search.
 */
public class MinimaxPlusChessBot extends AbstractMinimaxChessBot {

    private final long timeLimitMillis = 10000; // 10 seconds per move max
    private final TranspositionTable transpositionTable = new TranspositionTable();

    // Helper class to hold move and score
    private static class MoveScore {
        final Move move;
        final int score;
        MoveScore(Move move, int score) {
            this.move = move;
            this.score = score;
        }
    }

    @Override
    public Move getMove(ChessGame game) {
        // Use opening book if available
        Move bookMove = SimpleOpeningBook.getBookMove(game);
        if (bookMove != null) {
            return bookMove;
        }
        transpositionTable.clear();
        ChessColor toMove = ChessColor.BLACK;
        int maxDepth = 4; // moved from field to local variable
        Move bestMove = null;
        Move lastCompletedBestMove = null;
        int lastCompletedBestScore = Integer.MIN_VALUE;
        long startTime = System.currentTimeMillis();
        for (int depth = 1; depth <= maxDepth; depth++) {
            MoveScore result = iterativeDeepening(game, toMove, depth, startTime);
            if (System.currentTimeMillis() - startTime > timeLimitMillis) {
                ChessUtils.log("Best move found at last completed depth " + (depth - 1) + ": " + lastCompletedBestMove);
                ChessUtils.log("Best score: " + lastCompletedBestScore);
                ChessUtils.log("-----");
                return lastCompletedBestMove;
            }
            if (result != null && result.move != null) {
                bestMove = result.move;
                lastCompletedBestMove = bestMove;
                lastCompletedBestScore = result.score;
            }
        }
        ChessUtils.log("Best move found at max depth " + maxDepth + ": " + bestMove);
        ChessUtils.log("Best score: " + lastCompletedBestScore);
        ChessUtils.log("-----");
        return bestMove;
    }

    private MoveScore iterativeDeepening(ChessGame game, ChessColor toMove, int depth, long startTime) {
        List<Move> legalMoves = generateLegalMovesUsingGame(game, toMove);

        ChessUtils.log(" - Moves: " + legalMoves.size() + " Depth: " + depth);
        ChessUtils.log(" ***** ");

        Move best = null;
        int bestScore = Integer.MIN_VALUE;
        ChessBoard board = game.getBoard();
        for (Move m : legalMoves) {
            if (System.currentTimeMillis() - startTime > timeLimitMillis) break;
            ChessBoard copy = copyBoard(board);
            applyMoveOnBoard(copy, m);
            int score = minimax(copy, opposite(toMove), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, game, startTime);

            ChessUtils.log(" - Move: " + m + " Score: " + score + " Depth: " + depth);

            if (score > bestScore) {
                bestScore = score;
                best = m;
            }
        }
        return new MoveScore(best, bestScore);
    }

    private int minimax(ChessBoard board, ChessColor toMove, int depth, int alpha, int beta, ChessGame gameCtx, long startTime) {
        long hash = ZobristHashing.computeHash(board);
        TranspositionTableEntry entry = transpositionTable.get(hash);
        if (entry != null && entry.getDepth() >= depth) {
            if (entry.getFlag() == TranspositionTableEntry.Flag.EXACT) {
                return entry.getScore();
            } else if (entry.getFlag() == TranspositionTableEntry.Flag.LOWERBOUND) {
                alpha = Math.max(alpha, entry.getScore());
            } else if (entry.getFlag() == TranspositionTableEntry.Flag.UPPERBOUND) {
                beta = Math.min(beta, entry.getScore());
            }
            if (alpha >= beta) {
                return entry.getScore();
            }
        }

        if (System.currentTimeMillis() - startTime > timeLimitMillis) return Integer.MIN_VALUE;
        if (depth == 0) {
            return quiescenceSearch(board, toMove, alpha, beta, gameCtx, startTime);
        }
        List<Move> moves = generateLegalMovesForColor(board, toMove, gameCtx);
        if (moves.isEmpty()) {
            boolean inCheck = isKingInCheckForColor(board, toMove, gameCtx);
            if (inCheck) {
                return (toMove == ChessColor.BLACK) ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE - 1;
            } else {
                return 0;
            }
        }
        int value = Integer.MIN_VALUE;
        int originalAlpha = alpha; // store original alpha for flag assignment
        for (Move move : moves) {
            if (System.currentTimeMillis() - startTime > timeLimitMillis) break;
            ChessBoard next = copyBoard(board);
            applyMoveOnBoard(next, move);
            int score = minimax(next, opposite(toMove), depth - 1, alpha, beta, gameCtx, startTime);
            value = Math.max(value, score);
            alpha = Math.max(alpha, value);
            if (alpha >= beta) break;
        }

        TranspositionTableEntry.Flag flag;
        if (value <= originalAlpha) {
            flag = TranspositionTableEntry.Flag.UPPERBOUND;
        } else if (value >= beta) {
            flag = TranspositionTableEntry.Flag.LOWERBOUND;
        } else {
            flag = TranspositionTableEntry.Flag.EXACT;
        }
        transpositionTable.put(hash, new TranspositionTableEntry(depth, value, flag));

        return value;
    }

    private int quiescenceSearch(ChessBoard board, ChessColor toMove, int alpha, int beta, ChessGame gameCtx, long startTime) {
        if (System.currentTimeMillis() - startTime > timeLimitMillis) return Integer.MIN_VALUE;

        int standPat = evaluateBoard(board);
        if (standPat >= beta) {
            return beta;
        }
        if (alpha < standPat) {
            alpha = standPat;
        }

        List<Move> captureMoves = generateCaptureMoves(board, toMove, gameCtx);
        for (Move move : captureMoves) {
            ChessBoard next = copyBoard(board);
            applyMoveOnBoard(next, move);
            int score = -quiescenceSearch(next, opposite(toMove), -beta, -alpha, gameCtx, startTime);
            if (score >= beta) {
                return beta;
            }
            if (score > alpha) {
                alpha = score;
            }
        }
        return alpha;
    }

    private List<Move> generateCaptureMoves(ChessBoard board, ChessColor color, ChessGame gameCtx) {
        List<Move> captures = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getColor() == color) {
                    MoveChecker checker = getChecker(piece.getType());
                    if (checker == null) continue;
                    for (int dx = 0; dx < 8; dx++) {
                        for (int dy = 0; dy < 8; dy++) {
                            if (x == dx && y == dy) continue;
                            if (board.getPieceAt(dx, dy) != null && board.getPieceAt(dx, dy).getColor() != color) {
                                Move m = new Move(new Coordinate(x, y), new Coordinate(dx, dy));
                                if (checker.isValidMove(piece, m, board, gameCtx)) {
                                    ChessBoard tmp = copyBoard(board);
                                    applyMoveOnBoard(tmp, m);
                                    if (!isKingInCheckForColor(tmp, color, gameCtx)) {
                                        captures.add(m);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return captures;
    }
}
