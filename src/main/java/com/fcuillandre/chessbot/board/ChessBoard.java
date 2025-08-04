package com.fcuillandre.chessbot.board;

import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.utils.ChessUtils;

public class ChessBoard {

    private final ChessPiece[][] board;
    private final String[][] cases;

    public ChessBoard() {
        this.board = ChessUtils.initializeBoard();
        this.cases = ChessUtils.initializeCases();
    }

    public ChessPiece getPieceAt(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        return board[x][y];
    }

    public String getCaseAt(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        return cases[x][y];
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    public void printBoard() {
        ChessUtils.printBoard(board);
    }

    public void move(int startX, int startY, int endX, int endY) {
        if (startX < 0 || startX >= 8 || startY < 0 || startY >= 8 ||
            endX < 0 || endX >= 8 || endY < 0 || endY >= 8) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds: (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")");
        }
        ChessPiece piece = board[startX][startY];
        if (piece == null) {
            throw new IllegalArgumentException("No piece at starting position: (" + startX + ", " + startY + ")");
        }
        board[endX][endY] = piece;
        board[startX][startY] = null;
    }

    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        ChessUtils.initializeBoard();
    }
}
