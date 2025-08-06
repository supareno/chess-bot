package com.fcuillandre.chessbot.utils;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.pieces.ChessPiece;

import static com.fcuillandre.chessbot.pieces.ChessPieces.*;

public class ChessUtils {

    public static void log(String arg) {
        System.out.println(arg);
    }

    public static ChessPiece[][] initializeBoard() {
        ChessPiece[][] board = new ChessPiece[8][8];
        // Initialize pieces for white
        board[0][0] = WHITE_ROOK; // Rook
        board[0][1] = WHITE_KNIGHT; // Knight
        board[0][2] = WHITE_BISHOP; // Bishop
        board[0][3] = WHITE_QUEEN; // Queen
        board[0][4] = WHITE_KING; // King
        board[0][5] = WHITE_BISHOP; // Bishop
        board[0][6] = WHITE_KNIGHT; // Knight
        board[0][7] = WHITE_ROOK; // Rook
        for (int i = 0; i < 8; i++) {
            board[1][i] = WHITE_PAWN; // Pawns
        }
        // Initialize pieces for black
        board[7][0] = BLACK_ROOK; // Rook
        board[7][1] = BLACK_KNIGHT; // Knight
        board[7][2] = BLACK_BISHOP; // Bishop
        board[7][3] = BLACK_QUEEN; // Queen
        board[7][4] = BLACK_KING; // King
        board[7][5] = BLACK_BISHOP; // Bishop
        board[7][6] = BLACK_KNIGHT; // Knight
        board[7][7] = BLACK_ROOK; // Rook
        for (int i = 0; i < 8; i++) {
            board[6][i] = BLACK_PAWN; // Pawns
        }
        return board;
    }

    public static void printBoard(ChessPiece[][] board) {

        for (int i = 7; i >= 0; i--) {
            System.out.print((i + 1) + " | ");
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                System.out.print((piece != null ? piece : ".") + " ");
            }
            System.out.println();
        }
        System.out.println("   -----------------");
        System.out.println("    A B C D E F G H");
        System.out.println("---- ---- ---- ---- ---- ---- ---- ----");
    }

    public static String[][] initializeCases() {
        // initialize the chessboard cases with coordinates with format "A1", "B2", etc.
        String[][] cases = new String[8][8];
        char file = 'H';
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cases[i][j] = "" + file + (i + 1);
                file--;
            }
            file = 'H'; // Reset file for the next row
        }
        return cases;
    }

    public static String getCaseName(int x, int y) {
        // Convert x and y to chess notation (A1, B2, etc.)
        char file = (char) ('A' + x);
        int rank = 8 - y; // Reverse the rank for chess notation
        return "" + file + rank;
    }

    // Additional utility methods can be added here
}