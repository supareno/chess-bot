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
package com.fcuillandre.chessbot.engine;

import com.fcuillandre.chessbot.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests covering draw conditions in {@link GameEngine}:
 * <ul>
 *   <li>Insufficient material (K-K, K+minor-K, KB-KB same-color bishops)</li>
 *   <li>Threefold repetition</li>
 *   <li>50-move rule</li>
 *   <li>Stalemate</li>
 * </ul>
 *
 * @author fcuillandre
 * @since 1.0
 */
class DrawDetectionTest {

    private GameEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GameEngine();
    }


    // -------------------------------------------------------------------------
    // Insufficient material
    // -------------------------------------------------------------------------

    @Nested
    class InsufficientMaterial {

        @Test
        void kingVsKing_isDrawAfterLastCapture() {
            // Set up a position where White captures the last Black non-king piece
            // K on e1, k on e8, white rook on a8 (to capture black's last piece)
            // We use GameEngine.newGame() + manual board manipulation is not possible via
            // public API, so we drive this through the engine via a real game sequence.
            //
            // Simpler: create a new game, reset the board to K vs k via the board's
            // public API, then call makeMove to trigger updateGameState indirectly.
            // Since GameEngine.newGame() resets the board, we rely on the board's
            // direct piece placement instead.

            engine.newGame();
            ChessBoard board = engine.getBoard();

            // Clear all pieces
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    board.setPieceAt(new Position(r, c), null);
                }
            }

            // Place only kings
            board.setPieceAt(new Position(7, 4), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(new Position(0, 4), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            // Place a black knight that White will capture to trigger the draw check
            board.setPieceAt(new Position(6, 5), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.BLACK));
            // White king on e1 can capture the knight on f2
            board.getPieceAt(new Position(7, 4)).setHasMoved(true);

            boolean moved = engine.makeMove(new Position(7, 4), new Position(6, 5), null);
            assertTrue(moved, "King should be able to capture the knight");
            assertEquals(GameState.DRAW_INSUFFICIENT_MATERIAL, engine.getGameState(),
                    "K vs K should be insufficient material");
        }

        @Test
        void kingAndBishopVsKing_isDrawAfterLastCapture() {
            engine.newGame();
            ChessBoard board = engine.getBoard();

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    board.setPieceAt(new Position(r, c), null);
                }
            }

            // White: King e1, Bishop c1
            board.setPieceAt(new Position(7, 4), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(new Position(7, 2), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE));
            // Black: King e8, Knight that will be captured
            board.setPieceAt(new Position(0, 4), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(new Position(6, 3), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.BLACK));

            // White bishop on c1 captures black knight on d2
            boolean moved = engine.makeMove(new Position(7, 2), new Position(6, 3), null);
            assertTrue(moved, "Bishop should capture the knight");
            assertEquals(GameState.DRAW_INSUFFICIENT_MATERIAL, engine.getGameState(),
                    "K+B vs K should be insufficient material");
        }

        @Test
        void kingAndKnightVsKing_isDrawAfterLastCapture() {
            engine.newGame();
            ChessBoard board = engine.getBoard();

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    board.setPieceAt(new Position(r, c), null);
                }
            }

            // White: King e1, Knight b1. Black: King e8, Bishop that will be captured.
            board.setPieceAt(new Position(7, 4), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(new Position(7, 1), new ChessPiece(ChessPieceType.KNIGHT, ChessColor.WHITE));
            board.setPieceAt(new Position(0, 4), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            // Place a black piece that the white knight can capture: a3 = (5,0)
            board.setPieceAt(new Position(5, 2), new ChessPiece(ChessPieceType.BISHOP, ChessColor.BLACK));

            // Knight b1 (7,1) -> c3 (5,2) captures black bishop
            boolean moved = engine.makeMove(new Position(7, 1), new Position(5, 2), null);
            assertTrue(moved, "Knight should capture the bishop");
            assertEquals(GameState.DRAW_INSUFFICIENT_MATERIAL, engine.getGameState(),
                    "K+N vs K should be insufficient material");
        }

        @Test
        void kingAndBishopVsKingAndBishop_sameLightSquares_isDraw() {
            engine.newGame();
            ChessBoard board = engine.getBoard();

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    board.setPieceAt(new Position(r, c), null);
                }
            }

            // White: King e1, Bishop c1 (light square: (7+2)%2=1 — dark)
            // Black: King e8, Bishop f8 (light square: (0+5)%2=1 — dark)
            // Both bishops are on dark squares → draw
            board.setPieceAt(new Position(7, 4), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(new Position(7, 2), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE)); // c1: dark
            board.setPieceAt(new Position(0, 4), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(new Position(0, 5), new ChessPiece(ChessPieceType.BISHOP, ChessColor.BLACK)); // f8: dark

            // White needs to make a move to trigger updateGameState.
            // Move white king e1->d1 (7,4)->(7,3)
            board.getPieceAt(new Position(7, 4)).setHasMoved(true);
            boolean moved = engine.makeMove(new Position(7, 4), new Position(7, 3), null);
            assertTrue(moved, "King should be able to move");
            assertEquals(GameState.DRAW_INSUFFICIENT_MATERIAL, engine.getGameState(),
                    "KB vs KB (same-color bishops) should be insufficient material");
        }

        @Test
        void kingAndBishopVsKingAndBishop_differentSquareColors_isNotDraw() {
            engine.newGame();
            ChessBoard board = engine.getBoard();

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    board.setPieceAt(new Position(r, c), null);
                }
            }

            // White bishop on c1 (dark: (7+2)%2=1), Black bishop on e8 … actually let's use
            // White bishop on c1 (row=7, col=2): (7+2)%2=1 → dark
            // Black bishop on d8 (row=0, col=3): (0+3)%2=1 → dark  ← same, let's pick a light one
            // Black bishop on c8 (row=0, col=2): (0+2)%2=0 → light ← opposite
            board.setPieceAt(new Position(7, 4), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.setPieceAt(new Position(7, 2), new ChessPiece(ChessPieceType.BISHOP, ChessColor.WHITE)); // c1: dark
            board.setPieceAt(new Position(0, 4), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(new Position(0, 2), new ChessPiece(ChessPieceType.BISHOP, ChessColor.BLACK)); // c8: light

            board.getPieceAt(new Position(7, 4)).setHasMoved(true);
            boolean moved = engine.makeMove(new Position(7, 4), new Position(7, 3), null);
            assertTrue(moved, "King should be able to move");
            assertNotEquals(GameState.DRAW_INSUFFICIENT_MATERIAL, engine.getGameState(),
                    "KB vs KB with bishops on different square colors should NOT be a draw");
        }
    }

    // -------------------------------------------------------------------------
    // Threefold repetition
    // -------------------------------------------------------------------------

    @Nested
    class ThreefoldRepetition {

        /**
         * Drives a position to occur three times by shuttling both knights back and forth.
         * After the 3rd occurrence the game state must be DRAW_REPETITION.
         */
        @Test
        void threeRepetitions_triggersDrawRepetition() {
            engine.newGame();

            // Initial position counts as 1 occurrence for White.
            // We shuttle the knights to return to the starting position twice more.
            // Each full round-trip (White knight out+back, Black knight out+back) = 1 extra occurrence.

            // Round-trip 1
            assertTrue(engine.makeMove(ChessCaseEnumeration.G1, ChessCaseEnumeration.F3)); // White Nf3
            assertTrue(engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.F6)); // Black Nf6
            assertTrue(engine.makeMove(ChessCaseEnumeration.F3, ChessCaseEnumeration.G1)); // White Ng1
            assertTrue(engine.makeMove(ChessCaseEnumeration.F6, ChessCaseEnumeration.G8)); // Black Ng8
            // Position repeated: 2nd occurrence for White

            // Round-trip 2
            assertTrue(engine.makeMove(ChessCaseEnumeration.G1, ChessCaseEnumeration.F3)); // White Nf3
            assertTrue(engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.F6)); // Black Nf6
            assertTrue(engine.makeMove(ChessCaseEnumeration.F3, ChessCaseEnumeration.G1)); // White Ng1
            assertTrue(engine.makeMove(ChessCaseEnumeration.F6, ChessCaseEnumeration.G8)); // Black Ng8
            // Position repeated: 3rd occurrence for White

            // Round-trip 2
            assertTrue(engine.makeMove(ChessCaseEnumeration.G1, ChessCaseEnumeration.F3)); // White Nf3
            assertFalse(engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.F6)); // Black Nf6
            // Position repeated: 3rd occurrence for White

            // White makes any move; position at White's turn is now the start again (3rd time).
            // Actually after Black Ng8 it is White's turn with the initial position for the 3rd time,
            // so the draw should be triggered on that last Black move.
            assertEquals(GameState.DRAW_REPETITION, engine.getGameState(),
                    "Position reached 3 times — should be DRAW_REPETITION");
        }

        @Test
        void twoRepetitions_doesNotTriggerDraw() {
            engine.newGame();

            // Only one round-trip: 2nd occurrence, not yet a draw
            assertTrue(engine.makeMove(ChessCaseEnumeration.G1, ChessCaseEnumeration.F3));
            assertTrue(engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.F6));
            assertTrue(engine.makeMove(ChessCaseEnumeration.F3, ChessCaseEnumeration.G1));
            assertTrue(engine.makeMove(ChessCaseEnumeration.F6, ChessCaseEnumeration.G8));

            assertNotEquals(GameState.DRAW_REPETITION, engine.getGameState(),
                    "Position repeated only twice — should NOT be DRAW_REPETITION yet");
            assertEquals(GameState.PLAYING, engine.getGameState());
        }

        @Test
        void newGame_resetsRepetitionHistory() {
            engine.newGame();

            // Two round-trips (almost draw)
            engine.makeMove(ChessCaseEnumeration.G1, ChessCaseEnumeration.F3);
            engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.F6);
            engine.makeMove(ChessCaseEnumeration.F3, ChessCaseEnumeration.G1);
            engine.makeMove(ChessCaseEnumeration.F6, ChessCaseEnumeration.G8);
            engine.makeMove(ChessCaseEnumeration.G1, ChessCaseEnumeration.F3);
            engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.F6);
            engine.makeMove(ChessCaseEnumeration.F3, ChessCaseEnumeration.G1);
            // Would be 3rd occurrence on the next Black move, but we reset first.

            engine.newGame();

            // One round-trip after the reset — should still be PLAYING
            engine.makeMove(ChessCaseEnumeration.G1, ChessCaseEnumeration.F3);
            engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.F6);
            engine.makeMove(ChessCaseEnumeration.F3, ChessCaseEnumeration.G1);
            engine.makeMove(ChessCaseEnumeration.F6, ChessCaseEnumeration.G8);

            assertEquals(GameState.PLAYING, engine.getGameState(),
                    "After newGame(), repetition history should be reset");
        }
    }

    // -------------------------------------------------------------------------
    // 50-move rule
    // -------------------------------------------------------------------------

    @Nested
    class FiftyMoveRule {

        @Test
        void pawnMove_resetsFiftyMoveCounter() {
            engine.newGame();

            // Make some moves without pawn
            assertTrue(engine.makeMove(ChessCaseEnumeration.G1, ChessCaseEnumeration.F3));
            assertTrue(engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.F6));
            assertTrue(engine.makeMove(ChessCaseEnumeration.F3, ChessCaseEnumeration.G1));
            assertTrue(engine.makeMove(ChessCaseEnumeration.F6, ChessCaseEnumeration.G8));

            // Make a pawn move (resets counter)
            assertTrue(engine.makeMove(ChessCaseEnumeration.E2, ChessCaseEnumeration.E4));

            // Game should still be in progress
            assertNotEquals(GameState.DRAW_FIFTY_MOVES, engine.getGameState());
        }

        @Test
        void capture_resetsFiftyMoveCounter() {
            engine.newGame();
            ChessBoard board = engine.getBoard();

            // Set up position where capture will happen
            board.setPieceAt(ChessCaseEnumeration.E4.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK));

            // Make some non-capture moves first
            assertTrue(engine.makeMove(ChessCaseEnumeration.G1, ChessCaseEnumeration.F3));
            assertTrue(engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.H6));
            assertTrue(engine.makeMove(ChessCaseEnumeration.F3, ChessCaseEnumeration.G1));
            assertTrue(engine.makeMove(ChessCaseEnumeration.H6, ChessCaseEnumeration.G8));

            // Capture (resets counter)
            assertTrue(engine.makeMove(ChessCaseEnumeration.D2, ChessCaseEnumeration.D3));
            assertTrue(engine.makeMove(ChessCaseEnumeration.E4, ChessCaseEnumeration.D3)); // Black pawn captures

            assertNotEquals(GameState.DRAW_FIFTY_MOVES, engine.getGameState());
        }
    }

    // -------------------------------------------------------------------------
    // Stalemate
    // -------------------------------------------------------------------------

    @Nested
    class Stalemate {

        @Test
        void stalemate_blackKingNoLegalMoves_notInCheck() {
            engine.newGame();
            ChessBoard board = engine.getBoard();

            // Clear board
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    board.setPieceAt(new Position(r, c), null);
                }
            }

            // Classic stalemate position: White King e6, White Queen d6, Black King e8
            // After White plays, Black has no legal moves but is not in check
            board.setPieceAt(ChessCaseEnumeration.E6.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.getPieceAt(ChessCaseEnumeration.E6.getPosition()).setHasMoved(true);
            board.setPieceAt(ChessCaseEnumeration.D4.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.WHITE));
            board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));

            // White queen moves to d8 - stalemating black
            assertTrue(engine.makeMove(ChessCaseEnumeration.D4.getPosition(), ChessCaseEnumeration.D6.getPosition(), null));

            assertEquals(GameState.STALEMATE, engine.getGameState(),
                    "Black has no legal moves and is not in check - should be stalemate");
        }

        @Test
        void stalemate_kingInCorner() {
            engine.newGame();
            ChessBoard board = engine.getBoard();

            // Clear board
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    board.setPieceAt(new Position(r, c), null);
                }
            }

            // Black king trapped in corner: Black King a8, White King c7, White Queen b6
            board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
            board.setPieceAt(ChessCaseEnumeration.C7.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
            board.getPieceAt(ChessCaseEnumeration.C7.getPosition()).setHasMoved(true);
            board.setPieceAt(ChessCaseEnumeration.B5.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.WHITE));

            // Queen moves to b6 - stalemate
            assertTrue(engine.makeMove(ChessCaseEnumeration.B5.getPosition(), ChessCaseEnumeration.B6.getPosition(), null));

            assertEquals(GameState.STALEMATE, engine.getGameState(),
                    "Black king in corner with no legal moves and not in check - should be stalemate");
        }

    }
}

