package com.chessbot.com.fcuillandre;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
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

        chessGame.makeMove("e1", "g1");

        // verify the rook and king positions after castling
        assertEquals(ChessPieceType.KING, chessGame.getBoard().getPieceAt(G1).getType());
        assertEquals(ChessPieceType.ROOK, chessGame.getBoard().getPieceAt(F1).getType());
    }

    // test king can castle long
    @Test
    void test_king_can_castle_long() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        // 1
        chessGame.makeMove("e2", "e4"); // Move a pawn to open
        chessGame.makeMove("e7", "e5"); // Move a pawn to open

        // 2
        chessGame.makeMove("d1", "h5"); // Queen move to prepare for castling
        chessGame.makeMove("d8", "h4"); // Queen move to prepare for castling

        // 3
        chessGame.makeMove("d2", "d4");
        chessGame.makeMove("d7", "d5"); // Move pawns to open the way for castling
        // 4
        chessGame.makeMove("c1", "f4"); // Bishop move to prepare for castling
        chessGame.makeMove("c8", "f5"); // Bishop move to prepare for castling

        // 5
        chessGame.makeMove("b1", "c3"); // Knight move to prepare for castling
        chessGame.makeMove("g8", "f6"); // Knight move to prepare for castling

        chessGame.makeMove("e1", "c1"); // King castles long

        // verify the rook and king positions after castling
        assertEquals(ChessPieceType.KING, chessGame.getBoard().getPieceAt(C1).getType());
        assertEquals(ChessPieceType.ROOK, chessGame.getBoard().getPieceAt(D1).getType());
    }
}
