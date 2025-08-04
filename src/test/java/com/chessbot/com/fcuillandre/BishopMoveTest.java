package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.game.ChessGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BishopMoveTest {

    @Test
    void test_that_move_C1B2_is_a_valid_first_move_for_a_bishop() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,6, 3, 6); // b2 b4 pour les blancs
        chessGame.makeMove(6, 3, 5,3); // e7 e6 pour les noirs
        assertTrue(chessGame.isValidMove(0, 5, 1, 6)); // c1 to b2
    }

    @Test
    void test_that_move_F1G2_is_not_a_valid_first_move_for_a_bishop_if_the_path_is_not_clear() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,6, 3, 6); // b2 b3 pour les blancs
        chessGame.makeMove(6, 3, 5,3); // e7 e6 pour les noirs
        assertFalse(chessGame.isValidMove(0, 2, 2, 1)); // f1 to g2
    }

    @Test
    void test_that_move_F1B1_is_not_a_valid_first_move_for_a_bishop() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,6, 3, 6); // b2 b3 pour les blancs
        chessGame.makeMove(6, 3, 5,3); // e7 e6 pour les noirs
        assertFalse(chessGame.isValidMove(0, 2, 0, 6)); // f1 to b1
    }
}
