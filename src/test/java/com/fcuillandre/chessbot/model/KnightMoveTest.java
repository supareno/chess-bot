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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for knight movement rules.
 *
 * @author fcuillandre
 * @since 1.0
 */
class KnightMoveTest {

    private ChessBoard board;

    @BeforeEach
    void setUp() {
        board = new ChessBoard();
    }

    @Test
    void knight_movesInLShape() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.D4.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));

        Position knightPos = ChessCaseEnumeration.D4.getPosition();
        List<Move> knightMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(knightPos))
                .toList();

        // Knight in center should have 8 possible moves
        assertEquals(8, knightMoves.size());
    }

    @Test
    void knight_canJumpOverPieces() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.B1.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));
        // Surround knight with pawns
        board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.C1.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.A2.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.B2.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.C2.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));

        Position knightPos = ChessCaseEnumeration.B1.getPosition();
        List<Move> knightMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(knightPos))
                .toList();

        // Knight can jump over surrounding pieces to a3, c3 and d2
        assertEquals(3, knightMoves.size());
        assertTrue(knightMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.A3.getPosition())));
        assertTrue(knightMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.D2.getPosition())));
    }

    @Test
    void knight_cannotLandOnOwnPiece() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.B1.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.A3.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));

        Position knightPos = ChessCaseEnumeration.B1.getPosition();
        List<Move> knightMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(knightPos))
                .toList();

        // Cannot land on a3 where own pawn is
        assertFalse(knightMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.A3.getPosition())));
    }

    @Test
    void knight_canCaptureEnemyPiece() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.D4.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E6.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

        Position knightPos = ChessCaseEnumeration.D4.getPosition();
        List<Move> knightMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(knightPos))
                .toList();

        // Can capture on e6
        assertTrue(knightMoves.stream().anyMatch(m ->
                m.getTo().equals(ChessCaseEnumeration.E6.getPosition()) && m.isCapture()));
    }

    @Test
    void knight_inCorner_hasLimitedMoves() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));

        Position knightPos = ChessCaseEnumeration.A1.getPosition();
        List<Move> knightMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(knightPos))
                .toList();

        // Knight in corner has only 2 moves
        assertEquals(2, knightMoves.size());
    }

    @Test
    void knight_initialPosition_hasTwoMoves() {
        board.setupInitialPosition();

        Position knightPos = ChessCaseEnumeration.B1.getPosition();
        List<Move> knightMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(knightPos))
                .toList();

        // Knight on b1 in initial position has 2 moves (a3, c3)
        assertEquals(2, knightMoves.size());
    }
}

