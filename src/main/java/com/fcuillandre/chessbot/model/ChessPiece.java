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
package com.fcuillandre.chessbot.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chess piece.
 * Each piece has a type (king, queen, rook, bishop, knight, pawn), a color (white or black), and a flag indicating if
 * it has moved (important for castling and en passant).
 * This class provides methods to get the piece's value, symbol, and generate pseudo-legal moves based on its type and
 * position on the board.
 *
 * @author fcuillandre
 * @since 0.1
 */
public final class ChessPiece {

    @Getter
    private final ChessPieceType type;
    @Getter
    private final ChessColor color;
    @Getter
    @Setter
    private boolean hasMoved;

    /**
     * Constructs a ChessPiece with the specified type and color.
     *
     * @param type  the type of the chess piece (king, queen, rook, bishop, knight, pawn)
     * @param color the color of the chess piece (white or black)
     */
    public ChessPiece(ChessPieceType type, ChessColor color) {
        this.type = type;
        this.color = color;
        this.hasMoved = false;
    }

    /**
     * Returns the value of the piece based on its type.
     * King is typically considered to have an infinite value, but for practical purposes, we can assign it a high value.
     *
     * @return the value of the piece
     */
    public int getValue() {
        return type.getValue();
    }

    /**
     * Returns the symbol representing the piece, which can be used for display purposes.
     * The symbol is determined by the piece type and color.
     *
     * @return the symbol of the piece
     */
    public char getSymbol() {
        return type.getSymbol(color);
    }

    /**
     * Generates a list of pseudo-legal moves for this piece from the given position on the board.
     * Pseudo-legal moves are moves that follow the movement rules of the piece but do not consider checks or pins.
     *
     * @param from  the current position of the piece
     * @param board the current state of the chess board
     * @return a list of pseudo-legal moves for this piece
     */
    public List<Move> getPseudoLegalMoves(Position from, ChessBoard board) {
        return switch (type) {
            case KING -> getKingMoves(from, board);
            case QUEEN -> getQueenMoves(from, board);
            case ROOK -> getRookMoves(from, board);
            case BISHOP -> getBishopMoves(from, board);
            case KNIGHT -> getKnightMoves(from, board);
            case PAWN -> getPawnMoves(from, board);
        };
    }

