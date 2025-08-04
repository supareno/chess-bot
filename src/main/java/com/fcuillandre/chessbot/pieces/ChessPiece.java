package com.fcuillandre.chessbot.pieces;

import lombok.Getter;

/**
 * Represents a chess piece with its color and type.
 * This class is immutable and provides methods to access the piece's color and type.
 *
 * @author FCuillandre
 * @version 1.0
 */
@Getter
public final class ChessPiece {

    private final ChessColor color;
    private final ChessPieceType type;

    public ChessPiece(ChessColor color, ChessPieceType type) {
        this.color = color;
        this.type = type;
    }

    @Override
    public String toString() {
        return getColor() == ChessColor.WHITE ? getType().getShortName() : getType().getShortName().toLowerCase();
    }

}
