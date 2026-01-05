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
package com.fcuillandre.chessbot.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author fcuillandre
 * @version 0.1
 */
class ChessBoardTest {

    private ChessBoard chessBoard;

    @Test
    void verify_that_x0_y0_returns_case_A1() {
        chessBoard = new ChessBoard();
        String chessCase = chessBoard.getCaseAt(0, 0);
        assertEquals(ChessCaseEnumeration.A1.name(), chessCase);
    }

    @Test
    void verify_that_x1_y3_returns_case_D2() {
        chessBoard = new ChessBoard();
        String chessCase = chessBoard.getCaseAt(1, 3);
        assertEquals(ChessCaseEnumeration.D2.name(), chessCase);
    }


    @Test
    void verify_that_x4_y5_returns_case_F5() {
        chessBoard = new ChessBoard();
        String chessCase = chessBoard.getCaseAt(4, 5);
        assertEquals(ChessCaseEnumeration.F5.name(), chessCase);
    }
}
