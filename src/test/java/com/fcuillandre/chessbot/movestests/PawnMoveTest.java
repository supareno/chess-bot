package com.fcuillandre.chessbot.movestests;

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

    // Test for en passant capture
    @Test
    void test_that_en_passant_capture_for_white_is_valid() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        chessGame.makeMove("a7", "a5");
        chessGame.makeMove("e4", "e5"); // White pawn moves two squares
        chessGame.makeMove("d7","d5"); // Black pawn moves two squares
        assertTrue(chessGame.isValidMove(new Move(E5, D6))); // White pawn captures en passant
    }


    @Test
    void test_that_en_passant_capture_for_white_is_invalid_if_not_done_immediatly_after_black_move() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        chessGame.makeMove("a7", "a5");
        chessGame.makeMove("e4", "e5");
        chessGame.makeMove("d7","d5");
        // white does not capture en passant immediately
        chessGame.makeMove("f1", "c4"); // White bishop moves
        // black plays a move
        chessGame.makeMove("c7", "c6");
        // Now try to capture en passant
        assertFalse(chessGame.isValidMove(new Move(E5, D6))); // White pawn cannot capture en passant after other moves
    }

    // Test for en passant capture for black
    @Test
    void test_that_en_passant_capture_for_black_is_valid() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("a2", "a3");
        chessGame.makeMove("e7", "e5");
        chessGame.makeMove("h2", "h3");
        chessGame.makeMove("e5", "e4");
        chessGame.makeMove("d2", "d4"); // White pawn moves two squares
        assertTrue(chessGame.isValidMove(new Move(E4, D3))); // Black pawn captures en passant
    }

    // Test for en passant capture for black
    @Test
    void test_that_en_passant_capture_for_black_is_invalid_if_not_done_immediatly_after_white_move() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("a2", "a3");
        chessGame.makeMove("e7", "e5");
        chessGame.makeMove("h2", "h3");
        chessGame.makeMove("e5", "e4");
        chessGame.makeMove("d2", "d4");
        // Black does not capture en passant immediately
        chessGame.makeMove("f8", "c5"); // Black bishop moves
        // White plays a move
        chessGame.makeMove("a3", "a4");
        assertFalse(chessGame.isValidMove(new Move(E4, D3))); // Black pawn captures en passant
    }

    @Test
    void test_that_en_passant_is_not_possible_if_no_pawn_moved_two_squares() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        chessGame.makeMove("d7", "d5");
        chessGame.makeMove("a2", "a3");
        chessGame.makeMove("d5", "d4"); // Black pawn moves one square
        assertFalse(chessGame.isValidMove(new Move(E4, D5))); // Black pawn cannot capture en passant
    }
}