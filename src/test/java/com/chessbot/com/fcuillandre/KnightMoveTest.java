package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;

import static com.fcuillandre.chessbot.board.ChessCaseEnumeration.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KnightMoveTest {

    @Test
    void test_that_move_G1F3_is_a_valid_move_for_a_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("b2", "b4");
        chessGame.makeMove("e7", "e6");
        assertTrue(chessGame.isValidMove(new Move(G1, F3)));
    }

    @Test
    void test_that_move_G1H3_is_a_valid_move_for_a_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("b2", "b4");
        chessGame.makeMove("e7", "e6");
        assertTrue(chessGame.isValidMove(new Move(G1, H3)));
    }

    @Test
    void test_that_move_G1F2_is_not_a_valid_move_for_a_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        assertFalse(chessGame.isValidMove(new Move(G1, F2)));
    }

    @Test
    void test_that_move_G1B4_is_not_a_valid_move_for_a_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        assertFalse(chessGame.isValidMove(new Move(G1, B4)));
    }
}
