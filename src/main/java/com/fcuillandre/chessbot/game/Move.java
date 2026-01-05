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
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import lombok.Getter;

/**
 * Represents a move in a chess game, defined by starting and ending coordinates.
 * This class is immutable and provides methods to access the coordinates of the move.
 *
 * @author fcuillandre
 * @version 0.1
 */
@Getter
public final class Move {

    private final Coordinate start;
    private final Coordinate end;
    private final ChessPieceType promotionPieceType; // null if not a promotion

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
        this.promotionPieceType = null;
    }

    /**
     * Constructs a Move object with specified start and end coordinates and a promotion piece type.
     *
     * @param start              the starting coordinate of the move
     * @param end                the ending coordinate of the move
     * @param promotionPieceType the type of piece to promote to, if applicable
     */
    public Move(Coordinate start, Coordinate end, ChessPieceType promotionPieceType) {
        this.start = start;
        this.end = end;
        this.promotionPieceType = promotionPieceType;
    }

    @Override
    public String toString() {
        return String.format("Move{start=%s, end=%s, promotion=%s}",
                ChessCaseEnumeration.getByCoordinate(start),
                ChessCaseEnumeration.getByCoordinate(end),
                promotionPieceType);
    }
}
