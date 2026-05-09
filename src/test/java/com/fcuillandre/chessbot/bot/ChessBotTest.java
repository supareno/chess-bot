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
package com.fcuillandre.chessbot.bot;

import com.fcuillandre.chessbot.engine.GameEngine;
import com.fcuillandre.chessbot.engine.GameState;
import com.fcuillandre.chessbot.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ChessBot} correctness and root-level alpha-beta pruning behaviour.
 *
 * <p>The root-alpha fix ensures that when a better move is found at the root level,
 * the alpha bound is tightened immediately so subsequent sibling calls can be pruned.
 * The observable effect is that the bot must still find the correct best move — a free
 * capture, a mate-in-1, and avoidance of moving into check are the clearest validators.</p>
 *
 * @author fcuillandre
 * @since 1.0
 */
class ChessBotTest {

    private GameEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GameEngine();
    }

    /**
     * Bot should always return null when there are no legal moves (checkmate / stalemate).
     */
    @Test
    void findBestMove_noLegalMoves_returnsNull() {
        engine.newGame();
        ChessBoard board = engine.getBoard();

        // Clear the board and create a stalemate-like position for Black:
        // White King e6, White Queen d6, Black King e8 → Black is stalemated.
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPieceAt(new Position(r, c), null);
            }
        }
        board.setPieceAt(new Position(2, 4), new ChessPiece(ChessPieceType.KING,  ChessColor.WHITE)); // e6
        board.setPieceAt(new Position(2, 3), new ChessPiece(ChessPieceType.QUEEN, ChessColor.WHITE)); // d6
        board.setPieceAt(new Position(0, 4), new ChessPiece(ChessPieceType.KING,  ChessColor.BLACK)); // e8

        // Black has no legal moves — bot for Black should return null.
        ChessBot blackBot = new ChessBot(ChessColor.BLACK, 2);
        Move best = blackBot.findBestMove(engine);

        assertNull(best, "Bot must return null when there are no legal moves");
    }

    /**
     * Bot must capture a free queen when the opportunity is available.
     * Validates that root-level alpha is updated correctly so the obvious best
     * move (large material gain) is selected.
     */
    @Test
    void findBestMove_freeCaptureAvailable_capturesTaken() {
        engine.newGame();
        ChessBoard board = engine.getBoard();

        // Clear the board: White King e1, Black King e8.
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPieceAt(new Position(r, c), null);
            }
        }

        // White: King e1, Rook a1. Black: King e8, undefended White queen on a8 waiting to be taken.
        // Actually we want the bot (White) to capture a free Black queen.
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING,  ChessColor.WHITE)); // e1
        board.setPieceAt(ChessCaseEnumeration.A1.getPosition(), new ChessPiece(ChessPieceType.ROOK,  ChessColor.WHITE)); // a1
        board.getPieceAt(ChessCaseEnumeration.A1.getPosition()).setHasMoved(true);

        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING,  ChessColor.BLACK)); // e8
        board.setPieceAt(ChessCaseEnumeration.A8.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.BLACK)); // a8 — free queen

        ChessBot whiteBot = new ChessBot(ChessColor.WHITE, 2);
        Move best = whiteBot.findBestMove(engine);

        System.out.println(best);
        assertNotNull(best, "Bot must find a move");
        assertTrue(best.isCapture(), "Bot must capture the free queen");
        assertEquals(ChessPieceType.QUEEN, best.getCapturedPiece().getType(),
                "Bot must capture the queen, not any other piece");
    }

    /**
     * Bot must find a mate-in-1.
     * This also verifies that alpha from the first evaluated child (a winning move)
     * is propagated correctly so the engine does not accidentally prefer a non-mating move.
     */
    @Test
    void findBestMove_mateInOne_findsMatingMove() {
        engine.newGame();
        ChessBoard board = engine.getBoard();

        // Clear the board and set up a classic back-rank mate-in-1:
        // White: King g1 (7,6), Rook a1 (7,0), Rook h1->(7,7) delivering mate on e8.
        // Black: King e8 (0,4) boxed in by its own pawns on d7/e7/f7.
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPieceAt(new Position(r, c), null);
            }
        }

        board.setPieceAt(new Position(7, 6), new ChessPiece(ChessPieceType.KING,  ChessColor.WHITE)); // g1
        board.setPieceAt(new Position(7, 0), new ChessPiece(ChessPieceType.ROOK,  ChessColor.WHITE)); // a1
        board.getPieceAt(new Position(7, 0)).setHasMoved(true);
        board.getPieceAt(new Position(7, 6)).setHasMoved(true);

        board.setPieceAt(new Position(0, 4), new ChessPiece(ChessPieceType.KING,  ChessColor.BLACK)); // e8
        // Black pawns block escape
        board.setPieceAt(new Position(1, 3), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK)); // d7
        board.setPieceAt(new Position(1, 4), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK)); // e7
        board.setPieceAt(new Position(1, 5), new ChessPiece(ChessPieceType.PAWN, ChessColor.BLACK)); // f7

        ChessBot whiteBot = new ChessBot(ChessColor.WHITE, 3);
        Move best = whiteBot.findBestMove(engine);

        assertNotNull(best, "Bot must find a move");

        // Execute the move and check it delivers checkmate
        engine.makeMove(best);
        assertEquals(GameState.CHECKMATE, engine.getGameState(),
                "Bot's best move should deliver checkmate (mate-in-1)");
    }

    /**
     * Bot must not move into check.
     * Ensures the legality filter in findBestMove works and that alpha propagation
     * does not accidentally prefer an illegal (self-check) line.
     */
    @Test
    void findBestMove_doesNotMoveIntoCheck() {
        engine.newGame();
        ChessBoard board = engine.getBoard();

        // Set up: White King e1 (7,4). Black King e8 (0,4), Black Rook e5 (3,4) — pins the e-file.
        // Any White move that exposes the king to the rook must be rejected.
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                board.setPieceAt(new Position(r, c), null);
            }
        }

        board.setPieceAt(new Position(7, 4), new ChessPiece(ChessPieceType.KING,  ChessColor.WHITE)); // e1
        board.setPieceAt(new Position(5, 4), new ChessPiece(ChessPieceType.PAWN,  ChessColor.WHITE)); // e3 — pawn blocks rook
        board.setPieceAt(new Position(0, 4), new ChessPiece(ChessPieceType.KING,  ChessColor.BLACK)); // e8
        board.setPieceAt(new Position(3, 4), new ChessPiece(ChessPieceType.ROOK,  ChessColor.BLACK)); // e5

        ChessBot whiteBot = new ChessBot(ChessColor.WHITE, 2);
        Move best = whiteBot.findBestMove(engine);

        assertNotNull(best, "Bot must find a legal move");

        // The move must be legal (king not left in check)
        ChessBoard copy = board.copy();
        copy.executeMove(best);
        assertFalse(copy.isKingInCheck(ChessColor.WHITE),
                "Bot's chosen move must not leave the White king in check");
    }
}

