package com.fcuillandre.chessbot.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
