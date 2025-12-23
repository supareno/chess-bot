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
 * Interface for checking the validity of chess piece moves.
 * Implementations should define the rules for each type of chess piece.
 *
 * @author fcuillandre
 * @version 0.1
 */
public interface MoveChecker {

    /**
     * Checks if a move is valid for a given chess piece.
     *
     * @param piece The chess piece to check.
     * @param move The move to check.
     * @param board The current state of the chess board.
     * @param game The current state of the chess game.
     * @return true if the move is valid, false otherwise.
     */
    boolean isValidMove(ChessPiece piece, Move move, ChessBoard board, ChessGame game);
}
