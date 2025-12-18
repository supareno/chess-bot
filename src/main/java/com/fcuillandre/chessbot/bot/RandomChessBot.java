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
package com.fcuillandre.chessbot.bot;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Provides a chess bot that selects a random legal move for Black.
 * <p>
 * This bot iterates over all Black pieces on the board, generates all possible legal moves
 * (including pawn promotion to queen when reaching the last rank), and selects one at random.
 * <br>
 * <b>Usage example:</b>
 * <pre>
 *     ChessBot bot = new RandomChessBot();
 *     Move move = bot.getMove(game);
 *     if (move != null) {
 *         game.makeMove(move);
 *     }
 * </pre>
 *
 * @author fcuillandre
 * @version 1.0
 */
public final class RandomChessBot implements ChessBot {

    @Override
    public Move getMove(ChessGame game) {
        return getRandomLegalMove(game);
    }

    /**
     * Returns a random legal move for Black.
     * <p>
     * Iterates over all Black pieces, generates all legal moves (including pawn promotion),
     * and selects one at random.
     * </p>
     *
     * @param game the current chess game
     * @return a random legal move for Black, or null if none available
     */
    public Move getRandomLegalMove(ChessGame game) {
        ChessBoard board = game.getBoard();
        List<Move> legalMoves = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getColor() == ChessColor.BLACK) {
                    // Try all possible destinations
                    for (int dx = 0; dx < 8; dx++) {
                        for (int dy = 0; dy < 8; dy++) {
                            if (x == dx && y == dy) continue;
                            Move move;
                            // Promote black pawn to queen when reaching rank 8 (x == 0)
                            if (piece.getType() == ChessPieceType.PAWN && dx == 0) {
                                move = new Move(new Coordinate(x, y), new Coordinate(dx, dy), ChessPieceType.QUEEN);
                            } else {
                                move = new Move(new Coordinate(x, y), new Coordinate(dx, dy));
                            }
                            if (game.isValidMove(move)) {
                                legalMoves.add(move);
                            }
                        }
                    }
                }
            }
        }
        if (legalMoves.isEmpty()) return null;
        return legalMoves.get(new Random().nextInt(legalMoves.size()));
    }
}