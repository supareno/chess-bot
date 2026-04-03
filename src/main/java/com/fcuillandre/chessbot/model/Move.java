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
package com.fcuillandre.chessbot.model;

import lombok.Getter;

/**
 * Represents a chess move.
 * A move consists of a starting position, an ending position, the piece being moved,
 * any piece that is captured, and flags for special moves like castling, en passant, and promotion.
 *
 * @author fcuillandre
 * @since 0.1
 */
@Getter
public final class Move {

    private final Position from;
    private final Position to;
    private final ChessPiece piece;
    private final ChessPiece capturedPiece;
    private final boolean isCastling;
    private final boolean isEnPassant;
    private final ChessPieceType promotionType;

    /**
     * Constructs a Move with the specified starting and ending positions, and the piece being moved.
     *
     * @param from  the starting position of the move
     * @param to    the ending position of the move
     * @param piece the piece being moved
     */
    public Move(Position from, Position to, ChessPiece piece) {
        this(from, to, piece, null, false, false, null);
    }

    /**
     * Constructs a Move with the specified starting and ending positions, the piece being moved, and any piece that is
     * captured.
     *
     * @param from          the starting position of the move
     * @param to            the ending position of the move
     * @param piece         the piece being moved
     * @param capturedPiece the piece that is captured by this move, or null if no piece is captured
     */
    public Move(Position from, Position to, ChessPiece piece, ChessPiece capturedPiece) {
        this(from, to, piece, capturedPiece, false, false, null);
    }

    /**
     * Constructs a Move with the specified starting and ending positions, the piece being moved, any piece that is
     * captured, and flags for special moves.
     *
     * @param from          the starting position of the move
     * @param to            the ending position of the move
     * @param piece         the piece being moved
     * @param capturedPiece the piece that is captured by this move, or null if no piece is captured
     * @param isCastling    true if this move is a castling move, false otherwise
     * @param isEnPassant   true if this move is an en passant capture, false otherwise
     * @param promotionType the type of piece to promote to if this move is a promotion, or null if this move is not a
     *                      promotion
     */
    public Move(Position from, Position to, ChessPiece piece, ChessPiece capturedPiece,
                boolean isCastling, boolean isEnPassant, ChessPieceType promotionType) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.capturedPiece = capturedPiece;
        this.isCastling = isCastling;
        this.isEnPassant = isEnPassant;
        this.promotionType = promotionType;
    }

    // Factory methods

    /**
     * Creates a new Move representing a castling move.
     *
     * @param from the starting position of the king
     * @param to   the ending position of the king
     * @param king the king piece being moved
     * @return a new Move object representing the castling move
     */
    public static Move createCastling(Position from, Position to, ChessPiece king) {
        return new Move(from, to, king, null, true, false, null);
    }

    /**
     * Creates a new Move representing an en passant capture.
     *
     * @param from         the starting position of the pawn performing the en passant capture
     * @param to           the ending position of the pawn performing the en passant capture
     * @param pawn         the pawn piece being moved
     * @param capturedPawn the pawn piece being captured by the en passant move
     * @return a new Move object representing the en passant capture
     */
    public static Move createEnPassant(Position from, Position to, ChessPiece pawn, ChessPiece capturedPawn) {
        return new Move(from, to, pawn, capturedPawn, false, true, null);
    }

    /**
     * Creates a new Move representing a promotion.
     *
     * @param from          the starting position of the pawn being promoted
     * @param to            the ending position of the pawn being promoted
     * @param pawn          the pawn piece being moved and promoted
     * @param capturedPiece the piece being captured by the promotion move, or null if no piece is captured
     * @param promotionType the type of piece to promote to (e.g., QUEEN, ROOK, BISHOP, KNIGHT)
     * @return a new Move object representing the promotion
     */
    public static Move createPromotion(Position from, Position to, ChessPiece pawn,
                                       ChessPiece capturedPiece, ChessPieceType promotionType) {
        return new Move(from, to, pawn, capturedPiece, false, false, promotionType);
    }

    public boolean isPromotion() {
        return promotionType != null;
    }

    public boolean isCapture() {
        return capturedPiece != null;
    }

    /**
     * Creates a new move with a different promotion type.
     */
    public Move withPromotion(ChessPieceType newPromotionType) {
        return new Move(from, to, piece, capturedPiece, isCastling, isEnPassant, newPromotionType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(piece.getType().getNotation());
        sb.append(from.toAlgebraic());
        sb.append(isCapture() ? "x" : "-");
        sb.append(to.toAlgebraic());
        if (isPromotion()) {
            sb.append("=").append(promotionType.getNotation());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Move other)) return false;
        return from.equals(other.from)
                && to.equals(other.to)
                && piece.getType() == other.piece.getType()
                && piece.getColor() == other.piece.getColor()
                && promotionType == other.promotionType;
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + piece.getType().hashCode();
        result = 31 * result + piece.getColor().hashCode();
        if (promotionType != null) {
            result = 31 * result + promotionType.hashCode();
        }
        return result;
    }
}
