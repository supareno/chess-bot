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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author fcuillandre
 * @version 0.1
 */
class RookMoveTest {

    @Test
    void test_that_move_A1A3_is_valid_for_white_when_pawns_have_moved() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("a2", "a4");
        chessGame.makeMove("e7", "e6");
        chessGame.makeMove("a4", "a5");
        chessGame.makeMove("e6", "e5");
        // a1 a3 est un coup valide
        assertTrue(chessGame.isValidMove(new Move(A1, A3)));
    }

    @Test
    void test_that_move_A1A3_is_not_valid_for_white_when_pawns_have_not_moved() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();

        assertFalse(chessGame.isValidMove(new Move(A1, A3)));
    }

    @Test
    void test_that_move_A1H1_is_not_valid_for_white_when_pawns_have_not_moved() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();

        assertFalse(chessGame.isValidMove(new Move(A1, H1)));
    }
}
