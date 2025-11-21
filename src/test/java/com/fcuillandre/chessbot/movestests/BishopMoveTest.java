package com.fcuillandre.chessbot.movestests;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;

import static com.fcuillandre.chessbot.board.ChessCaseEnumeration.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BishopMoveTest {

    @Test
    void test_that_move_C1B2_is_a_valid_first_move_for_a_bishop() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("b2","b4");
        chessGame.makeMove("e7","e6");
        assertTrue(chessGame.isValidMove(new Move(C1, B2)));
    }

    @Test
    void test_that_move_F1G2_is_not_a_valid_first_move_for_a_bishop_if_the_path_is_not_clear() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("b2","b3");
        chessGame.makeMove("e7", "e6");
        assertFalse(chessGame.isValidMove(new Move(F1,G2))); // f1 to g2
    }

    @Test
    void test_that_move_F1B1_is_not_a_valid_first_move_for_a_bishop() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("b2","b3");
        chessGame.makeMove("e7", "e6");
        assertFalse(chessGame.isValidMove(new Move(F1,B1)));
    }
}
