/*
 * Copyright 2025-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * @author fcuillandre
 * @version 0.1
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
