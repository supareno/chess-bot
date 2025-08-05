package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import org.junit.jupiter.api.Test;

import static com.fcuillandre.chessbot.board.ChessCaseEnumeration.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KingMoveTest {

    @Test
    void test_king_can_move_one_square_in_any_direction() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4"); // Move a pawn to open
        chessGame.makeMove("e7", "e5"); // Move a pawn to open
        assertTrue(chessGame.isValidMove(new Move(E1,E2))); // King moves from E1 to E2
    }

    @Test
    void test_king_can_castle_short() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4"); // Move a pawn to open
        chessGame.makeMove("e7", "e5"); // Move a pawn to open
        chessGame.makeMove("g1", "f3"); // Knight move to prepare for castling
        chessGame.makeMove("b8", "c6"); // Knight move to prepare for castling
        chessGame.makeMove("f1", "c4"); // Bishop move to prepare for castling
        chessGame.makeMove("f8", "c5"); // Bishop move to prepare for castling
        assertTrue(chessGame.isValidMove(new Move(E1, G1))); // King castles short from E1 to G1

        chessGame.makeMove("e1", "g1");

        // verify the rook and king positions after castling
        assertEquals(ChessPieceType.KING, chessGame.getBoard().getPieceAt(G1).getType());
        assertEquals(ChessPieceType.ROOK, chessGame.getBoard().getPieceAt(F1).getType());
    }
}
