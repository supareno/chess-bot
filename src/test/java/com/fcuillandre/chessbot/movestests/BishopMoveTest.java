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
class BishopMoveTest {

    @Test
    void test_that_move_C1B2_is_a_valid_first_move_for_a_bishop() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("b2","b4");
        chessGame.makeMove("e7","e6");
        assertTrue(chessGame.isValidMove(new Move(C1, B2)));
    }

    @Test
    void test_that_move_F1G2_is_not_a_valid_first_move_for_a_bishop_if_the_path_is_not_clear() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("b2","b3");
        chessGame.makeMove("e7", "e6");
        assertFalse(chessGame.isValidMove(new Move(F1,G2))); // f1 to g2
    }

    @Test
    void test_that_move_F1B1_is_not_a_valid_first_move_for_a_bishop() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("b2","b3");
        chessGame.makeMove("e7", "e6");
        assertFalse(chessGame.isValidMove(new Move(F1,B1)));
    }
}
