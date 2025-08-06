package com.fcuillandre.chessbot.game.checkers;

import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.ChessGame;

/**
 * Checks if a queen's move is valid.
 * The queen can move any number of squares along a rank, file, or diagonal,
 * and cannot jump over other pieces.
 * It can capture an opponent's piece by moving onto its square.
 *
 * @author FCuillandre
 * @version 1.0
 */
public final class QueenMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        ChessBoard board = game.getBoard();
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        // Mouvement en ligne droite (tour)
        if (startX == endX || startY == endY) {
            // Vérifie qu'il n'y a pas d'obstacle
            if (startX == endX) {
                int minY = Math.min(startY, endY) + 1;
                int maxY = Math.max(startY, endY);
                for (int y = minY; y < maxY; y++) {
                    if (board.getPieceAt(startX, y) != null) return false;
                }
            } else {
                int minX = Math.min(startX, endX) + 1;
                int maxX = Math.max(startX, endX);
                for (int x = minX; x < maxX; x++) {
                    if (board.getPieceAt(x, startY) != null) return false;
                }
            }
        }
        // Mouvement en diagonale (fou)
        else if (dx == dy) {
            int stepX = (endX - startX) > 0 ? 1 : -1;
            int stepY = (endY - startY) > 0 ? 1 : -1;
            int x = startX + stepX;
            int y = startY + stepY;
            while (x != endX && y != endY) {
                if (board.getPieceAt(x, y) != null) return false;
                x += stepX;
                y += stepY;
            }
        } else {
            // Ni ligne droite ni diagonale
            return false;
        }
        // Vérifie la case d'arrivée
        ChessPiece dest = board.getPieceAt(endX, endY);
        return dest == null || dest.getColor() != piece.getColor();
    }
}
