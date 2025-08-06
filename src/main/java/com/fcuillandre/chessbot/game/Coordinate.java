package com.fcuillandre.chessbot.game;

import lombok.Getter;

@Getter
public final class Coordinate {

    private final int x;
    private final int y;

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
