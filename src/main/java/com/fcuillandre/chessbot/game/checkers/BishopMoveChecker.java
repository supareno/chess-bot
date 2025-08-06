package com.fcuillandre.chessbot.game.checkers;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessPiece;

/**
 * Checks if a bishop's move is valid.
 * The bishop moves only diagonally and cannot jump over other pieces.
 * It can capture an opponent's piece by moving onto its square.
 *
 * @author FCuillandre
 * @version 1.0
 */
public final class BishopMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        ChessBoard board = game.getBoard();
        // Le fou se déplace uniquement en diagonale
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        if (dx != dy || dx == 0) return false;
        int stepX = (endX - startX) > 0 ? 1 : -1;
        int stepY = (endY - startY) > 0 ? 1 : -1;
        int x = startX + stepX;
        int y = startY + stepY;
        while (x != endX && y != endY) {
            if (board.getPieceAt(x, y) != null) return false;
            x += stepX;
            y += stepY;
        }
        // Vérifie la case d'arrivée
        return board.getPieceAt(endX, endY) == null || board.getPieceAt(endX, endY).getColor() != piece.getColor();
    }
}
