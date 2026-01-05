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
 * Checks if a rook's move is valid.
 * The rook moves only in straight lines (horizontal or vertical) and cannot jump over other pieces.
 * It can capture an opponent's piece by moving onto its square.
 *
 * @author fcuillandre
 * @version 0.1
 */
public final class RookMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        ChessBoard board = this.board;
        // Horizontal or vertical movement only
        if (startX != endX && startY != endY) return false;
        // Path must be clear
        if (startX == endX) {
            int minY = Math.min(startY, endY) + 1;
            int maxY = Math.max(startY, endY);
            for (int y = minY; y < maxY; y++) {
                if (board.getPieceAt(startX, y) != null) return false;
            }
        } else {
            int minX = Math.min(startX, endX) + 1;
            int maxX = Math.max(startX, endX);
            for (int x = minX; x < maxX; x++) {
                if (board.getPieceAt(x, startY) != null) return false;
            }
        }
        // Check destination square
        return board.getPieceAt(endX, endY) == null || board.getPieceAt(endX, endY).getColor() != piece.getColor();
    }
}
