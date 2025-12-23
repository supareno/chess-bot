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
package com.fcuillandre.chessbot.ui;

import com.fcuillandre.chessbot.board.ChessCaseEnumeration;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.MovedPiece;
import com.fcuillandre.chessbot.pieces.ChessPieces;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author fcuillandre
 * @version 0.1
 */
class ChessGameFrameTest {

    @Test
    void verify_that_the_moveList_contains_all_30_moves_made_in_the_game() {
        ChessGame game = new ChessGame();
        // fill the game with 60 moves
        for (int i = 0; i < 60; i++) {
            game.getMoveHistory().add(new MovedPiece(ChessPieces.WHITE_PAWN,
                    new Coordinate(ChessCaseEnumeration.E2),
                    new Coordinate(ChessCaseEnumeration.E4),
                    false, false, false,
                    false, false,
                    false, null));
        }
        ChessGameFrame frame = new ChessGameFrame(game);

        assertEquals(30, frame.getMoveListModel().getSize());
        assertTrue(frame.getMoveList().getSize().getHeight() < 600);
    }

}
