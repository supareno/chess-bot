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
 * Tests for check detection and pin handling:
 * <ul>
 *   <li>Simple check detection by different piece types</li>
 *   <li>Discovered check scenarios</li>
 *   <li>Pin detection (pieces cannot move if they are pinning the king)</li>
 *   <li>Double check scenarios</li>
 * </ul>
 *
 * @author fcuillandre
 * @since 1.0
 */
class IsKingCheckTest {

    private ChessBoard board;

    @BeforeEach
    void setUp() {
        board = new ChessBoard();
    }

    // -------------------------------------------------------------------------
    // Basic check detection
    // -------------------------------------------------------------------------

    @Nested
    class BasicCheckDetection {

        @Test
        void noCheck_inInitialPosition() {
            board.setupInitialPosition();

            assertFalse(board.isKingInCheck(ChessColor.WHITE));
            assertFalse(board.isKingInCheck(ChessColor.BLACK));
        }

        @Test
        void checkByRook() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E5.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            assertTrue(board.isKingInCheck(ChessColor.WHITE), "White king should be in check by rook on e5");
            assertFalse(board.isKingInCheck(ChessColor.BLACK));
        }

        @Test
        void checkByBishop() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.A5.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.BLACK));

            assertTrue(board.isKingInCheck(ChessColor.WHITE), "White king should be in check by bishop on a5");
        }

        @Test
        void checkByQueen() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.H4.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.BLACK));

            assertTrue(board.isKingInCheck(ChessColor.WHITE), "White king should be in check by queen on h4 (diagonal)");
        }

        @Test
        void checkByKnight() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.D3.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.BLACK));

            assertTrue(board.isKingInCheck(ChessColor.WHITE), "White king should be in check by knight on d3");
        }

        @Test
        void checkByPawn() {
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.D5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

            assertTrue(board.isKingInCheck(ChessColor.WHITE), "White king should be in check by pawn on d5");
        }

        @Test
        void blockedCheck_notInCheck() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E5.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E3.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));

            assertFalse(board.isKingInCheck(ChessColor.WHITE), "Check should be blocked by pawn on e3");
        }
    }

    // -------------------------------------------------------------------------
    // Discovered check
    // -------------------------------------------------------------------------

    @Nested
    class DiscoveredCheck {

        @Test
        void discoveredCheck_byMovingBlockingPiece() {
            // White king on e1, white bishop on e3 (blocking), black rook on e8
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E3.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            // Before moving the bishop, no check
            assertFalse(board.isKingInCheck(ChessColor.WHITE));

            // The bishop can move, but not all its legal moves are valid (some would expose the king)
            Position bishopPos = ChessCaseEnumeration.E3.getPosition();
            List<Move> bishopMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(bishopPos))
                    .toList();

            // Bishop on e3 is pinned - it cannot move anywhere because it would expose the king
            assertEquals(0, bishopMoves.size(), "Bishop should have no legal moves because it is pinned");
        }

        @Test
        void discoveredCheck_afterMoveExecution() {
            // Set up a position where white can give discovered check
            // White king on h1, white rook on e1, white knight on e4 (blocking), black king on e8
            board.setPieceAt(ChessCaseEnumeration.H1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            // Move knight away from e-file (e.g., to f6)
            Move knightMove = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(ChessCaseEnumeration.E4.getPosition())
                            && m.getTo().equals(ChessCaseEnumeration.F6.getPosition()))
                    .findFirst()
                    .orElseThrow();

            board.executeMove(knightMove);

            // Black king should now be in check (discovered check by rook)
            assertTrue(board.isKingInCheck(ChessColor.BLACK), "Black king should be in discovered check");
        }
    }

    // -------------------------------------------------------------------------
    // Pin detection
    // -------------------------------------------------------------------------

    @Nested
    class PinDetection {

        @Test
        void pinnedPiece_cannotMoveAwayFromPinLine() {
            // White king on e1, white knight on e4, black rook on e8 - knight is pinned
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            Position knightPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> knightMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(knightPos))
                    .toList();

            // Knight cannot move at all - it would expose the king
            assertEquals(0, knightMoves.size(), "Pinned knight should have no legal moves");
        }

        @Test
        void pinnedRook_canMoveAlongPinLine() {
            // White king on e1, white rook on e4, black queen on e8 - rook is pinned but can move along e-file
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.BLACK));

            Position rookPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> rookMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(rookPos))
                    .toList();

            // Rook can move along the e-file (e2, e3, e5, e6, e7, e8 - capturing queen)
            assertTrue(rookMoves.size() > 0, "Pinned rook should be able to move along pin line");
            assertTrue(rookMoves.stream().allMatch(m -> m.getTo().col() == 4),
                    "All rook moves should stay on the e-file");
        }

        @Test
        void pinnedBishop_canMoveAlongDiagonalPinLine() {
            // White king on e1, white bishop on c3, black bishop on a5 - bishop is pinned but can move on diagonal
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.H8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.C3.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A5.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.BLACK));

            Position bishopPos = ChessCaseEnumeration.C3.getPosition();
            List<Move> bishopMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(bishopPos))
                    .toList();

            // Bishop can move along the a5-e1 diagonal (d2, b4, a5 - capturing)
            assertTrue(bishopMoves.size() > 0, "Pinned bishop should be able to move along pin diagonal");

            // Verify all moves are on the same diagonal
            for (Move move : bishopMoves) {
                int rowDiff = move.getTo().row() - ChessCaseEnumeration.E1.getPosition().row();
                int colDiff = move.getTo().col() - ChessCaseEnumeration.E1.getPosition().col();
                assertEquals(Math.abs(rowDiff), Math.abs(colDiff),
                        "Bishop should only move on the pin diagonal");
            }
        }

        @Test
        void diagonalPin_rookCannotMove() {
            // White king on e1, white rook on d2, black bishop on b4 - rook is diagonally pinned
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.H8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.D2.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.B4.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.BLACK));

            Position rookPos = ChessCaseEnumeration.D2.getPosition();
            List<Move> rookMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(rookPos))
                    .toList();

            // Rook is pinned diagonally and cannot move (rooks can't move diagonally)
            assertEquals(0, rookMoves.size(), "Diagonally pinned rook should have no legal moves");
        }
    }

    // -------------------------------------------------------------------------
    // Double check
    // -------------------------------------------------------------------------

    @Nested
    class DoubleCheck {

        @Test
        void doubleCheck_onlyKingCanMove() {
            // Set up double check: king is attacked by two pieces
            // White king on e1, black rook on e8, black knight on d3 (both attacking e1... wait, knight on d3 attacks e1)
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.D3.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.BLACK));
            // Add a white piece that could block one check
            board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));

            assertTrue(board.isKingInCheck(ChessColor.WHITE), "White king should be in check");

            List<Move> legalMoves = board.getAllLegalMoves(ChessColor.WHITE);

            // In double check, only the king can move (can't block or capture both attackers)
            assertTrue(legalMoves.stream().allMatch(m -> m.getPiece().getType() == ChessPieceType.KING),
                    "In double check, only king moves should be legal");
        }

        @Test
        void checkmate_noLegalMoves() {
            // Back rank mate: White king on g1, black queen on d1, black rook on d2
            board.setPieceAt(ChessCaseEnumeration.G1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.D1.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.D2.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            assertTrue(board.isKingInCheck(ChessColor.WHITE), "White king should be in check");

            List<Move> legalMoves = board.getAllLegalMoves(ChessColor.WHITE);
            assertEquals(0, legalMoves.size(), "White should have no legal moves (checkmate)");
        }
    }

    // -------------------------------------------------------------------------
    // Edge cases
    // -------------------------------------------------------------------------

    @Nested
    class EdgeCases {

        @Test
        void kingCannotMoveNextToEnemyKing() {
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E6.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Position whiteKingPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> kingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(whiteKingPos))
                    .toList();

            // King cannot move to e5 (adjacent to enemy king)
            assertFalse(kingMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.E5.getPosition())),
                    "King cannot move adjacent to enemy king");
        }

        @Test
        void checkDetection_throughMultiplePieces() {
            // Make sure check is properly blocked when there are multiple pieces in the way
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E5.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E3.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));

            assertFalse(board.isKingInCheck(ChessColor.WHITE), "Check should be blocked by multiple pawns");
        }
    }
}

