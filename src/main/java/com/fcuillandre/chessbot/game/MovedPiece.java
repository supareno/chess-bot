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
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a move made by a piece, including all relevant information for algebraic notation and display.
 *
 * @author fcuillandre
 * @version 0.1
 */
@Getter
@Setter
public class MovedPiece {

    private ChessPiece piece;
    private Coordinate start;
    private Coordinate end;
    private boolean isCapture;
    private boolean isCheck;
    private boolean isCheckmate;
    private boolean isStalemate = false;
    private boolean isCastleKingSide;
    private boolean isCastleQueenSide;
    private boolean isEnPassant;
    private ChessPieceType promotionPieceType;

    public MovedPiece(ChessPiece piece, Coordinate start, Coordinate end,
                      boolean isCapture, boolean isCheck, boolean isCheckmate,
                      boolean isCastleKingSide, boolean isCastleQueenSide,
                      boolean isEnPassant, ChessPieceType promotionPieceType) {
        this.piece = piece;
        this.start = start;
        this.end = end;
        this.isCapture = isCapture;
        this.isCheck = isCheck;
        this.isCheckmate = isCheckmate;
        this.isCastleKingSide = isCastleKingSide;
        this.isCastleQueenSide = isCastleQueenSide;
        this.isEnPassant = isEnPassant;
        this.promotionPieceType = promotionPieceType;
    }
}
