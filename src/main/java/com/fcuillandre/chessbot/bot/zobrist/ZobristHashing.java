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

package com.fcuillandre.chessbot.bot.zobrist;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;

import java.util.Random;

/**
 * Provides Zobrist hashing functionality for chess board positions.
 * <p>
 * Zobrist hashing is used to efficiently compute a unique hash for a given chess board state,
 * which is useful for transposition tables and detecting repeated positions.
 * </p>
 * <p>
 * This class is immutable and cannot be extended.
 * </p>
 *
 * @author fcuillandre
 * @version 0.1
 */
public final class ZobristHashing {

    /**
     * Zobrist keys for each square and piece type.
     * zobristKeys[x][y][pieceIndex] gives the random value for a piece at (x, y).
     */
    private static final long[][][] zobristKeys = new long[8][8][12];

    /**
     * Random number generator for initializing Zobrist keys.
     */
    private static final Random random = new Random();

    // Static initializer to fill zobristKeys with random values.
    static {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 12; k++) {
                    zobristKeys[i][j][k] = random.nextLong();
                }
            }
        }
    }

    /**
     * Computes the Zobrist hash for the given chess board.
     *
     * @param board the chess board to hash
     * @return the Zobrist hash value representing the board state
     */
    public static long computeHash(ChessBoard board) {
        long hash = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    hash ^= zobristKeys[i][j][getPieceIndex(piece)];
                }
            }
        }
        return hash;
    }

    /**
     * Returns the index for the given piece, used for Zobrist key lookup.
     * <ul>
     *     <li>White pieces: indices 6-11</li>
     *     <li>Black pieces: indices 0-5</li>
     *     <li>Order: PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING</li>
     * </ul>
     *
     * @param piece the chess piece
     * @return the index corresponding to the piece type and color
     */
    private static int getPieceIndex(ChessPiece piece) {
        int index = 0;
        if (piece.getColor() == ChessColor.WHITE) {
            index += 6;
        }
        switch (piece.getType()) {
            case PAWN:
                index += 0;
                break;
            case KNIGHT:
                index += 1;
                break;
            case BISHOP:
                index += 2;
                break;
            case ROOK:
                index += 3;
                break;
            case QUEEN:
                index += 4;
                break;
            case KING:
                index += 5;
                break;
        }
        return index;
    }
}
