package com.fcuillandre.chessbot.game.checkers;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessPiece;

/**
 * AbstractMoveChecker provides a base implementation for move checkers in chess.
 * It implements the MoveChecker interface and provides common functionality for move validation.
 * Subclasses should implement the customIsValidMove method to provide specific move validation logic.
 *
 * @author FCuillandre
 * @version 1.0
 */
public abstract class AbstractMoveChecker implements MoveChecker {

    protected int endY;
    protected int endX;
    protected int startY;
    protected int startX;
    protected ChessGame game;
    protected ChessBoard board;

    @Override
    public boolean isValidMove(ChessPiece piece, Move move, ChessBoard board, ChessGame game) {
        // Default implementation, can be overridden by subclasses
        if (piece == null || move == null || board == null || game == null) {
            throw new IllegalArgumentException("Piece, move, board, and game cannot be null");
        }

        this.startX = move.getStart().getX();
        this.startY = move.getStart().getY();
        this.endX = move.getEnd().getX();
        this.endY = move.getEnd().getY();
        this.board = board;
        this.game = game;
        return this.customIsValidMove(piece, move, game); // Default to valid if no specific checks are implemented
    }

    /**
     * Custom validation logic for specific piece moves.
     * This method should be implemented by subclasses to provide specific move validation logic.
     *
     * @param piece The chess piece being moved.
     * @param move The move being validated.
     * @param game The current chess game context.
     * @return true if the move is valid, false otherwise.
     */
    protected abstract boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game);
}
