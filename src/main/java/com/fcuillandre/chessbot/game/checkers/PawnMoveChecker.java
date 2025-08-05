package com.fcuillandre.chessbot.game.checkers;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.utils.ChessUtils;

import static com.fcuillandre.chessbot.pieces.ChessColor.WHITE;

/**
 * Classe pour vérifier les mouvements des pions dans le jeu d'échecs.
 * Implémente l'interface MoveChecker.
 *
 * <p>
 *     Le move e2 e4 correspond à un déplacement de pion blanc de la case e2 (x: 1, y: 3) à la case e4 (x: 3, y: 3).
 *     Le move e7 e5 correspond à un déplacement de pion noir de la case e7 (x: 6, y: 3) à la case e5 (x: 4, y: 3).
 * </p>
 * @author fcuillan
 */
public final class PawnMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {

        // Vérifie la couleur du pion
        boolean isWhite = piece.getColor() == WHITE;
        int direction = isWhite ? -1 : 1;
        // Déplacement simple d'une case
        if (startY == endY && startX - endX == direction && board.getPieceAt(endX,endY) == null) {
            return true;
        }

        // Premier déplacement de deux cases
        if (startY == endY && ((isWhite && startX == 1) || (!isWhite && startX == 6))
                && Math.abs(endX - startX) == Math.abs(2 * direction)
                && board.getPieceAt(endX + direction,startY) == null
                && board.getPieceAt(endX,endY) == null) {
            return true;
        }
        // Prise en diagonale
        if (Math.abs(endX - startX) == 1 && startY - endY == direction
                && board.getPieceAt(endX,endY) != null
                && board.getPieceAt(endX,endY).getColor() != piece.getColor()) {
            return true;
        }
        return false;
    }
}
