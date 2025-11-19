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
