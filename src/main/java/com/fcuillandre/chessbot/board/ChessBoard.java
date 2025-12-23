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

import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.utils.ChessUtils;
import lombok.Getter;

/**
 * Represents a chess board containing pieces and their positions.
 * Provides methods to get pieces, move them, and manage the board state.
 *
 * @author fcuillandre
 * @version 0.1
 */
@Getter
public final class ChessBoard {

    private final ChessPiece[][] board;
    private final String[][] cases;

    /**
     * Constructs a ChessBoard instance with an initialized board and cases.
     * The board is set up with the standard chess starting position.
     * The cases are initialized with their corresponding chess notation.
     */
    public ChessBoard() {
        this.board = ChessUtils.initializeBoard();
        this.cases = ChessUtils.initializeCases();
    }

    /**
     * Returns the chess piece at the specified ChessCaseEnumeration.
     *
     * @param chessCase the ChessCaseEnumeration representing the position on the board
     * @return the ChessPiece at the specified position, or null if no piece is present
     */
    public ChessPiece getPieceAt(ChessCaseEnumeration chessCase) {
        return getPieceAt(chessCase.getCoordinate().getX(), chessCase.getCoordinate().getY());
    }

    /**
     * Returns the chess piece at the specified coordinates.
     *
     * @param x the x-coordinate (0-7)
     * @param y the y-coordinate (0-7)
     * @return the ChessPiece at the specified position, or null if no piece is present
     * @throws IndexOutOfBoundsException if the coordinates are out of bounds
     */
    public ChessPiece getPieceAt(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        return board[x][y];
    }

    /**
     * Returns the chess case at the specified coordinates.
     *
     * @param x the x-coordinate (0-7)
     * @param y the y-coordinate (0-7)
     * @return the chess case notation at the specified position
     * @throws IndexOutOfBoundsException if the coordinates are out of bounds
     */
    public String getCaseAt(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        return cases[x][y];
    }

    /**
     * Moves a piece from one chess case to another using their string representations.
     *
     * @param from the starting chess case in string format (e.g., "A1")
     * @param to   the target chess case in string format (e.g., "B2")
     * @throws IllegalArgumentException if no piece is found at the starting position
     */
    public void move(String from, String to) {
        ChessCaseEnumeration caseEnumFrom = ChessCaseEnumeration.valueOf(from.toUpperCase());
        ChessCaseEnumeration caseEnumTo = ChessCaseEnumeration.valueOf(to.toUpperCase());

        this.move(new Move(caseEnumFrom.getCoordinate(), caseEnumTo.getCoordinate()));
    }

    /**
     * Moves a piece from one coordinate to another.
     *
     * @param move the Move object containing the start and end coordinates
     * @throws IllegalArgumentException if no piece is found at the starting position
     */
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

    public void setPieceAt(int startX, int startY, ChessPiece piece) {
        if (startX < 0 || startX >= 8 || startY < 0 || startY >= 8) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds: (" + startX + ", " + startY + ")");
        }
        board[startX][startY] = piece;
    }
}
