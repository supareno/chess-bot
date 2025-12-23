/*
 * Copyright 2025-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fcuillandre.chessbot.movestests;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import org.junit.jupiter.api.Test;

import static com.fcuillandre.chessbot.board.ChessCaseEnumeration.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fcuillandre
 * @version 0.1
 */
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

    // add a test to verify that a king cannot move into check
    @Test
    void test_king_cannot_move_into_check() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4");
        chessGame.makeMove("f7", "f5");
        //
        chessGame.makeMove("d1", "f3");
        chessGame.makeMove("g1", "h3");
        //
        chessGame.makeMove("f1", "c4");
        chessGame.makeMove("e7", "e6");
        //
        chessGame.makeMove("g1", "h3");
        chessGame.makeMove("f5", "e4");
        //
        chessGame.makeMove("f3", "f4");
        chessGame.makeMove("f1", "c5");
        //
        chessGame.makeMove("b1", "c3");
        // Now the king at e1 is in check from the queen at h5
        assertFalse(chessGame.isValidMove(new Move(E8, G8))); // King cannot move to E2 as it is in check
    }

    // add a test to verify that a king cannot castle through check
    @Test
    void test_king_cannot_castle_through_check() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4"); // Move a pawn to open
        chessGame.makeMove("e7", "e5"); // Move a pawn to open
        chessGame.makeMove("g1", "f3"); // Knight move to prepare for castling
        chessGame.makeMove("b8", "c6"); // Knight move to prepare for castling
        chessGame.makeMove("f1", "c4"); // Bishop move to prepare for castling
        chessGame.makeMove("f8", "c5"); // Bishop move to prepare for castling
        chessGame.makeMove("d2","d3");
        chessGame.makeMove("f7","f6");
        chessGame.makeMove("d1", "h5"); // Queen move to threaten the king
        // Now the king at e1 cannot castle short as it would pass through check at f1
        assertFalse(chessGame.isValidMove(new Move(E8, G8)));
    }

    // add a test to verify that a king cannot castle when in check
    @Test
    void test_king_cannot_castle_when_in_check() {
        ChessGame chessGame = new ChessGame();
        chessGame.startGame();
        chessGame.makeMove("e2", "e4"); // Move a pawn to open
        chessGame.makeMove("e7", "e5"); // Move a pawn to open
        chessGame.makeMove("g1", "f3"); // Knight move to prepare for castling
        chessGame.makeMove("d8", "h4"); // Queen move to threaten the king
        chessGame.makeMove("f1", "c4"); // Bishop move to prepare for castling
        chessGame.makeMove("h4", "e4"); // Queen checks the king on e1
        // Now the king at e1 is in check and cannot castle
        assertFalse(chessGame.isValidMove(new Move(E1, G1)));
    }
}
