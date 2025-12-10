package com.fcuillandre.chessbot.bot;

import com.fcuillandre.chessbot.bot.minimax.MinimaxPlusChessBot;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinimaxPlusChessBotTest {

    @Test
    void test_first_move_after_E4() {
        ChessGame game = new ChessGame();
        // White plays e2-e4
        game.makeMove("E2", "E4");
        game.makeMove("E7", "E5"); // Black plays e7-e5 to respond
        game.makeMove("G1", "F3"); // White plays Nf3
        MinimaxPlusChessBot bot = new MinimaxPlusChessBot();
        Move botMove = bot.getMove(game);
        assertNotNull(botMove, "Bot should return a move after Nf3");
        // Print for debug
        System.out.println("Bot move after e4: " + botMove);
        // Optionally, check that the move is a valid black move
        assertTrue(game.getBoard().getPieceAt(botMove.getStart().getX(), botMove.getStart().getY()) != null,
                "Move should start from a non-empty square");
    }
}

