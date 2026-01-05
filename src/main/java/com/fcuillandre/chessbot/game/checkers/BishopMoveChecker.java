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
 * Checks if a bishop's move is valid.
 * The bishop moves only diagonally and cannot jump over other pieces.
 * It can capture an opponent's piece by moving onto its square.
 *
 * @author fcuillandre
 * @version 0.1
 */
public final class BishopMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        ChessBoard board = this.board;
        // Diagonal movement check
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        if (dx != dy || dx == 0) return false;
        int stepX = (endX - startX) > 0 ? 1 : -1;
        int stepY = (endY - startY) > 0 ? 1 : -1;
        int x = startX + stepX;
        int y = startY + stepY;
        while (x != endX && y != endY) {
            if (board.getPieceAt(x, y) != null) return false;
            x += stepX;
            y += stepY;
        }
        // Destination square check
        return board.getPieceAt(endX, endY) == null || board.getPieceAt(endX, endY).getColor() != piece.getColor();
    }
}
