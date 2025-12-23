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

import com.fcuillandre.chessbot.board.ChessCaseEnumeration;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.MovedPiece;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fcuillandre
 * @version 0.1
 */
class ChessMoveFormatterUtilsTest {

    @Nested
    @DisplayName("Pawn Moves")
    class PawnMoves {
        @Test
        void test_pawn_move_no_capture() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.PAWN),
                    new Coordinate(1, 4), // e2
                    new Coordinate(3, 4), // e4
                    false, false, false, false, false, false, null);
            assertEquals("e4", ChessMoveFormatterUtils.formatMove(move));
        }

        @Test
        void test_pawn_capture() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.PAWN),
                    new Coordinate(4, 4), // e5
                    new Coordinate(5, 3), // d6
                    true, false, false, false, false, false, null);
            assertEquals("exd6", ChessMoveFormatterUtils.formatMove(move));
        }

        @Test
        void test_pawn_promotion() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.PAWN),
                    new Coordinate(6, 4), // e7
                    new Coordinate(7, 4), // e8
                    false, false, false, false, false, false, ChessPieceType.QUEEN);
            assertEquals("e8=Q", ChessMoveFormatterUtils.formatMove(move));
        }

        @Test
        void test_pawn_capture_with_promotion() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.PAWN),
                    new Coordinate(6, 4), // e7
                    new Coordinate(7, 5), // f8
                    true, false, false, false, false, false, ChessPieceType.KNIGHT);
            assertEquals("exf8=N", ChessMoveFormatterUtils.formatMove(move));
        }
    }

    @Nested
    @DisplayName("Piece Moves")
    class PieceMoves {
        @Test
        void test_knight_move() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.KNIGHT),
                    new Coordinate(0, 1), // b1
                    new Coordinate(2, 2), // c3
                    false, false, false, false, false, false, null);
            assertEquals("Nc3", ChessMoveFormatterUtils.formatMove(move));
        }

        @Test
        void test_bishop_capture() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.BISHOP),
                    new Coordinate(2, 0), // a3
                    new Coordinate(4, 2), // c5
                    true, false, false, false, false, false, null);
            assertEquals("Bxc5", ChessMoveFormatterUtils.formatMove(move));
        }
    }

    @Nested
    @DisplayName("Castling")
    class Castling {
        @Test
        void test_kingside_castle() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.KING),
                    new Coordinate(0, 4), // e1
                    new Coordinate(0, 6), // g1
                    false, false, false, true, false, false, null);
            assertEquals("O-O", ChessMoveFormatterUtils.formatMove(move));
        }

        @Test
        void test_queenside_castle() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.KING),
                    new Coordinate(0, 4), // e1
                    new Coordinate(0, 2), // c1
                    false, false, false, false, true, false, null);
            assertEquals("O-O-O", ChessMoveFormatterUtils.formatMove(move));
        }
    }

    @Nested
    @DisplayName("Check and Checkmate")
    class CheckAndMate {
        @Test
        void test_move_with_check() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.QUEEN),
                    new Coordinate(3, 3), // d5
                    new Coordinate(6, 7), // h7
                    false, true, false, false, false, false, null);
            assertEquals("Qh7+", ChessMoveFormatterUtils.formatMove(move));
        }

        @Test
        void test_move_with_checkmate() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.QUEEN),
                    new Coordinate(ChessCaseEnumeration.D5), // d5
                    new Coordinate(ChessCaseEnumeration.H7), // h7
                    false, false, true, false, false, false, null);
            assertEquals("Qh7#", ChessMoveFormatterUtils.formatMove(move));
        }

        @Test
        void test_castle_with_check() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.KING),
                    new Coordinate(0, 4), // e1
                    new Coordinate(0, 6), // g1
                    false, true, false, true, false, false, null);
            assertEquals("O-O+", ChessMoveFormatterUtils.formatMove(move));
        }

        @Test
        void test_castle_with_checkmate() {
            MovedPiece move = new MovedPiece(
                    new ChessPiece(ChessColor.WHITE, ChessPieceType.KING),
                    new Coordinate(0, 4), // e1
                    new Coordinate(0, 6), // g1
                    false, false, true, true, false, false, null);
            assertEquals("O-O#", ChessMoveFormatterUtils.formatMove(move));
        }
    }

    @Test
    void test_null_move_returns_empty_string() {
        assertEquals("", ChessMoveFormatterUtils.formatMove(null));
    }

    @Test
    void test_null_piece_returns_empty_string() {
        MovedPiece move = new MovedPiece(null, new Coordinate(0, 0), new Coordinate(0, 1), false, false, false, false, false, false, null);
        assertEquals("", ChessMoveFormatterUtils.formatMove(move));
    }
}