    /**
     * Generates pseudo-legal moves for the king, including normal moves and castling.
     *
     * @param from  the current position of the king
     * @param board the current state of the chess board
     * @return a list of pseudo-legal moves for the king
     */
    private List<Move> getKingMoves(Position from, ChessBoard board) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] dir : directions) {
            Position to = from.offset(dir[0], dir[1]);
            if (to.isValid()) {
                ChessPiece target = board.getPieceAt(to);
                if (target == null || target.color != this.color) {
                    moves.add(new Move(from, to, this, target));
                }
            }
        }

        // Castling
        if (!hasMoved && !board.isKingInCheck(color)) {
            // Kingside castling
            Position kingSideRook = new Position(from.row(), 7);
            if (canCastle(from, kingSideRook, board)) {
                moves.add(Move.createCastling(from, from.offset(0, 2), this));
            }
            // Queenside castling
            Position queenSideRook = new Position(from.row(), 0);
            if (canCastle(from, queenSideRook, board)) {
                moves.add(Move.createCastling(from, from.offset(0, -2), this));
            }
        }

        return moves;
    }

    /**
     * Checks if castling is possible with the given rook position.
     * This method verifies that the rook is in the correct position, has not moved, and that the squares between
     * the king and rook are empty and not attacked.
     *
     * @param kingPos the current position of the king
     * @param rookPos the position of the rook involved in castling
     * @param board   the current state of the chess board
     * @return true if castling is possible, false otherwise
     */
    private boolean canCastle(Position kingPos, Position rookPos, ChessBoard board) {
        ChessPiece rook = board.getPieceAt(rookPos);
        if (rook == null || rook.type != ChessPieceType.ROOK || rook.color != this.color || rook.hasMoved) {
            return false;
        }

        int direction = rookPos.col() > kingPos.col() ? 1 : -1;
        int endCol = rookPos.col() > kingPos.col() ? 6 : 2;

        // Check that squares are empty
        for (int col = kingPos.col() + direction; col != rookPos.col(); col += direction) {
            if (board.getPieceAt(new Position(kingPos.row(), col)) != null) {
                return false;
            }
        }

        // Check that the king doesn't pass through an attacked square
        for (int col = kingPos.col(); col != endCol + direction; col += direction) {
            if (board.isSquareAttacked(new Position(kingPos.row(), col), color.opposite())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Generates pseudo-legal moves for the queen, which combines the movement of a rook and bishop.
     *
     * @param from  the current position of the queen
     * @param board the current state of the chess board
     * @return a list of pseudo-legal moves for the queen
     */
    private List<Move> getQueenMoves(Position from, ChessBoard board) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(getSlidingMoves(from, board, new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}));
        moves.addAll(getSlidingMoves(from, board, new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}}));
        return moves;
    }

    /**
     * Generates pseudo-legal moves for the rook, which can move any number of squares along a rank or file.
     *
     * @param from  the current position of the rook
     * @param board the current state of the chess board
     * @return a list of pseudo-legal moves for the rook
     */
    private List<Move> getRookMoves(Position from, ChessBoard board) {
        return getSlidingMoves(from, board, new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}});
    }

    /**
     * Generates pseudo-legal moves for the bishop, which can move any number of squares diagonally.
     *
     * @param from  the current position of the bishop
     * @param board the current state of the chess board
     * @return a list of pseudo-legal moves for the bishop
     */
    private List<Move> getBishopMoves(Position from, ChessBoard board) {
        return getSlidingMoves(from, board, new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}});
    }

    /**
     * Helper method to generate sliding moves for pieces like rooks, bishops, and queens.
     * It iterates in the specified directions until it hits the edge of the board or another piece.
     *
     * @param from       the current position of the piece
     * @param board      the current state of the chess board
     * @param directions an array of direction vectors to slide in (e.g., {{-1, 0}, {1, 0}} for vertical moves)
     * @return a list of pseudo-legal moves in the specified directions
     */
    private List<Move> getSlidingMoves(Position from, ChessBoard board, int[][] directions) {
        List<Move> moves = new ArrayList<>();

        for (int[] dir : directions) {
            Position current = from.offset(dir[0], dir[1]);
            while (current.isValid()) {
                ChessPiece target = board.getPieceAt(current);
                if (target == null) {
                    moves.add(new Move(from, current, this));
                } else {
                    if (target.color != this.color) {
                        moves.add(new Move(from, current, this, target));
                    }
                    break;
                }
                current = current.offset(dir[0], dir[1]);
            }
        }

        return moves;
    }

    /**
     * Generates pseudo-legal moves for the knight, which moves in an L-shape (two squares in one direction and then
     * one square perpendicular).
     *
     * @param from  the current position of the knight
     * @param board the current state of the chess board
     * @return a list of pseudo-legal moves for the knight
     */
    private List<Move> getKnightMoves(Position from, ChessBoard board) {
        List<Move> moves = new ArrayList<>();
        int[][] offsets = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] offset : offsets) {
            Position to = from.offset(offset[0], offset[1]);
            if (to.isValid()) {
                ChessPiece target = board.getPieceAt(to);
                if (target == null || target.color != this.color) {
                    moves.add(new Move(from, to, this, target));
                }
            }
        }

        return moves;
    }

    /**
     * Generates pseudo-legal moves for the pawn, which has unique movement rules including forward moves, diagonal
     * captures, en passant, and promotion.
     *
     * @param from  the current position of the pawn
     * @param board the current state of the chess board
     * @return a list of pseudo-legal moves for the pawn
     */
    private List<Move> getPawnMoves(Position from, ChessBoard board) {
        List<Move> moves = new ArrayList<>();
        int direction = color.getPawnDirection();
        int startRow = color.getPawnStartRow();
        int promotionRow = color.getPromotionRow();

        // Advance one square
        Position oneStep = from.offset(direction, 0);
        if (oneStep.isValid() && board.getPieceAt(oneStep) == null) {
            if (oneStep.row() == promotionRow) {
                addPromotionMoves(moves, from, oneStep, null);
            } else {
                moves.add(new Move(from, oneStep, this));
            }

            // Advance two squares from starting position
            if (from.row() == startRow) {
                Position twoSteps = from.offset(direction * 2, 0);
                if (board.getPieceAt(twoSteps) == null) {
                    moves.add(new Move(from, twoSteps, this));
                }
            }
        }

        // Diagonal captures
        for (int colOffset : new int[]{-1, 1}) {
            Position capturePos = from.offset(direction, colOffset);
            if (capturePos.isValid()) {
                ChessPiece target = board.getPieceAt(capturePos);
                if (target != null && target.color != this.color) {
                    if (capturePos.row() == promotionRow) {
                        addPromotionMoves(moves, from, capturePos, target);
                    } else {
                        moves.add(new Move(from, capturePos, this, target));
                    }
                }

                // En passant capture
                Position enPassantTarget = board.getEnPassantTarget();
                if (enPassantTarget != null && capturePos.equals(enPassantTarget)) {
                    Position capturedPawnPos = new Position(from.row(), capturePos.col());
                    ChessPiece capturedPawn = board.getPieceAt(capturedPawnPos);
                    moves.add(Move.createEnPassant(from, capturePos, this, capturedPawn));
                }
            }
        }

        return moves;
    }

    /**
     * Helper method to add promotion moves for a pawn that reaches the promotion rank.
     * It generates moves for promoting to a queen, rook, bishop, or knight.
     *
     * @param moves    the list of moves to add the promotion moves to
     * @param from     the starting position of the pawn
     * @param to       the target position where the pawn promotes
     * @param captured the piece being captured (if any) during the promotion move
     */
    private void addPromotionMoves(List<Move> moves, Position from, Position to, ChessPiece captured) {
        moves.add(Move.createPromotion(from, to, this, captured, ChessPieceType.QUEEN));
        moves.add(Move.createPromotion(from, to, this, captured, ChessPieceType.ROOK));
        moves.add(Move.createPromotion(from, to, this, captured, ChessPieceType.BISHOP));
        moves.add(Move.createPromotion(from, to, this, captured, ChessPieceType.KNIGHT));
    }

    /**
     * Creates a copy of the piece.
     *
     * @return a new ChessPiece instance with the same type, color, and hasMoved status as the original piece
     */
    public ChessPiece copy() {
        ChessPiece copy = new ChessPiece(type, color);
        copy.hasMoved = this.hasMoved;
        return copy;
    }

    @Override
    public String toString() {
        return String.valueOf(getSymbol());
    }
}
