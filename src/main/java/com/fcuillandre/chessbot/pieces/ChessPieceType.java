package com.fcuillandre.chessbot.pieces;

import lombok.Getter;

/**
 * Enum representing the different types of chess pieces.
 * Each piece type has a short name used in chess notation.
 *
 * @author fcuillan
 */
@Getter
public enum ChessPieceType {

    ROOK("R"),
    KNIGHT("N"),
    BISHOP("B"),
    QUEEN("Q"),
    KING("K"),
    PAWN("P");

    private final String shortName;

    ChessPieceType(String r) {
        this.shortName = r;
    }
}
