package com.fcuillandre.chessbot.utils;

import com.fcuillandre.chessbot.game.MovedPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import lombok.NoArgsConstructor;

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
        int rankFrom = move.getStart().getX() + 1;
        char fileTo = (char) ('a' + move.getEnd().getY());
        int rankTo = move.getEnd().getX() + 1;
        // Castling
        if (move.isCastleKingSide()) return "O-O" + (move.isCheckmate() ? "#" : move.isCheck() ? "+" : "");
        if (move.isCastleQueenSide()) return "O-O-O" + (move.isCheckmate() ? "#" : move.isCheck() ? "+" : "");
        // Piece letter (except pawn)
        if (type != ChessPieceType.PAWN) {
            switch (type) {
                case KING:
                    notation.append("K");
                    break;
                case QUEEN:
                    notation.append("Q");
                    break;
                case ROOK:
                    notation.append("R");
                    break;
                case BISHOP:
                    notation.append("B");
                    break;
                case KNIGHT:
                    notation.append("N");
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
                    notation.append("Q");
                    break;
                case ROOK:
                    notation.append("R");
                    break;
                case BISHOP:
                    notation.append("B");
                    break;
                case KNIGHT:
                    notation.append("N");
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
