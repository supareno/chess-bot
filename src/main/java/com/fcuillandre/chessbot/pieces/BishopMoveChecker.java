package com.fcuillandre.chessbot.pieces;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.Move;

public final class BishopMoveChecker implements MoveChecker {

    @Override
    public boolean isValidMove(ChessPiece piece, Move move, ChessBoard board) {
        if (piece == null) return false;
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();
        // Le fou se déplace uniquement en diagonale
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        if (dx != dy || dx == 0) return false;
        int stepX = (endX - startX) > 0 ? 1 : -1;
        int stepY = (endY - startY) > 0 ? 1 : -1;
        int x = startX + stepX;
        int y = startY + stepY;
        while (x != endX && y != endY) {
            if (board.getPieceAt(x,y) != null) return false;
            x += stepX;
            y += stepY;
        }
        // Vérifie la case d'arrivée
        return board.getPieceAt(endX,endY) == null || board.getPieceAt(endX,endY).getColor() != piece.getColor();
    }
}
