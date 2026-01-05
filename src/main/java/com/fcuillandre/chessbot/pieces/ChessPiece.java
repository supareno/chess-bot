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

package com.fcuillandre.chessbot.pieces;

import lombok.Getter;

/**
 * Represents a chess piece with its color and type.
 * This class is immutable and provides methods to access the piece's color and type.
 *
 * @author fcuillandre
 * @version 0.1
 */
@Getter
public final class ChessPiece {

    private final ChessColor color;
    private final ChessPieceType type;

    /**
     * Constructs a ChessPiece with the specified color and type.
     *
     * @param color the color of the chess piece (WHITE or BLACK)
     * @param type  the type of the chess piece (e.g., PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING)
     */
    public ChessPiece(ChessColor color, ChessPieceType type) {
        this.color = color;
        this.type = type;
    }
}
