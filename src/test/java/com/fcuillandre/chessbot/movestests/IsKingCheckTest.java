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

import com.fcuillandre.chessbot.board.ChessCaseEnumeration;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author fcuillandre
 * @version 0.1
 */
class IsKingCheckTest {

    @Test
    void assert_true_that_king_is_in_check() {
        ChessGame game = new ChessGame();
        game.startGame();
        game.makeMove("e2", "e4");
        game.makeMove("d7", "d5");
        game.makeMove("f1", "b5");
        assertTrue(game.isKingInCheck());
    }

    @Test
    void assert_false_that_king_is_not_in_check() {
        ChessGame game = new ChessGame();
        game.startGame();
        game.makeMove("e2", "e4");
        game.makeMove("d7", "d5");
        game.makeMove("f1", "c4");
        assertFalse(game.isKingInCheck());
    }

    @Test
    void assert_that_moving_a_piece_to_not_block_check_when_king_is_in_check_is_not_a_valid_move() {
        ChessGame game = new ChessGame();
        game.startGame();
        game.makeMove("e2", "e4");
        game.makeMove("d7", "d5");
        game.makeMove("f1", "b5");
        assertFalse(game.isValidMove(new Move(ChessCaseEnumeration.A7, ChessCaseEnumeration.A6)));
    }


    @Test
    void assert_that_moving_a_piece_to_block_check_is_a_valid_move() {
        ChessGame game = new ChessGame();
        game.startGame();
        game.makeMove("e2", "e4");
        game.makeMove("d7", "d5");
        game.makeMove("f1", "b5");
        game.makeMove("c8", "d7");
        assertFalse(game.isKingInCheck());
    }
}
