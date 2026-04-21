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

import com.fcuillandre.chessbot.model.ChessBoard;
import com.fcuillandre.chessbot.model.ChessColor;
import com.fcuillandre.chessbot.model.ChessPiece;
import com.fcuillandre.chessbot.model.ChessPieceType;

/**
 * Position evaluator for the chess bot.
 * Uses an evaluation function based on material and position.
 *
 * @author fcuillandre
 * @since 1.0
 */
public final class PositionEvaluator {

    // Position tables for each piece type (from white's perspective)
    // Positive values favor this position

    private static final int[][] PAWN_TABLE = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5, 5, 10, 25, 25, 10, 5, 5},
            {0, 0, 0, 20, 20, 0, 0, 0},
            {5, -5, -10, 0, 0, -10, -5, 5},
            {5, 10, 10, -20, -20, 10, 10, 5},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };

    private static final int[][] KNIGHT_TABLE = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20, 0, 0, 0, 0, -20, -40},
            {-30, 0, 10, 15, 15, 10, 0, -30},
            {-30, 5, 15, 20, 20, 15, 5, -30},
            {-30, 0, 15, 20, 20, 15, 0, -30},
            {-30, 5, 10, 15, 15, 10, 5, -30},
            {-40, -20, 0, 5, 5, 0, -20, -40},
            {-50, -40, -30, -30, -30, -30, -40, -50}
    };

    private static final int[][] BISHOP_TABLE = {
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-10, 0, 5, 10, 10, 5, 0, -10},
            {-10, 5, 5, 10, 10, 5, 5, -10},
            {-10, 0, 10, 10, 10, 10, 0, -10},
            {-10, 10, 10, 10, 10, 10, 10, -10},
            {-10, 5, 0, 0, 0, 0, 5, -10},
            {-20, -10, -10, -10, -10, -10, -10, -20}
    };

    private static final int[][] ROOK_TABLE = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {5, 10, 10, 10, 10, 10, 10, 5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {0, 0, 0, 5, 5, 0, 0, 0}
    };

    private static final int[][] QUEEN_TABLE = {
            {-20, -10, -10, -5, -5, -10, -10, -20},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-10, 0, 5, 5, 5, 5, 0, -10},
            {-5, 0, 5, 5, 5, 5, 0, -5},
            {0, 0, 5, 5, 5, 5, 0, -5},
            {-10, 5, 5, 5, 5, 5, 0, -10},
            {-10, 0, 5, 0, 0, 0, 0, -10},
            {-20, -10, -10, -5, -5, -10, -10, -20}
    };

    private static final int[][] KING_MIDDLE_GAME_TABLE = {
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            {20, 20, 0, 0, 0, 0, 20, 20},
            {20, 30, 10, 0, 0, 10, 30, 20}
    };

    private static final int[][] KING_END_GAME_TABLE = {
            {-50, -40, -30, -20, -20, -30, -40, -50},
            {-30, -20, -10, 0, 0, -10, -20, -30},
            {-30, -10, 20, 30, 30, 20, -10, -30},
            {-30, -10, 30, 40, 40, 30, -10, -30},
            {-30, -10, 30, 40, 40, 30, -10, -30},
            {-30, -10, 20, 30, 30, 20, -10, -30},
            {-30, -30, 0, 0, 0, 0, -30, -30},
            {-50, -30, -30, -30, -30, -30, -30, -50}
    };

    /**
     * Evaluates a position from the perspective of a color.
     *
     * @param board       The chess board to evaluate
     * @param perspective The color for which to evaluate the position
     * @return Positive score if the position is favorable, negative otherwise
     */
    public int evaluate(ChessBoard board, ChessColor perspective) {
        int whiteScore = 0;
        int blackScore = 0;
        int totalMaterial = 0;

        // Calculate total material to determine game phase
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPieceAt(row, col);
                if (piece != null && piece.getType() != ChessPieceType.KING && piece.getType() != ChessPieceType.PAWN) {
                    totalMaterial += piece.getValue();
                }
            }
        }

        boolean isEndGame = totalMaterial < 1500; // End game if little material

        // Evaluate each piece
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPieceAt(row, col);
                if (piece == null) continue;

                int pieceScore = evaluatePiece(piece, row, col, isEndGame);

                if (piece.getColor() == ChessColor.WHITE) {
                    whiteScore += pieceScore;
                } else {
                    blackScore += pieceScore;
                }
            }
        }

        // Bonus for bishop pairs
        whiteScore += evaluateBishopPair(board, ChessColor.WHITE);
        blackScore += evaluateBishopPair(board, ChessColor.BLACK);

        // Relative score according to perspective
        int score = whiteScore - blackScore;
        return perspective == ChessColor.WHITE ? score : -score;
    }

    /**
     * Evaluates a single piece based on its type, position, and game phase.
     *
     * @param piece     The chess piece to evaluate
     * @param row       The row of the piece on the board
     * @param col       The column of the piece on the board
     * @param isEndGame Whether the game is in the endgame phase
     * @return The score contribution of this piece
     */
    private int evaluatePiece(ChessPiece piece, int row, int col, boolean isEndGame) {
        int score = piece.getValue();

        // Add position bonus
        int positionBonus = getPositionBonus(piece, row, col, isEndGame);
        score += positionBonus;

        return score;
    }

    /**
     * Gets the position bonus for a piece based on its type and location.
     *
     * @param piece     The chess piece to evaluate
     * @param row       The row of the piece on the board
     * @param col       The column of the piece on the board
     * @param isEndGame Whether the game is in the endgame phase
     * @return The position bonus for this piece
     */
    private int getPositionBonus(ChessPiece piece, int row, int col, boolean isEndGame) {
        // For black pieces, we invert the table (vertical mirror)
        int tableRow = piece.getColor() == ChessColor.WHITE ? row : 7 - row;

        return switch (piece.getType()) {
            case PAWN -> PAWN_TABLE[tableRow][col];
            case KNIGHT -> KNIGHT_TABLE[tableRow][col];
            case BISHOP -> BISHOP_TABLE[tableRow][col];
            case ROOK -> ROOK_TABLE[tableRow][col];
            case QUEEN -> QUEEN_TABLE[tableRow][col];
            case KING -> isEndGame ? KING_END_GAME_TABLE[tableRow][col] : KING_MIDDLE_GAME_TABLE[tableRow][col];
        };
    }

    /**
     * Evaluates the bishop pair bonus for a given color.
     *
     * @param board The chess board to evaluate
     * @param color The color to check for bishop pairs
     * @return A bonus score if the color has a bishop pair, 0 otherwise
     */
    private int evaluateBishopPair(ChessBoard board, ChessColor color) {
        int bishopCount = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPieceAt(row, col);
                if (piece != null && piece.getType() == ChessPieceType.BISHOP && piece.getColor() == color) {
                    bishopCount++;
                }
            }
        }

        return bishopCount >= 2 ? 50 : 0;
    }
}
