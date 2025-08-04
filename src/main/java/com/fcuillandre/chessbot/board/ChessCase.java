package com.fcuillandre.chessbot.board;

import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import lombok.Getter;

@Getter
public class ChessCase {

    private Coordinate coordinate;
    private ChessPiece piece;
    private String name;

    public ChessCase(Coordinate coordinate, ChessPiece piece, String name) {
        this.coordinate = coordinate;
        this.piece = piece;
        this.name = name;
    }
}
