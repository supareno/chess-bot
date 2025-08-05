package com.fcuillandre.chessbot.game;

import com.fcuillandre.chessbot.board.ChessCaseEnumeration;
import lombok.Getter;

/**
 * Represents a move in a chess game, defined by starting and ending coordinates.
 * This class is immutable and provides methods to access the coordinates of the move.
 *
 * @author FCuillandre
 * @version 1.0
 */
@Getter
public final class Move {

    private final Coordinate start;
    private final Coordinate end;

    /**
     * Constructs a Move object with specified start and end chess cases.
     *
     * @param start the starting chess case of the move
     * @param end   the ending chess case of the move
     */
    public Move(ChessCaseEnumeration start, ChessCaseEnumeration end) {
        this(start.getCoordinate(), end.getCoordinate());
    }

    /**
     * Constructs a Move object with specified start and end coordinates.
     *
     * @param start the starting coordinate of the move
     * @param end   the ending coordinate of the move
     */
    public Move(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }

}

