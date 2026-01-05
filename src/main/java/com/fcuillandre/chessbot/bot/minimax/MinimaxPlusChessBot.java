/*
 * Copyright 2025-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 *
 * @author fcuillandre
 * @version 0.1
 */
public final class MinimaxPlusChessBot extends AbstractMinimaxChessBot {

    private final long timeLimitMillis = 10000; // 10 seconds per move max
    private final TranspositionTable transpositionTable = new TranspositionTable();

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
            if (result.move != null) {
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
            // Use negamax approach: negate the result from opponent's perspective
            int score = -minimax(copy, opposite(toMove), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, game, startTime);

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
            return evaluateBoardFromPerspective(board, toMove);
        }

        List<Move> moves = generateLegalMovesForColor(board, toMove, gameCtx);
        if (moves.isEmpty()) {
            boolean inCheck = isKingInCheckForColor(board, toMove, gameCtx);
            if (inCheck) {
                // Checkmate: very bad for current player, better if deeper (faster mate is worse)
                return Integer.MIN_VALUE + depth;
            } else {
                // Stalemate
                return 0;
            }
        }

        int value = Integer.MIN_VALUE;
        int originalAlpha = alpha;

        for (Move move : moves) {
            if (System.currentTimeMillis() - startTime > timeLimitMillis) break;
            ChessBoard next = copyBoard(board);
            applyMoveOnBoard(next, move);
            // Negamax: negate the result from opponent's perspective
            int score = -minimax(next, opposite(toMove), depth - 1, -beta, -alpha, gameCtx, startTime);
            value = Math.max(value, score);
            alpha = Math.max(alpha, value);
            if (alpha >= beta) break; // Beta cutoff
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

    private int evaluateBoardFromPerspective(ChessBoard board, ChessColor perspective) {
        int score = evaluateBoard(board); // This returns positive for BLACK, negative for WHITE
        return (perspective == ChessColor.BLACK) ? score : -score;
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

    // Helper class to hold move and score
    private static class MoveScore {
        final Move move;
        final int score;

        MoveScore(Move move, int score) {
            this.move = move;
            this.score = score;
        }
    }
}
