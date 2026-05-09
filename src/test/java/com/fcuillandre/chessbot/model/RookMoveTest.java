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
 * Tests for rook movement rules.
 *
 * @author fcuillandre
 * @since 1.0
 */
class RookMoveTest {

    private ChessBoard board;

    @BeforeEach
    void setUp() {
        board = new ChessBoard();
    }

    @Test
    void rook_canMoveHorizontallyAndVertically() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.D4.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));

        Position rookPos = ChessCaseEnumeration.D4.getPosition();
        List<Move> rookMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(rookPos))
                .toList();

        // Rook in center should have 14 possible moves (7 horizontal + 7 vertical)
        assertEquals(14, rookMoves.size());
    }

    @Test
    void rook_cannotMoveDiagonally() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.D4.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));

        Position rookPos = ChessCaseEnumeration.D4.getPosition();
        List<Move> rookMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(rookPos))
                .toList();

        // All moves should be on the same row or same column
        assertTrue(rookMoves.stream().allMatch(m ->
                m.getTo().row() == rookPos.row() || m.getTo().col() == rookPos.col()));
    }

    @Test
    void rook_blockedByOwnPiece() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.A3.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));

        Position rookPos = ChessCaseEnumeration.A1.getPosition();
        List<Move> rookMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(rookPos))
                .toList();

        // Cannot move to or past a3
        assertFalse(rookMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.A3.getPosition())));
        assertFalse(rookMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.A4.getPosition())));
        // Can move to a2
        assertTrue(rookMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.A2.getPosition())));
    }

    @Test
    void rook_canCaptureEnemyPiece() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.A5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

        Position rookPos = ChessCaseEnumeration.A1.getPosition();
        List<Move> rookMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(rookPos))
                .toList();

        // Can capture on a5 but not beyond
        assertTrue(rookMoves.stream().anyMatch(m ->
                m.getTo().equals(ChessCaseEnumeration.A5.getPosition()) && m.isCapture()));
        assertFalse(rookMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.A6.getPosition())));
    }

    @Test
    void rook_inCorner_hasCorrectMoves() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E7.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.H8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));

        Position rookPos = ChessCaseEnumeration.H8.getPosition();
        List<Move> rookMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(rookPos))
                .toList();

        // 7 horizontal + 7 vertical = 14
        // if king was on E8, it reduces horizontal moves to 3
        assertEquals(14, rookMoves.size());
    }
}

