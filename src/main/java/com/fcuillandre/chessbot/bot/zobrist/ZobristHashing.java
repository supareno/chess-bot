package com.fcuillandre.chessbot.bot.zobrist;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;

import java.util.Random;

public class ZobristHashing {

    private static final long[][][] zobristKeys = new long[8][8][12];
    private static final Random random = new Random();

    static {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 12; k++) {
                    zobristKeys[i][j][k] = random.nextLong();
                }
            }
        }
    }

    public static long computeHash(ChessBoard board) {
        long hash = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPieceAt(i, j);
                if (piece != null) {
                    hash ^= zobristKeys[i][j][getPieceIndex(piece)];
                }
            }
        }
        return hash;
    }

    private static int getPieceIndex(ChessPiece piece) {
        int index = 0;
        if (piece.getColor() == ChessColor.WHITE) {
            index += 6;
        }
        switch (piece.getType()) {
            case PAWN:
                index += 0;
                break;
            case KNIGHT:
                index += 1;
                break;
            case BISHOP:
                index += 2;
                break;
            case ROOK:
                index += 3;
                break;
            case QUEEN:
                index += 4;
                break;
            case KING:
                index += 5;
                break;
        }
        return index;
    }
}

