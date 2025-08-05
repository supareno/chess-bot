package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;

import static com.fcuillandre.chessbot.board.ChessCaseEnumeration.*;
import static org.junit.jupiter.api.Assertions.*;

class PawnMoveTest {


    @Test
    void test_that_move_E6E5_is_not_a_valid_first_move_for_black() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        assertFalse(chessGame.isValidMove(new Move(E6, E5))); // e6 vers e5
    }

    @Test
    void test_that_move_E7E4_is_not_a_valid_first_move_for_black() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        assertFalse(chessGame.isValidMove(new Move(E7, E4))); // e7 vers e4
    }

    @Test
    void test_that_move_E7E5_is_a_valid_first_move_for_black() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        assertTrue(chessGame.isValidMove(new Move(E7, E5)));
    }

    @Test
    void test_that_move_E7E6_is_a_valid_first_move_for_black() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        assertTrue(chessGame.isValidMove(new Move(E7, E6)));
    }


    @Test
    void test_that_move_E4E5_for_white_is_a_valid_move() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        chessGame.makeMove("e7", "e6");
        assertTrue(chessGame.isValidMove(new Move(E4, E5)));
    }

    @Test
    void test_that_move_D2D4_for_white_is_a_valid_fifth_move() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        // 1
        chessGame.makeMove("e2", "e4");
        chessGame.makeMove("e7", "e5");

        // 2
        chessGame.makeMove("g1", "f3");
        chessGame.makeMove("b8", "c6");

        // 3
        chessGame.makeMove("d2", "d4");
        assertFalse(chessGame.isWhiteTurn());
    }

    @Test
    void test_that_a_pawn_can_capture_an_opponent_piece() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        chessGame.makeMove("d7", "d5");
        assertTrue(chessGame.isValidMove(new Move(E4, D5))); // Black pawn can capture white pawn on d5
    }
}