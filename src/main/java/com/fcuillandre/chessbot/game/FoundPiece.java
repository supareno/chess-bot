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

import com.fcuillandre.chessbot.pieces.ChessPiece;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a chess piece found at a specific coordinate on the chessboard.
 *
 * @author fcuillandre
 * @version 0.1
 */
@Getter
@ToString
public final class FoundPiece {

    private final Coordinate coordinate;
    private final ChessPiece piece;

    /**
     * Constructor for FoundPiece.
     *
     * @param coordinate The coordinate where the piece was found.
     * @param piece      The piece that was found.
     */
    public FoundPiece(ChessPiece piece, Coordinate coordinate) {
        this.coordinate = coordinate;
        this.piece = piece;
    }

}
