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
package com.fcuillandre.chessbot.game;

import com.fcuillandre.chessbot.board.ChessCaseEnumeration;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a coordinate on the chess board.
 * The board is an 8x8 grid with coordinates ranging from (0,0) to (7,7).
 * (0,0) corresponds to the bottom-left corner (a1 in chess notation),
 * and (7,7) corresponds to the top-right corner (h8 in chess notation).
 *
 * @author fcuillandre
 * @version 0.1
 */
@Getter
@EqualsAndHashCode
public final class Coordinate {

    private final int x;
    private final int y;

    /**
     * Constructs a Coordinate with the specified x and y values.
     *
     * @param x The x-coordinate (0-7).
     * @param y The y-coordinate (0-7).
     * @throws IndexOutOfBoundsException if x or y are out of bounds.
     */
    public Coordinate(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Coordinate from a ChessCaseEnumeration.
     * @param chessCase The chess case enumeration.
     */
    public Coordinate(ChessCaseEnumeration chessCase) {
        this(chessCase.getCoordinate().getX(), chessCase.getCoordinate().getY());
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
