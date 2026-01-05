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

import com.fcuillandre.chessbot.bot.minimax.MinimaxPlusChessBot;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fcuillandre
 * @version 0.1
 */
class MinimaxPlusChessBotOpeningTest {

    @Test
    void test_simple_book_e4() {
        ChessGame game = new ChessGame();
        MinimaxPlusChessBot bot = new MinimaxPlusChessBot();

        // 1. e4
        game.makeMove("E2", "E4");
        // 1... bot
        Move botMove1 = bot.getMove(game);
        assertNotNull(botMove1);
        // Acceptable black replies: e5, c5, e6, c6, d6, d5
        Set<String> book1 = Set.of("E7E5", "C7C5", "E7E6", "C7C6", "D7D6", "D7D5");
        String move1 = toSimpleMove(botMove1);
        assertTrue(book1.contains(move1), "Bot's first move (" + move1 + ") should be a book move");
        game.makeMove(botMove1);

    }

    @Test
    void test_simple_book_d4() {
        ChessGame game = new ChessGame();
        MinimaxPlusChessBot bot = new MinimaxPlusChessBot();

        // 1. e4
        game.makeMove("D2", "D4");
        // 1... bot
        Move botMove1 = bot.getMove(game);
        assertNotNull(botMove1);
        // Acceptable black replies: e5, c5, e6, c6, d6, d5
        Set<String> book1 = Set.of("D7D5", "G8F6", "E7E6", "C7C5");
        String move1 = toSimpleMove(botMove1);
        assertTrue(book1.contains(move1), "Bot's first move (" + move1 + ") should be a book move");
        game.makeMove(botMove1);

    }

    private String toSimpleMove(Move move) {
        // Returns a string like E7E5, B8C6, etc.
        char fromFile = (char) ('A' + move.getStart().getY());
        char fromRank = (char) ('1' + move.getStart().getX());
        char toFile = (char) ('A' + move.getEnd().getY());
        char toRank = (char) ('1' + move.getEnd().getX());
        return "" + fromFile + fromRank + toFile + toRank;
    }
}
