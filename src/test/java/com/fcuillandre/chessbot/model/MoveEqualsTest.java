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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Move#equals(Object)} and {@link Move#hashCode()}.
 * Verifies the fix for the critical bug where two moves between the same squares
 * but with pieces of different colors or types were incorrectly considered equal.
 *
 * @author fcuillandre
 * @since 1.0
 */
class MoveEqualsTest {

    private static final Position FROM = new Position(6, 4); // e2
    private static final Position TO   = new Position(4, 4); // e4

    @Test
    void equals_sameMove_isEqual() {
        ChessPiece piece = new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE);
        Move m1 = new Move(FROM, TO, piece);
        Move m2 = new Move(FROM, TO, piece);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void equals_differentPieceColor_isNotEqual() {
        ChessPiece whitePiece = new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE);
        ChessPiece blackPiece = new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK);

        Move whiteMove = new Move(FROM, TO, whitePiece);
        Move blackMove = new Move(FROM, TO, blackPiece);

        // Before the fix, this was incorrectly equal because only from/to/promotionType
        // were compared, not piece color.
        assertNotEquals(whiteMove, blackMove);
    }

    @Test
    void equals_differentPieceType_isNotEqual() {
        ChessPiece queen  = new ChessPiece(ChessPieceType.QUEEN,  ChessColor.WHITE);
        ChessPiece rook   = new ChessPiece(ChessPieceType.ROOK,   ChessColor.WHITE);

        Move queenMove = new Move(FROM, TO, queen);
        Move rookMove  = new Move(FROM, TO, rook);

        assertNotEquals(queenMove, rookMove);
    }

    @Test
    void equals_differentPromotionType_isNotEqual() {
        ChessPiece pawn = new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE);
        Position promotionTo = new Position(0, 4); // e8

        Move queenPromo  = Move.createPromotion(FROM, promotionTo, pawn, null, ChessPieceType.QUEEN);
        Move rookPromo   = Move.createPromotion(FROM, promotionTo, pawn, null, ChessPieceType.ROOK);

        assertNotEquals(queenPromo, rookPromo);
    }

    @Test
    void equals_samePromotionType_isEqual() {
        ChessPiece pawn = new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE);
        Position promotionTo = new Position(0, 4); // e8

        Move m1 = Move.createPromotion(FROM, promotionTo, pawn, null, ChessPieceType.QUEEN);
        Move m2 = Move.createPromotion(FROM, promotionTo, pawn, null, ChessPieceType.QUEEN);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void hashCode_consistentWithEquals_differentColor() {
        ChessPiece whitePiece = new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE);
        ChessPiece blackPiece = new ChessPiece(ChessPieceType.KNIGHT, ChessColor.BLACK);

        Move whiteMove = new Move(FROM, TO, whitePiece);
        Move blackMove = new Move(FROM, TO, blackPiece);

        // hashCodes should differ (not strictly required by contract, but expected for correctness)
        assertNotEquals(whiteMove.hashCode(), blackMove.hashCode());
    }

    @Test
    void equals_withSelf_isTrue() {
        ChessPiece piece = new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE);
        Move move = new Move(FROM, TO, piece);

        // reflexive: a.equals(a) must be true
        assertTrue(move.equals(move));
    }

    @Test
    void equals_withNull_isFalse() {
        ChessPiece piece = new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE);
        Move move = new Move(FROM, TO, piece);

        assertNotEquals(null, move);
    }
}

