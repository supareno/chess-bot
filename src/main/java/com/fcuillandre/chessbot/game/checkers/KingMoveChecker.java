package com.fcuillandre.chessbot.game.checkers;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessPiece;

/**
 * KingMoveChecker vérifie la validité des déplacements du roi.
 * Le roi se déplace d'une case dans n'importe quelle direction.
 *
 * @author FCuillandre
 * @version 1.0
 */
public final class KingMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {

        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        // Mouvement normal du roi
        if ((dx <= 1 && dy <= 1) && !(dx == 0 && dy == 0)) {
            ChessPiece dest = board.getPieceAt(endX, endY);
            return dest == null || dest.getColor() != piece.getColor();
        }
        // Gestion du roque
        if (dy == 0 && dx == 2) {
            boolean isWhite = piece.getColor() == com.fcuillandre.chessbot.pieces.ChessColor.WHITE;
            boolean kingMoved = isWhite ? game.hasWhiteKingMoved() : game.hasBlackKingMoved();
            boolean kingside = endX > startX;
            boolean rookMoved = false;
            int rookY = isWhite ? 0 : 7;
            int rookX = kingside ? 7 : 0;
            if (isWhite) {
                rookMoved = kingside ? game.hasWhiteKingsideRookMoved() : game.hasWhiteQueensideRookMoved();
            } else {
                rookMoved = kingside ? game.hasBlackKingsideRookMoved() : game.hasBlackQueensideRookMoved();
            }
            // Le roi et la tour ne doivent pas avoir bougé
            if (kingMoved || rookMoved) return false;
            // Les cases entre le roi et la tour doivent être libres
            int min = Math.min(startX, rookX) + 1;
            int max = Math.max(startX, rookX) - 1;
            for (int x = min; x <= max; x++) {
                if (board.getPieceAt(x, rookY) != null) return false;
            }
            // La case d'arrivée doit être libre
            if (board.getPieceAt(endX, endY) != null) return false;
            // (Optionnel) : vérifier que le roi ne passe pas par une case attaquée (non implémenté ici)
            return true;
        }
        return false;
    }
}
