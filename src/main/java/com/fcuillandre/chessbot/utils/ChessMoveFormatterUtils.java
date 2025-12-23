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
package com.fcuillandre.chessbot.utils;

import com.fcuillandre.chessbot.game.MovedPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import lombok.NoArgsConstructor;

/**
 * Utility class for formatting chess moves in algebraic notation.
 *
 * @author fcuillandre
 * @version 0.1
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ChessMoveFormatterUtils {

    /**
     * Formats a MovedPiece into algebraic chess notation.
     *
     * @param move The MovedPiece to format
     * @return The move in algebraic notation
     */
    public static String formatMove(MovedPiece move) {
        if (move == null || move.getPiece() == null) {
            return "";
        }
        StringBuilder notation = new StringBuilder();
        ChessPieceType type = move.getPiece().getType();
        char fileFrom = (char) ('a' + move.getStart().getY());
        char fileTo = (char) ('a' + move.getEnd().getY());
        int rankTo = move.getEnd().getX() + 1;
        // Castling
        if (move.isCastleKingSide()) return "O-O" + (move.isCheckmate() ? "#" : move.isCheck() ? "+" : "");
        if (move.isCastleQueenSide()) return "O-O-O" + (move.isCheckmate() ? "#" : move.isCheck() ? "+" : "");
        // Piece letter (except pawn)
        if (type != ChessPieceType.PAWN) {
            switch (type) {
                case KING:
                    notation.append(ChessPieceType.KING.getShortName());
                    break;
                case QUEEN:
                    notation.append(ChessPieceType.QUEEN.getShortName());
                    break;
                case ROOK:
                    notation.append(ChessPieceType.ROOK.getShortName());
                    break;
                case BISHOP:
                    notation.append(ChessPieceType.BISHOP.getShortName());
                    break;
                case KNIGHT:
                    notation.append(ChessPieceType.KNIGHT.getShortName());
                    break;
                default:
                    break;
            }
        }
        // Pawn move: file if capture
        if (type == ChessPieceType.PAWN && move.isCapture()) {
            notation.append(fileFrom);
        }
        // Capture
        if (move.isCapture()) {
            notation.append("x");
        }
        // Destination
        notation.append(fileTo).append(rankTo);
        // Promotion
        if (move.getPromotionPieceType() != null) {
            notation.append("=");
            switch (move.getPromotionPieceType()) {
                case QUEEN:
                    notation.append(ChessPieceType.QUEEN.getShortName());
                    break;
                case ROOK:
                    notation.append(ChessPieceType.ROOK.getShortName());
                    break;
                case BISHOP:
                    notation.append(ChessPieceType.BISHOP.getShortName());
                    break;
                case KNIGHT:
                    notation.append(ChessPieceType.KNIGHT.getShortName());
                    break;
                default:
                    break;
            }
        }
        // En passant (optional, not standard in algebraic)
        // if (move.isEnPassant()) notation.append(" e.p.");
        // Check/checkmate
        if (move.isCheckmate()) {
            notation.append("#");
        } else if (move.isCheck()) {
            notation.append("+");
        }
        return notation.toString();
    }
}
