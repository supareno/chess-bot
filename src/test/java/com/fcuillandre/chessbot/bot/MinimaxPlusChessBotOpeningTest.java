package com.fcuillandre.chessbot.bot;

import com.fcuillandre.chessbot.bot.minimax.MinimaxPlusChessBot;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class MinimaxPlusChessBotOpeningTest {

    /**
     * Test MinimaxPlusChessBot's first 3 moves against a basic opening book.
     * This test uses the Italian Game as a reference (e4 e5 Nf3 Nc6 Bc4 Bc5).
     */
    @Test
    void testFirstThreeMovesAccuracy() {
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

        // 2. Nf3
        game.makeMove("G1", "F3");
        // 2... bot
        Move botMove2 = bot.getMove(game);
        assertNotNull(botMove2);
        // Acceptable: Nc6, d6, Nf6, d5, g6, e6
        Set<String> book2 = Set.of("B8C6", "D7D6", "G8F6", "D7D5", "G7G6", "E7E6");
        String move2 = toSimpleMove(botMove2);
        assertTrue(book2.contains(move2), "Bot's second move (" + move2 + ") should be a book move");
        game.makeMove(botMove2);

        // 3. Bc4
        game.makeMove("F1", "C4");
        // 3... bot
        Move botMove3 = bot.getMove(game);
        assertNotNull(botMove3);
        // Acceptable: Bc5, Nf6, Be7, Nc6, d6
        Set<String> book3 = Set.of("F8C5", "G8F6", "F8E7", "B8C6", "D7D6");
        String move3 = toSimpleMove(botMove3);
        assertTrue(book3.contains(move3), "Bot's third move (" + move3 + ") should be a book move");
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
