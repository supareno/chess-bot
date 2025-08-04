package com.fcuillandre.chessbot.pieces;

public final class ChessPiece {

    private ChessColor color;
    private ChessPieceType type;

    public ChessPiece(ChessColor color, ChessPieceType type) {
        this.color = color;
        this.type = type;
    }

    public ChessColor getColor() {
        return color;
    }

    public ChessPieceType getType() {
        return type;
    }

    @Override
    public String toString() {
        return getColor() == ChessColor.WHITE ? getType().getShortName() : getType().getShortName().toLowerCase();
    }

}
