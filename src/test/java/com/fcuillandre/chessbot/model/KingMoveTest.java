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
 * Tests for king movement rules including:
 * <ul>
 *   <li>Normal king moves (one square in any direction)</li>
 *   <li>Kingside castling (O-O)</li>
 *   <li>Queenside castling (O-O-O)</li>
 *   <li>Blocked castling scenarios</li>
 *   <li>Castling through check prevention</li>
 * </ul>
 *
 * @author fcuillandre
 * @since 1.0
 */
class KingMoveTest {

    private ChessBoard board;

    @BeforeEach
    void setUp() {
        board = new ChessBoard();
    }

    // -------------------------------------------------------------------------
    // Normal king movement
    // -------------------------------------------------------------------------

    @Nested
    class NormalMovement {

        @Test
        void king_canMoveOneSquareInAnyDirection() {
            // King in the center of the board
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> kingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos))
                    .toList();

            // King should be able to move to 8 squares
            assertEquals(8, kingMoves.size());
        }

        @Test
        void king_cannotMoveIntoCheck() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.H2.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> kingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos))
                    .toList();

            // f2 is attacked by the rook on h2
            assertFalse(kingMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.F2.getPosition())),
                    "King should not be able to move to f2 (attacked by rook)");
        }

        @Test
        void king_canCaptureUndefendedPiece() {
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.D5.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E4.getPosition();
            List<Move> captureMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCapture())
                    .toList();

            assertEquals(1, captureMoves.size());
            assertEquals(ChessCaseEnumeration.D5.getPosition(), captureMoves.get(0).getTo());
        }
    }

    // -------------------------------------------------------------------------
    // Kingside castling (O-O)
    // -------------------------------------------------------------------------

    @Nested
    class KingsideCastling {

        @Test
        void white_canCastleKingside_whenPathClear() {
            // White king e1, white rook h1 - both unmoved
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.H1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(1, castlingMoves.size());
            assertEquals(ChessCaseEnumeration.G1.getPosition(), castlingMoves.get(0).getTo());
        }

        @Test
        void white_castleKingside_movesRookCorrectly() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.H1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Move castleKingside = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(Move::isCastling)
                    .findFirst()
                    .orElseThrow();

            board.executeMove(castleKingside);

            // King should be on g1
            ChessPiece king = board.getPieceAt(ChessCaseEnumeration.G1.getPosition());
            assertNotNull(king);
            assertEquals(ChessPieceType.KING, king.getType());
            assertTrue(king.isHasMoved());

            // Rook should be on f1
            ChessPiece rook = board.getPieceAt(ChessCaseEnumeration.F1.getPosition());
            assertNotNull(rook);
            assertEquals(ChessPieceType.ROOK, rook.getType());
            assertTrue(rook.isHasMoved());

            // Original squares should be empty
            assertNull(board.getPieceAt(ChessCaseEnumeration.E1.getPosition()));
            assertNull(board.getPieceAt(ChessCaseEnumeration.H1.getPosition()));
        }

        @Test
        void black_canCastleKingside() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.H8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E8.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.BLACK).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(1, castlingMoves.size());
            assertEquals(ChessCaseEnumeration.G8.getPosition(), castlingMoves.get(0).getTo());
        }
    }

    // -------------------------------------------------------------------------
    // Queenside castling (O-O-O)
    // -------------------------------------------------------------------------

    @Nested
    class QueensideCastling {

        @Test
        void white_canCastleQueenside_whenPathClear() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(1, castlingMoves.size());
            assertEquals(ChessCaseEnumeration.C1.getPosition(), castlingMoves.get(0).getTo());
        }

        @Test
        void white_castleQueenside_movesRookCorrectly() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Move castleQueenside = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(Move::isCastling)
                    .findFirst()
                    .orElseThrow();

            board.executeMove(castleQueenside);

            // King should be on c1
            ChessPiece king = board.getPieceAt(ChessCaseEnumeration.C1.getPosition());
            assertNotNull(king);
            assertEquals(ChessPieceType.KING, king.getType());

            // Rook should be on d1
            ChessPiece rook = board.getPieceAt(ChessCaseEnumeration.D1.getPosition());
            assertNotNull(rook);
            assertEquals(ChessPieceType.ROOK, rook.getType());

            // Original squares should be empty
            assertNull(board.getPieceAt(ChessCaseEnumeration.E1.getPosition()));
            assertNull(board.getPieceAt(ChessCaseEnumeration.A1.getPosition()));
        }

        @Test
        void black_canCastleQueenside() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E8.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.BLACK).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(1, castlingMoves.size());
            assertEquals(ChessCaseEnumeration.C8.getPosition(), castlingMoves.get(0).getTo());
        }
    }

    // -------------------------------------------------------------------------
    // Blocked castling
    // -------------------------------------------------------------------------

    @Nested
    class BlockedCastling {

        @Test
        void cannotCastle_whenKingHasMoved() {
            ChessPiece king = new ChessPiece(ChessPieceType.KING, ChessColor.WHITE);
            king.setHasMoved(true);
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), king);
            board.setPieceAt(ChessCaseEnumeration.H1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(0, castlingMoves.size(), "Cannot castle when king has moved");
        }

        @Test
        void cannotCastle_whenRookHasMoved() {
            ChessPiece rook = new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE);
            rook.setHasMoved(true);
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.H1.getPosition(), rook);
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(0, castlingMoves.size(), "Cannot castle when rook has moved");
        }

        @Test
        void cannotCastleKingside_whenPieceBlockingPath() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.H1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.F1.getPosition(), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(0, castlingMoves.size(), "Cannot castle when path is blocked");
        }

        @Test
        void cannotCastleQueenside_whenPieceBlockingPath() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.B1.getPosition(), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(0, castlingMoves.size(), "Cannot castle when path is blocked");
        }
    }

    // -------------------------------------------------------------------------
    // Castling through check
    // -------------------------------------------------------------------------

    @Nested
    class CastlingThroughCheck {

        @Test
        void cannotCastle_whenKingInCheck() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.H1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.E5.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            assertTrue(board.isKingInCheck(ChessColor.WHITE), "King should be in check");

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(0, castlingMoves.size(), "Cannot castle when king is in check");
        }

        @Test
        void cannotCastleKingside_whenKingPassesThroughCheck() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.H1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            // Rook on f8 attacks f1 (where king would pass)
            board.setPieceAt(ChessCaseEnumeration.F8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(0, castlingMoves.size(), "Cannot castle through check (f1 is attacked)");
        }

        @Test
        void cannotCastleKingside_whenKingLandsInCheck() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.H1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            // Rook on g8 attacks g1 (where king would land)
            board.setPieceAt(ChessCaseEnumeration.G8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(0, castlingMoves.size(), "Cannot castle into check (g1 is attacked)");
        }

        @Test
        void cannotCastleQueenside_whenKingPassesThroughCheck() {
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            // Rook on d8 attacks d1 (where king would pass)
            board.setPieceAt(ChessCaseEnumeration.D8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(0, castlingMoves.size(), "Cannot castle through check (d1 is attacked)");
        }

        @Test
        void canCastleQueenside_whenRookPassesThroughAttack() {
            // The rook can pass through an attacked square (b1), only the king's path matters
            board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            // Rook on b8 attacks b1 (where rook would pass, but not king)
            board.setPieceAt(ChessCaseEnumeration.B8.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));

            Position kingPos = ChessCaseEnumeration.E1.getPosition();
            List<Move> castlingMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                    .filter(m -> m.getFrom().equals(kingPos) && m.isCastling())
                    .toList();

            assertEquals(1, castlingMoves.size(), "Can castle queenside even when b1 is attacked (rook's path)");
        }
    }
}

