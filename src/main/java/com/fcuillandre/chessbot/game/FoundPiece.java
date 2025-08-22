package com.fcuillandre.chessbot.game;

import com.fcuillandre.chessbot.pieces.ChessPiece;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FoundPiece {
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
