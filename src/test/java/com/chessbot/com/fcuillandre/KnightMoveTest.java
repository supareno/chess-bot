package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.game.ChessGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KnightMoveTest {

    @Test
    void test_that_move_G1F3_is_a_valid_move_for_a_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,6, 3, 6); // b2 b4 pour les blancs
        chessGame.makeMove(6, 3, 5,3); // e7 e6 pour les noirs
        assertTrue(chessGame.isValidMove(0, 1, 2, 2)); // g1 to f3
    }

    @Test
    void test_that_move_G1H3_is_a_valid_move_for_a_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,6, 3, 6); // b2 b4 pour les blancs
        chessGame.makeMove(6, 3, 5,3); // e7 e6 pour les noirs
        assertTrue(chessGame.isValidMove(0, 1, 2, 0)); // g1 to f3
    }

    @Test
    void test_that_move_G1F2_is_not_a_valid_move_for_a_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        assertFalse(chessGame.isValidMove(0, 1, 1, 2)); // g1 to f3
    }

    @Test
    void test_that_move_G1B4_is_not_a_valid_move_for_a_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        assertFalse(chessGame.isValidMove(0,1, 3, 6)); // g1 to b4
    }
}
