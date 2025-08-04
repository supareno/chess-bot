package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.board.ChessCaseEnumeration;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueenMoveTest {
    @Test
    void test_queen_can_move_vertically() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("d2", "d4"); // d2 d4 pour les blancs
        chessGame.makeMove("g7", "g6"); // g7 g6 pour les noirs (coup neutre)
        assertTrue(chessGame.isValidMove(new Move(ChessCaseEnumeration.D1.getCoordinate(), ChessCaseEnumeration.D3.getCoordinate()))); // d1 to d3
    }

    @Test
    void test_queen_can_move_horizontally() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();

        chessGame.makeMove(1,6, 3,6); // b2 b3 pour les blancs
        chessGame.makeMove(6, 6, 5,6); // b7 b6 pour les noirs

        chessGame.makeMove(0,5, 1,6); // d4 d5 pour les blancs
        chessGame.makeMove(5,6, 4,6); // f7 f6 pour les noirs

        chessGame.makeMove(0,6, 2,7); // b2 b3 pour les blancs
        chessGame.makeMove(6, 7, 5,7); // b7 b6 pour les noirs

        assertTrue(chessGame.isValidMove(0,4, 0,6)); // d1 to b1
    }

    @Test
    void test_queen_can_move_diagonally() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,3, 3,3); // e2 e4 pour les blancs
        chessGame.makeMove(6, 6, 5,6); // g7 g6 pour les noirs
        assertTrue(chessGame.isValidMove(0,4, 2,2)); // d1 to f3
    }

    @Test
    void test_queen_cannot_jump_over_pieces() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        // d1 to d5 impossible car d2 bloque
        assertFalse(chessGame.isValidMove(0,4, 4,4));
    }

    @Test
    void test_queen_cannot_move_like_knight() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        assertFalse(chessGame.isValidMove(0,4, 2,5)); // d1 to e3 (L)
    }

    @Test
    void test_queen_can_capture_enemy_piece() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove(1,3, 3,3); // e2 e4 pour les blancs
        chessGame.makeMove(6,0, 4,0); // h7 h5 pour les noirs
        assertTrue(chessGame.isValidMove(0,4, 4,0)); // d1 capture h5
    }

    @Test
    void test_queen_cannot_capture_own_piece() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        // d1 to d2 impossible car d2 est un pion blanc
        assertFalse(chessGame.isValidMove(0,4, 1,4));
    }
}
