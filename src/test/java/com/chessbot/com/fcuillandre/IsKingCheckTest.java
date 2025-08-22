package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.board.ChessCaseEnumeration;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IsKingCheckTest {

    @Test
    void assert_true_that_king_is_in_check() {
        ChessGame game = new ChessGame();
        game.startGame();
        game.makeMove("e2", "e4");
        game.makeMove("d7", "d5");
        game.makeMove("f1", "b5");
        assertTrue(game.isKingInCheck());
    }

    @Test
    void assert_false_that_king_is_not_in_check() {
        ChessGame game = new ChessGame();
        game.startGame();
        game.makeMove("e2", "e4");
        game.makeMove("d7", "d5");
        game.makeMove("f1", "c4");
        assertFalse(game.isKingInCheck());
    }

    @Test
    void assert_that_moving_a_piece_to_not_block_check_when_king_is_in_check_is_not_a_valid_move() {
        ChessGame game = new ChessGame();
        game.startGame();
        game.makeMove("e2", "e4");
        game.makeMove("d7", "d5");
        game.makeMove("f1", "b5");
        assertFalse(game.isValidMove(new Move(ChessCaseEnumeration.A7, ChessCaseEnumeration.A6)));
    }


    @Test
    void assert_that_moving_a_piece_to_block_check_is_a_valid_move() {
        ChessGame game = new ChessGame();
        game.startGame();
        game.makeMove("e2", "e4");
        game.makeMove("d7", "d5");
        game.makeMove("f1", "b5");
        game.makeMove("c8", "d7");
        assertFalse(game.isKingInCheck());
    }
}
