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
package com.fcuillandre.chessbot.engine;

import com.fcuillandre.chessbot.model.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chess game engine managing game logic.
 * This class is responsible for maintaining the state of the chess game, including the board, current player, move
 * history, and game state. It provides methods to make moves, check for legal moves, and determine game outcomes such
 * as checkmate and stalemate.
 *
 * @author fcuillandre
 * @since 1.0
 */
public final class GameEngine {

    private final List<Move> moveHistory;
    private final List<GameListener> listeners;
    private final Map<String, Integer> positionHistory;
    @Getter
    private ChessBoard board;
    @Getter
    private ChessColor currentPlayer;
    @Getter
    private GameState gameState;
    private int halfMoveClock; // For the 50-move rule

    /**
     * Constructs a new GameEngine instance with an empty board and initial settings.
     */
    public GameEngine() {
        this.board = new ChessBoard();
        this.moveHistory = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.positionHistory = new HashMap<>();
        this.halfMoveClock = 0;
    }

    /**
     * Starts a new game.
     */
    public void newGame() {
        board = new ChessBoard();
        board.setupInitialPosition();
        currentPlayer = ChessColor.WHITE;
        gameState = GameState.PLAYING;
        moveHistory.clear();
        positionHistory.clear();
        halfMoveClock = 0;
        notifyBoardChanged();
        notifyGameStateChanged();
    }

    /**
     * Attempts to make a move.
     *
     * @return true if the move was executed successfully
     */
    public boolean makeMove(Move move) {
        if (gameState.isGameOver()) {
            return false;
        }

        // Check that it's the player's turn
        if (move.getPiece().getColor() != currentPlayer) {
            return false;
        }

        // Check that the move is legal
        if (!board.isMoveLegal(move)) {
            return false;
        }

        // Execute the move
        board.executeMove(move);
        moveHistory.add(move);

        // Update half-move clock
        if (move.isCapture() || move.getPiece().getType() == ChessPieceType.PAWN) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }

        // Change player
        currentPlayer = currentPlayer.opposite();

        // Record position for threefold-repetition detection
        String positionKey = board.toPositionKey(currentPlayer);
        positionHistory.merge(positionKey, 1, Integer::sum);

        // Update game state
        updateGameState();

        notifyBoardChanged();
        notifyGameStateChanged();
        notifyMoveMade(move);

