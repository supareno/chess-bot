package com.fcuillandre.chessbot.utils;

import com.fcuillandre.chessbot.pieces.ChessPiece;

import static com.fcuillandre.chessbot.pieces.ChessPieces.*;

/**
 * Utility class for chess-related functions.
 *
 * @author FCuillandre
 * @version 1.0
 */
public class ChessUtils {

    /**
     * Log a message.
     * <p>For the moment, the message is logged in the console</p>
     *
     * @param arg The message to log.
     */
    public static void log(String arg) {
        System.out.println(arg);
    }

    /**
     * Initialize the chess board with pieces in their starting positions.
     *
     * @return A 2D array representing the chess board with pieces.
     */
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


    /**
     * Initialize the chessboard cases with coordinates.
     *
     * @return A 2D array representing the chessboard cases.
     */
    public static String[][] initializeCases() {
        // initialize the chessboard cases with coordinates with format "A1", "B2", etc.
        String[][] cases = new String[8][8];
        char file = 'A';
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cases[i][j] = "" + file + (i + 1);
                file++;
            }
            file = 'A'; // Reset file for the next row
        }
        return cases;
    }
}