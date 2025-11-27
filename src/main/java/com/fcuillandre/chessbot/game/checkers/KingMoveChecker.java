package com.fcuillandre.chessbot.game.checkers;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessPiece;

/**
 * KingMoveChecker validates the king's moves.
 * The king moves one square in any direction (horizontal, vertical, or diagonal).
 * It also handles castling validation including checking that the king does not
 * castle while in check, through check, or into check.
 *
 * @author FCuillandre
 * @version 1.0
 */
public final class KingMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        ChessBoard board = game.getBoard();
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        // Mouvement normal du roi
        if ((dx <= 1 && dy <= 1) && !(dx == 0 && dy == 0)) {
            ChessPiece dest = board.getPieceAt(endX, endY);
            return dest == null || dest.getColor() != piece.getColor();
        }
        // Gestion du roque
        if (dx == 0 && dy == 2) {
            boolean isWhite = piece.getColor() == com.fcuillandre.chessbot.pieces.ChessColor.WHITE;
            boolean kingMoved = isWhite ? game.hasWhiteKingMoved() : game.hasBlackKingMoved();
            boolean kingside = endY > startY;
            boolean rookMoved = false;
            int rookX = isWhite ? 0 : 7;
            int rookY = kingside ? 7 : 0;
            if (isWhite) {
                rookMoved = kingside ? game.hasWhiteKingsideRookMoved() : game.hasWhiteQueensideRookMoved();
            } else {
                rookMoved = kingside ? game.hasBlackKingsideRookMoved() : game.hasBlackQueensideRookMoved();
            }
            // Le roi et la tour ne doivent pas avoir bougé
            if (kingMoved || rookMoved) return false;
            // Les cases entre le roi et la tour doivent être libres
            int min = Math.min(startY, rookY) + 1;
            int max = Math.max(startY, rookY) - 1;
            for (int y = min; y <= max; y++) {
                if (board.getPieceAt(rookX, y) != null) {
                    return false;
                }
            }
            // La case d'arrivée doit être libre
            if (board.getPieceAt(endX, endY) != null) {
                return false;
            }
            return true;
        }
        return false;
    }
}
