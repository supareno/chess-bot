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
package com.fcuillandre.chessbot.movestests;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;

import static com.fcuillandre.chessbot.board.ChessCaseEnumeration.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fcuillandre
 * @version 0.1
 */
class QueenMoveTest {

    @Test
    void test_queen_can_move_vertically_D1D3() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("d2", "d4");
        chessGame.makeMove("g7", "g6");
        assertTrue(chessGame.isValidMove(new Move(D1, D3)));
    }

    @Test
    void test_queen_can_move_horizontally() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();

        chessGame.makeMove("b2", "b3");
        chessGame.makeMove("b7", "b6");

        chessGame.makeMove("b1", "c3");
        chessGame.makeMove("f7", "f6");

        chessGame.makeMove("c1", "b2");
        chessGame.makeMove("c7", "c6");

        assertTrue(chessGame.isValidMove(new Move(D1, B1))); // d1 to b1
    }

    @Test
    void test_queen_can_move_diagonally() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        chessGame.makeMove("g7", "g6");
        assertTrue(chessGame.isValidMove(new Move(D1, F3)));
    }

    @Test
    void test_queen_cannot_jump_over_pieces() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        // d1 to d5 impossible car d2 bloque
        assertFalse(chessGame.isValidMove(new Move(D1, D5)));
    }

    @Test
    void test_queen_cannot_move_like_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        assertFalse(chessGame.isValidMove(new Move(D1, D3))); // d1 to e3 (L)
    }

    @Test
    void test_queen_can_capture_enemy_piece() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4"); // e2 e4 pour les blancs
        chessGame.makeMove("h7", "h5"); // h7 h5 pour les noirs
        assertTrue(chessGame.isValidMove(new Move(D1, H5))); // d1 capture h5
    }

    @Test
    void test_queen_cannot_capture_own_piece() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        // d1 to d2 impossible car d2 est un pion blanc
        assertFalse(chessGame.isValidMove(new Move(D1, D2)));
    }
}