        return true;
    }

    /**
     * Makes a move from start and end positions.
     */
    public boolean makeMove(ChessCaseEnumeration from, ChessCaseEnumeration to) {
        return makeMove(from.getPosition(), to.getPosition(), null);
    }

    /**
     * Makes a move with possible promotion.
     */
    public boolean makeMove(Position from, Position to, ChessPieceType promotionType) {
        List<Move> legalMoves = getLegalMovesFrom(from);

        for (Move move : legalMoves) {
            if (move.getTo().equals(to)) {
                if (move.isPromotion()) {
                    if (promotionType != null && move.getPromotionType() == promotionType) {
                        return makeMove(move);
                    }
                } else {
                    return makeMove(move);
                }
            }
        }

        return false;
    }

    /**
     * Updates the game state after a move.
     */
    private void updateGameState() {
        List<Move> legalMoves = board.getAllLegalMoves(currentPlayer);
        boolean inCheck = board.isKingInCheck(currentPlayer);

        if (legalMoves.isEmpty()) {
            if (inCheck) {
                gameState = GameState.CHECKMATE;
            } else {
                gameState = GameState.STALEMATE;
            }
        } else if (inCheck) {
            gameState = GameState.CHECK;
        } else if (halfMoveClock >= 100) { // 50 moves per player = 100 half-moves
            gameState = GameState.DRAW_FIFTY_MOVES;
        } else if (isThreefoldRepetition()) {
            gameState = GameState.DRAW_REPETITION;
        } else if (isInsufficientMaterial()) {
            gameState = GameState.DRAW_INSUFFICIENT_MATERIAL;
        } else {
            gameState = GameState.PLAYING;
        }
    }

    /**
     * Checks whether the current position has occurred three or more times (threefold repetition).
     *
     * @return {@code true} if the current position has appeared at least three times
     */
    private boolean isThreefoldRepetition() {
        String positionKey = board.toPositionKey(currentPlayer);
        return positionHistory.getOrDefault(positionKey, 0) >= 3;
    }

    /**
     * Checks if there is insufficient material to checkmate.
     * Covers: K-K, K+minor-K, and K+B vs K+B with bishops on the same square color.
     */
    private boolean isInsufficientMaterial() {
        int whiteBishops = 0, whiteKnights = 0;
        int blackBishops = 0, blackKnights = 0;
        Position whiteBishopPos = null;
        Position blackBishopPos = null;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPieceAt(new Position(row, col));
                if (piece != null) {
                    switch (piece.getType()) {
                        case QUEEN, ROOK, PAWN -> {
                            return false;
                        }
                        case BISHOP -> {
                            if (piece.getColor() == ChessColor.WHITE) {
                                whiteBishops++;
                                whiteBishopPos = new Position(row, col);
                            } else {
                                blackBishops++;
                                blackBishopPos = new Position(row, col);
                            }
                        }
                        case KNIGHT -> {
                            if (piece.getColor() == ChessColor.WHITE) whiteKnights++;
                            else blackKnights++;
                        }
                        case KING -> { } // Ignore kings
                    }
                }
            }
        }

        int whiteMinor = whiteBishops + whiteKnights;
        int blackMinor = blackBishops + blackKnights;

        // King vs King
        if (whiteMinor == 0 && blackMinor == 0) return true;

        // King + minor piece vs King
        if ((whiteMinor == 1 && blackMinor == 0) || (whiteMinor == 0 && blackMinor == 1)) {
            return true;
        }

        // King + Bishop vs King + Bishop, both bishops on the same square color
        if (whiteBishops == 1 && blackBishops == 1 && whiteKnights == 0 && blackKnights == 0) {
            boolean whiteOnLight = (whiteBishopPos.row() + whiteBishopPos.col()) % 2 == 0;
            boolean blackOnLight = (blackBishopPos.row() + blackBishopPos.col()) % 2 == 0;
            return whiteOnLight == blackOnLight;
        }

        return false;
    }

    /**
     * Gets all legal moves from a position.
     */
    public List<Move> getLegalMovesFrom(Position from) {
        ChessPiece piece = board.getPieceAt(from);
        if (piece == null || piece.getColor() != currentPlayer) {
            return new ArrayList<>();
        }

        List<Move> pseudoLegal = piece.getPseudoLegalMoves(from, board);
        List<Move> legal = new ArrayList<>();

        for (Move move : pseudoLegal) {
            if (board.isMoveLegal(move)) {
                legal.add(move);
            }
        }

        return legal;
    }

    /**
     * Gets all legal moves for the current player.
     */
    public List<Move> getAllLegalMoves() {
        return board.getAllLegalMoves(currentPlayer);
    }

    /**
     * The current player resigns.
     */
    public void resign() {
        gameState = GameState.RESIGNED;
        notifyGameStateChanged();
    }

    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }

    /**
     * Gets the last move made in the game.
     *
     * @return The last move, or null if no moves have been made.
     */
    public Move getLastMove() {
        if (moveHistory.isEmpty()) {
            return null;
        }
        return moveHistory.get(moveHistory.size() - 1);
    }

    /**
     * Returns the winner of the game if it has ended, or null if the game is still ongoing or ended in a draw.
     *
     * @return The color of the winning player, or null if no winner (ongoing game or draw).
     */
    public ChessColor getWinner() {
        if (gameState == GameState.CHECKMATE) {
            return currentPlayer.opposite();
        }
        if (gameState == GameState.RESIGNED) {
            return currentPlayer.opposite();
        }
        return null;
    }

    // Listener management
    public void addGameListener(GameListener listener) {
        listeners.add(listener);
    }

    public void removeGameListener(GameListener listener) {
        listeners.remove(listener);
    }

    private void notifyBoardChanged() {
        for (GameListener listener : listeners) {
            listener.onBoardChanged(board);
        }
    }

    private void notifyGameStateChanged() {
        for (GameListener listener : listeners) {
            listener.onGameStateChanged(gameState, currentPlayer);
        }
    }

    private void notifyMoveMade(Move move) {
        for (GameListener listener : listeners) {
            listener.onMoveMade(move);
        }
    }
}
