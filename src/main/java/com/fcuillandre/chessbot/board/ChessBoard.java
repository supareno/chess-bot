package com.fcuillandre.chessbot.board;

import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.utils.ChessUtils;
import lombok.Getter;

@Getter
public class ChessBoard {

    private final ChessPiece[][] board;
    private final String[][] cases;

    public ChessBoard() {
        this.board = ChessUtils.initializeBoard();
        this.cases = ChessUtils.initializeCases();
    }

    public ChessPiece getPieceAt(ChessCaseEnumeration chessCase) {
        return getPieceAt(chessCase.getCoordinate().getX(), chessCase.getCoordinate().getY());
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


    public void printBoard() {
        ChessUtils.printBoard(board);
    }

    public void move(String from, String to) {
        ChessCaseEnumeration caseEnumFrom = ChessCaseEnumeration.valueOf(from.toUpperCase());
        ChessCaseEnumeration caseEnumTo = ChessCaseEnumeration.valueOf(to.toUpperCase());

        this.move(new Move(caseEnumFrom.getCoordinate(), caseEnumTo.getCoordinate()));
    }

    public void move(Move move) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
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
