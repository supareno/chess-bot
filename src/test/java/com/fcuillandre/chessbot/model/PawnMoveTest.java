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
 * Tests for pawn movement rules including:
 * <ul>
 *   <li>Single and double push from starting position</li>
 *   <li>Diagonal captures</li>
 *   <li>En passant capture</li>
 *   <li>Pawn promotion</li>
 * </ul>
 *
 * @author fcuillandre
 * @since 1.0
 */
class PawnMoveTest {

    private ChessBoard board;

    @BeforeEach
    void setUp() {
        board = new ChessBoard();
    }

    // -------------------------------------------------------------------------
    // Basic pawn movement
    // -------------------------------------------------------------------------

    @Nested
    class BasicMovement {

        @Test
        void whitePawn_fromStartingPosition_canMoveOneOrTwoSquares() {
            board.setupInitialPosition();
            Position pawnPos = ChessCaseEnumeration.E2.getPosition(); // e2

            List<Move> legalMoves = board.getAllLegalMoves(ChessColor.WHITE);
            List<Move> pawnMoves = legalMoves.stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            assertEquals(2, pawnMoves.size(), "Pawn on e2 should have 2 moves (e3 and e4)");
            assertTrue(pawnMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.E3.getPosition())));
            assertTrue(pawnMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.E4.getPosition())));
        }

        @Test
        void blackPawn_fromStartingPosition_canMoveOneOrTwoSquares() {
            board.setupInitialPosition();
            Position pawnPos = ChessCaseEnumeration.E7.getPosition(); // e7

            List<Move> legalMoves = board.getAllLegalMoves(ChessColor.BLACK);
            List<Move> pawnMoves = legalMoves.stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            assertEquals(2, pawnMoves.size(), "Pawn on e7 should have 2 moves (e6 and e5)");
            assertTrue(pawnMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.E6.getPosition())));
            assertTrue(pawnMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.E5.getPosition())));
        }

        @Test
        void pawn_afterMoving_canOnlyMoveOneSquare() {
            board.setupInitialPosition();

            // Move pawn e2-e4
            Move e2e4 = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(ChessCaseEnumeration.E2.getPosition())
                            && m.getTo().equals(ChessCaseEnumeration.E4.getPosition()))
                    .findFirst()
                    .orElseThrow();
            board.executeMove(e2e4);

            // Now from e4, pawn can only move one square
            Position pawnPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            assertEquals(1, pawnMoves.size(), "Pawn on e4 should only be able to move to e5");
            assertEquals(ChessCaseEnumeration.E5.getPosition(), pawnMoves.get(0).getTo());
        }

        @Test
        void pawn_blockedByPiece_cannotMove() {
            // Place white king, white pawn on e4, blocking piece on e5
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

            Position pawnPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            assertEquals(0, pawnMoves.size(), "Pawn blocked by another piece should have no moves");
        }

        @Test
        void pawn_doublePushBlocked_canOnlyMoveOne() {
            // Pawn on e2 (starting), blocking piece on e4
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E2.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

            Position pawnPos = ChessCaseEnumeration.E2.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            assertEquals(1, pawnMoves.size(), "Pawn should only be able to move one square when double push is blocked");
            assertEquals(ChessCaseEnumeration.E3.getPosition(), pawnMoves.get(0).getTo());
        }
    }

    // -------------------------------------------------------------------------
    // Pawn captures
    // -------------------------------------------------------------------------

    @Nested
    class Captures {

        @Test
        void pawn_canCaptureDiagonally() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.D5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.F5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

            Position pawnPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            // Can move forward or capture on d5 or f5
            assertEquals(3, pawnMoves.size());
            assertTrue(pawnMoves.stream().anyMatch(m -> m.isCapture() && m.getTo().equals(ChessCaseEnumeration.D5.getPosition())));
            assertTrue(pawnMoves.stream().anyMatch(m -> m.isCapture() && m.getTo().equals(ChessCaseEnumeration.F5.getPosition())));
        }

        @Test
        void pawn_cannotCaptureOwnPiece() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.D5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));

            Position pawnPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            // Can only move forward
            assertEquals(1, pawnMoves.size());
            assertFalse(pawnMoves.get(0).isCapture());
        }
    }

    // -------------------------------------------------------------------------
    // En passant
    // -------------------------------------------------------------------------

    @Nested
    class EnPassant {

        @Test
        void whitePawn_canCaptureEnPassant() {
            // White pawn on e5, black pawn just double-pushed from d7 to d5
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.D5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));
            board.setEnPassantTarget(ChessCaseEnumeration.D6.getPosition()); // en passant target is d6

            Position pawnPos = ChessCaseEnumeration.E5.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            // Can move forward or capture en passant
            assertEquals(2, pawnMoves.size());
            Move enPassantMove = pawnMoves.stream()
                    .filter(Move::isEnPassant)
                    .findFirst()
                    .orElse(null);

            assertNotNull(enPassantMove, "En passant move should be available");
            assertEquals(ChessCaseEnumeration.D6.getPosition(), enPassantMove.getTo());
            assertTrue(enPassantMove.isCapture());
        }

        @Test
        void blackPawn_canCaptureEnPassant() {
            // Black pawn on d4, white pawn just double-pushed from e2 to e4
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.D4.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            board.setEnPassantTarget(ChessCaseEnumeration.E3.getPosition()); // en passant target is e3

            Position pawnPos = ChessCaseEnumeration.D4.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.BLACK).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            // Can move forward or capture en passant
            assertEquals(2, pawnMoves.size());
            Move enPassantMove = pawnMoves.stream()
                    .filter(Move::isEnPassant)
                    .findFirst()
                    .orElse(null);

            assertNotNull(enPassantMove, "En passant move should be available");
            assertEquals(ChessCaseEnumeration.E3.getPosition(), enPassantMove.getTo());
        }

        @Test
        void enPassant_removeCapturedPawn() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.D5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));
            board.setEnPassantTarget(ChessCaseEnumeration.D6.getPosition());

            Move enPassantMove = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(Move::isEnPassant)
                    .findFirst()
                    .orElseThrow();

            board.executeMove(enPassantMove);

            // White pawn should be on d6
            assertNotNull(board.getPieceAt(ChessCaseEnumeration.D6.getPosition()));
            assertEquals(ChessPieceType.PAWN, board.getPieceAt(ChessCaseEnumeration.D6.getPosition()).getType());
            assertEquals(ChessColor.WHITE, board.getPieceAt(ChessCaseEnumeration.D6.getPosition()).getColor());

            // Black pawn on d5 should be removed
            assertNull(board.getPieceAt(ChessCaseEnumeration.D5.getPosition()));

            // Original white pawn square should be empty
            assertNull(board.getPieceAt(ChessCaseEnumeration.E5.getPosition()));
        }

        @Test
        void enPassant_targetResetAfterMove() {
            board.setupInitialPosition();

            // e2-e4 sets en passant target
            Move e2e4 = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(ChessCaseEnumeration.E2.getPosition())
                            && m.getTo().equals(ChessCaseEnumeration.E4.getPosition()))
                    .findFirst()
                    .orElseThrow();
            board.executeMove(e2e4);

            assertEquals(ChessCaseEnumeration.E3.getPosition(), board.getEnPassantTarget(),
                    "En passant target should be set after double push");

            // Any move by black should reset the en passant target
            Move a7a6 = board.getAllLegalMoves(ChessColor.BLACK).stream()
                    .filter(m -> m.getFrom().equals(ChessCaseEnumeration.A7.getPosition())
                            && m.getTo().equals(ChessCaseEnumeration.A6.getPosition()))
                    .findFirst()
                    .orElseThrow();
            board.executeMove(a7a6);

            assertNull(board.getEnPassantTarget(), "En passant target should be reset after any move");
        }
    }

    // -------------------------------------------------------------------------
    // Pawn promotion
    // -------------------------------------------------------------------------

    @Nested
    class Promotion {

        @Test
        void whitePawn_onSeventhRank_canPromote() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E7.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));

            Position pawnPos = ChessCaseEnumeration.E7.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            // Should have 4 promotion options (Queen, Rook, Bishop, Knight)
            assertEquals(4, pawnMoves.size());
            assertTrue(pawnMoves.stream().allMatch(Move::isPromotion));
            assertTrue(pawnMoves.stream().anyMatch(m -> m.getPromotionType() == ChessPieceType.QUEEN));
            assertTrue(pawnMoves.stream().anyMatch(m -> m.getPromotionType() == ChessPieceType.ROOK));
            assertTrue(pawnMoves.stream().anyMatch(m -> m.getPromotionType() == ChessPieceType.BISHOP));
            assertTrue(pawnMoves.stream().anyMatch(m -> m.getPromotionType() == ChessPieceType.KNIGHT));
        }

        @Test
        void blackPawn_onSecondRank_canPromote() {
            board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E2.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

            Position pawnPos = ChessCaseEnumeration.E2.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.BLACK).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            // Should have 4 promotion options
            assertEquals(4, pawnMoves.size());
            assertTrue(pawnMoves.stream().allMatch(Move::isPromotion));
        }

        @Test
        void promotion_withCapture_generates4Options() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E7.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.D8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            Position pawnPos = ChessCaseEnumeration.E7.getPosition();
            List<Move> pawnMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(pawnPos))
                    .toList();

            // Should have 4 promotion options for e8, and 4 for capturing d8
            assertEquals(8, pawnMoves.size());

            List<Move> capturePromotions = pawnMoves.stream()
                    .filter(m -> m.isCapture() && m.getTo().equals(ChessCaseEnumeration.D8.getPosition()))
                    .toList();
            assertEquals(4, capturePromotions.size());
        }

        @Test
        void promotion_executeMove_createCorrectPiece() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E7.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));

            Move queenPromotion = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.isPromotion() && m.getPromotionType() == ChessPieceType.QUEEN)
                    .findFirst()
                    .orElseThrow();

            board.executeMove(queenPromotion);

            ChessPiece promotedPiece = board.getPieceAt(ChessCaseEnumeration.E8.getPosition());
            assertNotNull(promotedPiece);
            assertEquals(ChessPieceType.QUEEN, promotedPiece.getType());
            assertEquals(ChessColor.WHITE, promotedPiece.getColor());
            assertNull(board.getPieceAt(ChessCaseEnumeration.E7.getPosition()));
        }
    }
}

