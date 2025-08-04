package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.game.ChessGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PawnMoveTest {

    // Vérifie que e6-e5 n'est pas un coup valide au début de la partie pour les noirs
    // il faut avant faire le coup e2-e4 pour les blancs
    @Test
    void test_that_move_E6E5_is_not_a_valid_first_move_for_black() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,3, 3, 3); // e2 e4 pour les blancs
        // e6-e5 n'est pas un coup valide au début
        assertFalse(chessGame.isValidMove(5, 3, 4, 3)); // e6 vers e5
    }

    @Test
    void test_that_move_E7E4_is_not_a_valid_first_move_for_black() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,3, 3, 3); // e2 e4 pour les blancs
        // e6-e5 n'est pas un coup valide au début
        assertFalse(chessGame.isValidMove(6, 3, 3, 3)); // e7 vers e4
    }

    @Test
    void test_that_move_E7E5_is_a_valid_first_move_for_black() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,3, 3, 3); // e2 e4 pour les blancs
        // e7-e5 est un coup valide au début
        assertTrue(chessGame.isValidMove(6, 3, 4, 3)); // e7 vers e5
    }

    @Test
    void test_that_move_E7E6_is_a_valid_first_move_for_black() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,3, 3, 3); // e2 e4 pour les blancs
        // e7-e5 est un coup valide au début
        assertTrue(chessGame.isValidMove(6, 3, 5, 3)); // e7 vers e6
    }


    @Test
    void test_that_move_E4E5_for_white_is_a_valid_first_move_for_black() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,3, 3, 3); // e2 e4 pour les blancs
        chessGame.makeMove(6,3, 5, 3); // e7 e5 pour les noirs
        // e4-e5 est un coup valide au début
        assertTrue(chessGame.isValidMove(3, 3, 4, 3)); // e4 vers e5
    }

    /*
    e2 e4
    e7 e5
    g1 f3
    b8 c6
    d2 d4
     */
    @Test
    void test_that_move_D2D4_for_white_is_a_valid_fifth_move() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        // 1
        chessGame.makeMove("e2", "e4"); // e2 e4 pour les blancs
        chessGame.makeMove("e7", "e5"); // e7 e5 pour les noirs

        // 2
        chessGame.makeMove("g1", "f3"); // g1 f3 pour les blancs
        chessGame.makeMove("b8", "c6"); // b8 c6 pour les noirs

        // 3
        chessGame.makeMove("d2", "d4"); // d2 d4 pour les blancs
        assertFalse(chessGame.isWhiteTurn()); // e4 vers e5
    }
}