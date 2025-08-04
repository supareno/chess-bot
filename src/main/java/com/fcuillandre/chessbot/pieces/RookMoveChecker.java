package com.fcuillandre.chessbot.pieces;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.Move;

public final class RookMoveChecker implements MoveChecker {
    /**
     * Vérifie si le mouvement d'une tour est valide.
     *
     * @param piece  La tour à déplacer.
     * @param move   Le mouvement à effectuer.
     * @param board  Le plateau de jeu.
     * @return true si le mouvement est valide, false sinon.
     */
    @Override
    public boolean isValidMove(ChessPiece piece, Move move, ChessBoard board) {
        if (piece == null) return false;
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();
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
