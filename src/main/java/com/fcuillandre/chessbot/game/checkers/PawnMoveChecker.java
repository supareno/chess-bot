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
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.pieces.ChessPiece;

import static com.fcuillandre.chessbot.pieces.ChessColor.WHITE;
import static com.fcuillandre.chessbot.pieces.ChessPieceType.PAWN;

/**
 * Checks if a pawn's move is valid.
 * The pawn moves forward one square, with the option to move two squares on its first move.
 * It captures diagonally and has special rules for en passant.
 *
 * @author fcuillandre
 * @version 0.1
 */
public final class PawnMoveChecker extends AbstractMoveChecker {

    @Override
    public boolean customIsValidMove(ChessPiece piece, Move move, ChessGame game) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        ChessBoard board = this.board;
        boolean isWhite = piece.getColor() == WHITE;
        int direction = isWhite ? 1 : -1;
        // one case move
        if (startY == endY && endX - startX == direction && board.getPieceAt(endX, endY) == null) {
            return true;
        }
        // First move 2 cases
        if (startY == endY && ((isWhite && startX == 1) || (!isWhite && startX == 6))
                && endX - startX == 2 * direction
                && board.getPieceAt(startX + direction, startY) == null
                && board.getPieceAt(endX, endY) == null) {
            return true;
        }
        // Capture
        if (Math.abs(endY - startY) == 1 && endX - startX == direction
                && board.getPieceAt(endX, endY) != null
                && board.getPieceAt(endX, endY).getColor() != piece.getColor()) {
            return true;
        }
        // en passant
        int enPassantRow = isWhite ? 4 : 3;
        if (startX == enPassantRow && Math.abs(endY - startY) == 1 && endX - startX == direction) {
            if (game.getLastMove() != null) {
                Move lastMove = new Move(game.getLastMove().getStart(), game.getLastMove().getEnd());
                int lmStartX = lastMove.getStart().getX();
                int lmEndX = lastMove.getEnd().getX();
                int lmEndY = lastMove.getEnd().getY();
                ChessPiece lastMovedPiece = board.getPieceAt(lmEndX, lmEndY);
                if (lastMovedPiece != null && lastMovedPiece.getType() == PAWN && lastMovedPiece.getColor() != piece.getColor()
                    && Math.abs(lmEndY - startY) == 1 && lmEndX == startX && Math.abs(lmEndX - lmStartX) == 2) {
                    if (board.getPieceAt(endX, endY) == null) {
                        // En passant capture is valid
                        game.setEnPassant(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
