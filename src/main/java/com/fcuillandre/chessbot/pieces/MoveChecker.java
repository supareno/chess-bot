package com.fcuillandre.chessbot.pieces;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.Move;

/**
 * Interface for checking the validity of chess piece moves.
 * Implementations should define the rules for each type of chess piece.
 *
 * @author FCuillandre
 * @version 1.0
 */
public interface MoveChecker {

    /**
     * Checks if a move is valid for a given chess piece.
     *
     * @param piece The chess piece to check.
     * @param move The move to check.
     * @param board The current state of the chess board.
     * @return true if the move is valid, false otherwise.
     */
    boolean isValidMove(ChessPiece piece, Move move, ChessBoard board);
}
