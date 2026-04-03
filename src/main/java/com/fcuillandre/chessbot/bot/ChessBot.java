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
package com.fcuillandre.chessbot.bot;

import com.fcuillandre.chessbot.engine.GameEngine;
import com.fcuillandre.chessbot.model.ChessBoard;
import com.fcuillandre.chessbot.model.ChessColor;
import com.fcuillandre.chessbot.model.Move;
import lombok.Getter;

import java.util.List;

/**
 * ChessBot class implementing a simple Minimax algorithm with alpha-beta pruning to find the best move for the bot.
 *
 * @author fcuillandre
 * @since 1.0
 */
public class ChessBot {

    @Getter
    private final ChessColor color;
    private final int maxDepth;
    private final PositionEvaluator evaluator;

    public ChessBot(ChessColor color, int depth) {
        this.color = color;
        this.maxDepth = depth;
        this.evaluator = new PositionEvaluator();
    }

    /**
     * Find the best move for the bot using Minimax with alpha-beta pruning.
     *
     * @return The best move found, or null if no legal moves are available (checkmate or stalemate).
     */
    public Move findBestMove(GameEngine engine) {
        ChessBoard board = engine.getBoard().copy();
        List<Move> legalMoves = board.getAllLegalMoves(color);

        if (legalMoves.isEmpty()) {
            return null;
        }

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Sort moves to improve alpha-beta pruning efficiency (captures and promotions first)
        legalMoves.sort((m1, m2) -> {
            int score1 = getMoveOrderScore(m1);
            int score2 = getMoveOrderScore(m2);
            return score2 - score1;
        });

        for (Move move : legalMoves) {
            ChessBoard testBoard = board.copy();
            testBoard.executeMove(move);

            int score = minimax(testBoard, maxDepth - 1, alpha, beta, false, color.opposite());

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = bestScore; // tighten the window for subsequent siblings
            }
        }

        return bestMove;
    }

    /**
     * Minimax algorithm with alpha-beta pruning.
     *
     * @param board         The current chess board state.
     * @param depth         The remaining depth to search.
     * @param alpha         The alpha value for pruning.
     * @param beta          The beta value for pruning.
     * @param maximizing    True if the current player is the maximizing player, false otherwise.
     * @param currentPlayer The color of the current player.
     * @return The evaluated score of the board state.
     */
    private int minimax(ChessBoard board, int depth, int alpha, int beta, boolean maximizing, ChessColor currentPlayer) {
        List<Move> legalMoves = board.getAllLegalMoves(currentPlayer);

        if (legalMoves.isEmpty()) {
            if (board.isKingInCheck(currentPlayer)) {
                // Mate
                return maximizing ? -100000 + (maxDepth - depth) : 100000 - (maxDepth - depth);
            } else {
                // Stalemate
                return 0;
            }
        }

        if (depth == 0) {
            return evaluator.evaluate(board, color);
        }

        // Sort moves to improve pruning efficiency (captures and promotions first)
        legalMoves.sort((m1, m2) -> {
            int score1 = getMoveOrderScore(m1);
            int score2 = getMoveOrderScore(m2);
            return score2 - score1;
        });

        if (maximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : legalMoves) {
                ChessBoard testBoard = board.copy();
                testBoard.executeMove(move);
                int eval = minimax(testBoard, depth - 1, alpha, beta, false, currentPlayer.opposite());
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : legalMoves) {
                ChessBoard testBoard = board.copy();
                testBoard.executeMove(move);
                int eval = minimax(testBoard, depth - 1, alpha, beta, true, currentPlayer.opposite());
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cut-off
                }
            }
            return minEval;
        }
    }

    /**
     * Scores moves for move ordering (captures and promotions are prioritized).
     * Captures are scored based on the value of the captured piece minus the value of the attacking piece (MVV-LVA).
     * Promotions are scored based on the value of the promotion piece.
     *
     * @param move The move to score.
     * @return The score for move ordering.
     */
    private int getMoveOrderScore(Move move) {
        int score = 0;

        if (move.isCapture()) {
            // MVV-LVA (Most Valuable Victim - Least Valuable Attacker)
            score += 10 * move.getCapturedPiece().getValue() - move.getPiece().getValue();
        }

        if (move.isPromotion()) {
            score += move.getPromotionType().getValue();
        }

        return score;
    }
}
