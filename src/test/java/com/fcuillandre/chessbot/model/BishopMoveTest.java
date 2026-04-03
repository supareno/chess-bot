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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for bishop movement rules.
 *
 * @author fcuillandre
 * @since 1.0
 */
class BishopMoveTest {

    private ChessBoard board;

    @BeforeEach
    void setUp() {
        board = new ChessBoard();
    }

    @Nested
    class BasicMovement {

        @Test
        void bishop_canMoveDiagonally() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));

            Position bishopPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> bishopMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(bishopPos))
                    .toList();

            // Bishop in center should have 13 possible moves
            assertEquals(13, bishopMoves.size());
        }

        @Test
        void bishop_cannotMoveHorizontallyOrVertically() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));

            Position bishopPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> bishopMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(bishopPos))
                    .toList();

            // No moves should be horizontal (same row) or vertical (same column)
            assertTrue(bishopMoves.stream().noneMatch(m ->
                    m.getTo().row() == bishopPos.row() || m.getTo().col() == bishopPos.col()));
        }

        @Test
        void bishop_blockedByOwnPiece() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.C1.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.D2.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));

            Position bishopPos = ChessCaseEnumeration.C1.getPosition();
            List<Move> bishopMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(bishopPos))
                    .toList();

            // Bishop blocked on one diagonal by own pawn
            assertFalse(bishopMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.D2.getPosition())));
            assertFalse(bishopMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.E3.getPosition())));
        }

        @Test
        void bishop_canCaptureEnemyPiece() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.C1.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E3.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

            Position bishopPos = ChessCaseEnumeration.C1.getPosition();
            List<Move> bishopMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(bishopPos))
                    .toList();

            // Can capture on e3 but not beyond
            assertTrue(bishopMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.E3.getPosition()) && m.isCapture()));
            assertFalse(bishopMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.F4.getPosition())));
        }
    }
}

