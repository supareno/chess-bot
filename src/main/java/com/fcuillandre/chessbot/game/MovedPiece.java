package com.fcuillandre.chessbot.game;

import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a move made by a piece, including all relevant information for algebraic notation and display.
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

