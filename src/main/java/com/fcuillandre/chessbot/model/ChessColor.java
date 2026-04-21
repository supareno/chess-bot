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
package com.fcuillandre.chessbot.model;

/**
 * Represents the color of a piece or a player.
 * Provides utility methods related to color, such as getting the opposite color and pawn movement direction.
 *
 * @author fcuillandre
 * @since 0.1
 */
public enum ChessColor {

    WHITE,
    BLACK;

    /**
     * Returns the opposite color.
     *
     * @return the opposite color (BLACK for WHITE, WHITE for BLACK)
     */
    public ChessColor opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    /**
     * Returns the pawn movement direction (1 for white moving up, -1 for black moving down).
     *
     * @return the pawn movement direction
     */
    public int getPawnDirection() {
        return this == WHITE ? -1 : 1;
    }

    /**
     * Returns the starting row of pawns.
     *
     * @return the starting row of pawns (6 for white, 1 for black)
     */
    public int getPawnStartRow() {
        return this == WHITE ? 6 : 1;
    }

    /**
     * Returns the promotion row of pawns.
     *
     * @return the promotion row of pawns (0 for white, 7 for black)
     */
    public int getPromotionRow() {
        return this == WHITE ? 0 : 7;
    }

    /**
     * Returns the starting row of the king (for castling).
     *
     * @return the starting row of the king (7 for white, 0 for black)
     */
    public int getBackRank() {
        return this == WHITE ? 7 : 0;
    }
}
