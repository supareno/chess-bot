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
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessColor;

import java.util.List;

/**
 * Minimax-based chess bot with alpha-beta pruning.
 * <p>
 * Uses a simple material evaluation and searches to a fixed depth. This implementation operates on board copies
 * to avoid mutating the game state.
 * </p>
 *
 * @author fcuillandre
 * @version 0.1
 */
public final class MinimaxChessBot extends AbstractMinimaxChessBot {

    /*
     * Depth is set to 4 plies (2 full moves).
     * Higher depths include latency and performance considerations and the application cannot play.
     */
    private static final int MAX_DEPTH = 4;

    @Override
    public Move getMove(ChessGame game) {
        // Use opening book if available
        Move bookMove = SimpleOpeningBook.getBookMove(game);
        if (bookMove != null) {
            return bookMove;
        }
        ChessColor toMove = ChessColor.BLACK;
        List<Move> legalMoves = generateLegalMovesUsingGame(game, toMove);
        if (legalMoves.isEmpty()) return null;
        int depth = game.isKingInCheck() ? Math.max(1, MAX_DEPTH - 1) : MAX_DEPTH;
        Move best = null;
        int bestScore = Integer.MIN_VALUE;
        ChessBoard board = game.getBoard();
        for (Move m : legalMoves) {
            ChessBoard copy = copyBoard(board);
            applyMoveOnBoard(copy, m);
            int score = minimax(copy, opposite(toMove), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, game);
            if (score > bestScore) {
                bestScore = score;
                best = m;
            }
        }
        return best;
    }

    private int minimax(ChessBoard board, ChessColor toMove, int depth, int alpha, int beta, ChessGame gameCtx) {
        if (depth == 0) {
            return evaluateBoard(board);
        }
        // Terminal checks based on available moves
        List<Move> moves = generateLegalMovesForColor(board, toMove, gameCtx);
        if (moves.isEmpty()) {
            // No legal moves: check if in check -> checkmate, else stalemate
            boolean inCheck = isKingInCheckForColor(board, toMove, gameCtx);
            if (inCheck) {
                // If side to move is checkmated, it's very bad for that side
                return (toMove == ChessColor.BLACK) ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE - 1;
            } else {
                // Stalemate: neutral outcome
                return 0;
            }
        }
        int value = Integer.MIN_VALUE;
        for (Move move : moves) {
            ChessBoard next = copyBoard(board);
            applyMoveOnBoard(next, move);
            int score = minimax(next, opposite(toMove), depth - 1, alpha, beta, gameCtx);
            value = Math.max(value, score);
            alpha = Math.max(alpha, value);
            if (alpha >= beta) break;
        }
        return value;
    }


}
