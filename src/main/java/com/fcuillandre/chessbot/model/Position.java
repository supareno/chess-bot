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
 * Represent a position on the chessboard with row and column indices.
 * Row and column are 0-indexed (0-7).
 *
 * @author fcuillandre
 * @version 1.0
 */
public record Position(int row, int col) {

    /**
     * Creates a Position from algebraic notation (e.g., "e4").
     *
     * @param notation the algebraic notation string
     * @return a Position object corresponding to the given notation
     */
    public static Position fromAlgebraic(String notation) {
        if (notation == null || notation.length() != 2) {
            throw new IllegalArgumentException("Invalid notation: " + notation);
        }
        char colChar = notation.charAt(0);
        char rowChar = notation.charAt(1);
        int col = colChar - 'a';
        int row = 8 - (rowChar - '0');
        return new Position(row, col);
    }

    /**
     * Returns true if the position is within the bounds of the chessboard.
     *
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /**
     * Creates a new Position by applying the given row and column offsets to this position.
     *
     * @param rowOffset the number of rows to move (positive for down, negative for up)
     * @param colOffset the number of columns to move (positive for right, negative for left)
     * @return a new Position with the applied offsets
     */
    public Position offset(int rowOffset, int colOffset) {
        return new Position(row + rowOffset, col + colOffset);
    }

    /**
     * Converts this position to algebraic notation (e.g., "e4").
     *
     * @return the algebraic notation string for this position
     */
    public String toAlgebraic() {
        char colChar = (char) ('a' + col);
        int rowNum = 8 - row;
        return "" + colChar + rowNum;
    }

    @Override
    public String toString() {
        return toAlgebraic();
    }
}
