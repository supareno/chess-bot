package com.fcuillandre.chessbot.game.checkers;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessPiece;

/**
 * Vérifie si le mouvement d'une tour est valide.
 * La tour se déplace uniquement en ligne droite (horizontal ou vertical) et ne peut pas sauter par-dessus d'autres pièces.
 * Elle peut capturer une pièce adverse en se déplaçant sur sa case.
 *
 * @author FCuillandre
 * @version 1.0
 */
public final class RookMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {

        // La tour se déplace uniquement en ligne droite (horizontal ou vertical)
        if (startX != endX && startY != endY) return false;
        // Vérifie qu'il n'y a pas d'obstacle sur le chemin
        if (startX == endX) {
            int minY = Math.min(startY, endY) + 1;
            int maxY = Math.max(startY, endY);
            for (int y = minY; y < maxY; y++) {
                if (board.getPieceAt(startX,y) != null) return false;
            }
        } else {
            int minX = Math.min(startX, endX) + 1;
            int maxX = Math.max(startX, endX);
            for (int x = minX; x < maxX; x++) {
                if (board.getPieceAt(x,startY) != null) return false;
            }
        }
        // Vérifie la case d'arrivée
        return board.getPieceAt(endX,endY) == null || board.getPieceAt(endX,endY).getColor() != piece.getColor();
    }
}
