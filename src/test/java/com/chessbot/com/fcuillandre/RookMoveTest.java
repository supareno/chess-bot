package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;

import static com.fcuillandre.chessbot.board.ChessCaseEnumeration.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RookMoveTest {

    @Test
    void test_that_move_A1A3_is_valid_for_white_when_pawns_have_moved() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("a2", "a4");
        chessGame.makeMove("e7", "e6");
        chessGame.makeMove("a4", "a5");
        chessGame.makeMove("e6", "e5");
        // a1 a3 est un coup valide
        assertTrue(chessGame.isValidMove(new Move(A1, A3)));
    }

    @Test
    void test_that_move_A1A3_is_not_valid_for_white_when_pawns_have_not_moved() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();

        assertFalse(chessGame.isValidMove(new Move(A1, A3)));
    }

    @Test
    void test_that_move_A1H1_is_not_valid_for_white_when_pawns_have_not_moved() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();

        assertFalse(chessGame.isValidMove(new Move(A1, H1)));
    }
}
