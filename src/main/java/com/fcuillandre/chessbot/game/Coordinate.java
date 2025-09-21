package com.fcuillandre.chessbot.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a coordinate on the chess board.
 * The board is an 8x8 grid with coordinates ranging from (0,0) to (7,7).
 * (0,0) corresponds to the bottom-left corner (a1 in chess notation),
 * and (7,7) corresponds to the top-right corner (h8 in chess notation).
 *
 * @author FCuillandre
 * @version 1.0
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

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
