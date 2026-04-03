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

import lombok.Getter;

/**
 * Chess piece types.
 *
 * @author fcuillandre
 * @version 0.1
 */
@Getter
public enum ChessPieceType {

    KING("King", "K", 0, '♔', '♚'),
    QUEEN("Queen", "Q", 900, '♕', '♛'),
    ROOK("Rook", "R", 500, '♖', '♜'),
    BISHOP("Bishop", "B", 330, '♗', '♝'),
    KNIGHT("Knight", "N", 320, '♘', '♞'),
    PAWN("Pawn", "", 100, '♙', '♟');

    private final String englishName;
    private final String notation;
    private final int value;
    private final char whiteSymbol;
    private final char blackSymbol;

    /**
     * Constructor for PieceType enum.
     *
     * @param englishName The English name of the piece type (e.g., "King", "Queen").
     * @param notation    The standard chess notation for the piece type (e.g., "K" for King, "Q" for Queen, "" for Pawn).
     * @param value       The relative value of the piece type (e.g., 0 for King, 900 for Queen, etc.).
     * @param whiteSymbol The Unicode character representing the piece for white (e.g., '♔' for King).
     * @param blackSymbol The Unicode character representing the piece for black (e.g., '♚' for King).
     */
    ChessPieceType(String englishName, String notation, int value, char whiteSymbol, char blackSymbol) {
        this.englishName = englishName;
        this.notation = notation;
        this.value = value;
        this.whiteSymbol = whiteSymbol;
        this.blackSymbol = blackSymbol;
    }

    public char getSymbol(ChessColor color) {
        return color == ChessColor.WHITE ? whiteSymbol : blackSymbol;
    }
}
