package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.game.ChessGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RookMoveTest {

    @Test
    void test_that_move_A1A3_is_valid_for_white_when_pawns_have_moved() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,7, 3, 7); // a2 a4 pour les blancs
        chessGame.makeMove(6, 3, 5,3); // e7 e6 pour les noirs
        chessGame.makeMove(3,7, 4, 7); // a4 a5 pour les blancs
        chessGame.makeMove(5, 3, 4, 3); // e6 e5 pour les noirs
        // a1 a3 est un coup valide
        assertTrue(chessGame.isValidMove(0, 7, 2, 7)); // a1 a3
    }

    @Test
    void test_that_move_A1A3_is_not_valid_for_white_when_pawns_have_not_moved() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();

        assertFalse(chessGame.isValidMove(0, 7, 2, 7)); // a1 a3
    }

    @Test
    void test_that_move_A1H1_is_not_valid_for_white_when_pawns_have_not_moved() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();

        assertFalse(chessGame.isValidMove(0, 7, 7, 0)); // a1 a3
    }
}
