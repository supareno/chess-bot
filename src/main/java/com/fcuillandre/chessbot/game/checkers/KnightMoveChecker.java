package com.fcuillandre.chessbot.game.checkers;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.ChessGame;

/**
 * KnightMoveChecker implements the MoveChecker interface to validate moves for knight pieces in chess.
 * It checks if a knight's move adheres to the rules of chess, specifically the unique "L" shape movement.
 *
 * @author FCuillandre
 * @version 1.0
 */
public class KnightMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        ChessBoard board = this.board; // use injected board
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        // L move: 2 in one direction and 1 in the other
        if (!((dx == 2 && dy == 1) || (dx == 1 && dy == 2))) return false;
        // Destination square must be empty or occupied by opponent
        ChessPiece dest = board.getPieceAt(endX, endY);
        return dest == null || dest.getColor() != piece.getColor();
    }
}
