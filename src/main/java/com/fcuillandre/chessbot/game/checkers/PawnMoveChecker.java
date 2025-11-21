package com.fcuillandre.chessbot.game.checkers;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.utils.ChessUtils;

import static com.fcuillandre.chessbot.pieces.ChessColor.WHITE;
import static com.fcuillandre.chessbot.pieces.ChessPieceType.PAWN;

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
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        ChessBoard board = game.getBoard();
        boolean isWhite = piece.getColor() == WHITE;
        int direction = isWhite ? 1 : -1;
        // Déplacement simple d'une case
        if (startY == endY && endX - startX == direction && board.getPieceAt(endX, endY) == null) {
            return true;
        }
        // Premier déplacement de deux cases
        if (startY == endY && ((isWhite && startX == 1) || (!isWhite && startX == 6))
                && endX - startX == 2 * direction
                && board.getPieceAt(startX + direction, startY) == null
                && board.getPieceAt(endX, endY) == null) {
            return true;
        }
        // Prise en diagonale
        if (Math.abs(endY - startY) == 1 && endX - startX == direction
                && board.getPieceAt(endX, endY) != null
                && board.getPieceAt(endX, endY).getColor() != piece.getColor()) {
            return true;
        }
        // Prise en passant
        int enPassantRow = isWhite ? 4 : 3;
        if (startX == enPassantRow && Math.abs(endY - startY) == 1 && endX - startX == direction) {
            Move lastMove = new Move(  game.getLastMove().getStart(), game.getLastMove().getEnd());
            if (lastMove != null) {
                int lmStartX = lastMove.getStart().getX();
                int lmStartY = lastMove.getStart().getY();
                int lmEndX = lastMove.getEnd().getX();
                int lmEndY = lastMove.getEnd().getY();
                ChessPiece lastMovedPiece = board.getPieceAt(lmEndX, lmEndY);
                if (lastMovedPiece != null && lastMovedPiece.getType() == PAWN && lastMovedPiece.getColor() != piece.getColor()
                    && Math.abs(lmEndY - startY) == 1 && lmEndX == startX && Math.abs(lmEndX - lmStartX) == 2) {
                    if (board.getPieceAt(endX, endY) == null) {
                        // Marque la prise en passant dans ChessGame
                        game.setEnPassant(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
