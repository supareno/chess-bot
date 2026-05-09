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
 * Represents the chessboard and its current state.
 * This class manages the positions of pieces, en passant targets, and provides methods to manipulate the board state,
 * check for checks, and generate legal moves.
 *
 * @author fcuillandre
 * @since 0.1
 */
public final class ChessBoard {

    private final ChessPiece[][] squares;
    @Getter
    @Setter
    private Position enPassantTarget;

    /**
     * Initializes an empty chessboard.
     */
    public ChessBoard() {
        this.squares = new ChessPiece[8][8];
        this.enPassantTarget = null;
    }

    /**
     * Initializes the chessboard with the standard starting position.
     */
    public void setupInitialPosition() {
        // Black pieces (rows 0 and 1)
        setupBackRank(0, ChessColor.BLACK);
        setupPawnRank(1, ChessColor.BLACK);

        // Empty squares in the middle
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = null;
            }
        }

        // White pieces (rows 6 and 7)
        setupPawnRank(6, ChessColor.WHITE);
        setupBackRank(7, ChessColor.WHITE);

        enPassantTarget = null;
    }

    private void setupBackRank(int row, ChessColor color) {
        squares[row][0] = new ChessPiece(ChessPieceType.ROOK, color);
        squares[row][1] = new ChessPiece(ChessPieceType.KNIGHT, color);
        squares[row][2] = new ChessPiece(ChessPieceType.BISHOP, color);
        squares[row][3] = new ChessPiece(ChessPieceType.QUEEN, color);
        squares[row][4] = new ChessPiece(ChessPieceType.KING, color);
        squares[row][5] = new ChessPiece(ChessPieceType.BISHOP, color);
        squares[row][6] = new ChessPiece(ChessPieceType.KNIGHT, color);
        squares[row][7] = new ChessPiece(ChessPieceType.ROOK, color);
    }

    private void setupPawnRank(int row, ChessColor color) {
        for (int col = 0; col < 8; col++) {
            squares[row][col] = new ChessPiece(ChessPieceType.PAWN, color);
        }
    }

    /**
     * Retrieves the piece at a given position.
     *
     * @param row the row index (0-7)
     * @param col the column index (0-7)
     * @return the piece at the position, or null if the square is empty or invalid
     */
    public ChessPiece getPieceAt(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null;
        }
        return squares[row][col];
    }

    /**
     * Retrieves the piece at a given position.
     *
     * @return the piece at the position, or null if the square is empty or invalid
     */
    public ChessPiece getPieceAt(Position pos) {
        if (!pos.isValid()) {
            return null;
        }
        return squares[pos.row()][pos.col()];
    }

    /**
     * Sets a piece at a given position.
     *
     * @param pos   the position to set the piece at
     * @param piece the piece to place (or null to clear the square)
     */
    public void setPieceAt(Position pos, ChessPiece piece) {
        if (pos.isValid()) {
            squares[pos.row()][pos.col()] = piece;
        }
    }

    /**
     * Finds the position of the king of a given color.
     *
     * @param color the color of the king to find
     * @return the position of the king, or null if not found (should never happen in a valid game)
     */
    public Position findKing(ChessColor color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                if (piece != null && piece.getType() == ChessPieceType.KING && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    /**
     * Checks if a square is attacked by any piece of the given color.
     *
     * @param target        the position to check
     * @param attackerColor the color of the attacking pieces
     * @return true if the square is attacked, false otherwise
     */
    public boolean isSquareAttacked(Position target, ChessColor attackerColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                if (piece != null && piece.getColor() == attackerColor) {
                    Position from = new Position(row, col);
                    if (canAttack(piece, from, target)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if a piece can attack a square (without considering checks).
     */
    private boolean canAttack(ChessPiece piece, Position from, Position target) {
        int rowDiff = target.row() - from.row();
        int colDiff = target.col() - from.col();

        return switch (piece.getType()) {
            case KING -> Math.abs(rowDiff) <= 1 && Math.abs(colDiff) <= 1;
            case QUEEN -> canAttackStraight(from, target) || canAttackDiagonal(from, target);
            case ROOK -> canAttackStraight(from, target);
            case BISHOP -> canAttackDiagonal(from, target);
            case KNIGHT -> (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 1) ||
                    (Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 2);
            case PAWN -> {
                int direction = piece.getColor().getPawnDirection();
                yield rowDiff == direction && Math.abs(colDiff) == 1;
            }
        };
    }

    private boolean canAttackStraight(Position from, Position target) {
        int rowDiff = target.row() - from.row();
        int colDiff = target.col() - from.col();

        if (rowDiff != 0 && colDiff != 0) return false;

        int rowStep = Integer.compare(rowDiff, 0);
        int colStep = Integer.compare(colDiff, 0);

        Position current = from.offset(rowStep, colStep);
        while (!current.equals(target)) {
            if (getPieceAt(current) != null) return false;
            current = current.offset(rowStep, colStep);
        }
        return true;
    }

    private boolean canAttackDiagonal(Position from, Position target) {
        int rowDiff = target.row() - from.row();
        int colDiff = target.col() - from.col();

        if (Math.abs(rowDiff) != Math.abs(colDiff) || rowDiff == 0) return false;

        int rowStep = Integer.compare(rowDiff, 0);
        int colStep = Integer.compare(colDiff, 0);

        Position current = from.offset(rowStep, colStep);
        while (!current.equals(target)) {
            if (getPieceAt(current) != null) return false;
            current = current.offset(rowStep, colStep);
        }
        return true;
    }

    /**
     * Checks if the king of a given color is in check.
     *
     * @param color the color of the king to check
     * @return true if the king is in check, false otherwise
     */
    public boolean isKingInCheck(ChessColor color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false;
        return isSquareAttacked(kingPos, color.opposite());
    }

    /**
     * Generates all legal moves for a given color, filtering out moves that would leave the king in check.
     *
     * @param color the color to generate moves for
     * @return a list of legal moves for the given color
     */
    public List<Move> getAllLegalMoves(ChessColor color) {
        List<Move> legalMoves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                if (piece != null && piece.getColor() == color) {
                    Position from = new Position(row, col);
                    List<Move> pieceMoves = piece.getPseudoLegalMoves(from, this);

                    for (Move move : pieceMoves) {
                        if (isMoveLegal(move)) {
                            legalMoves.add(move);
                        }
                    }
                }
            }
        }

        return legalMoves;
    }

    /**
     * Checks if a move is legal (does not leave the king in check).
     *
     * @param move the move to check
     * @return true if the move is legal, false otherwise
     */
    public boolean isMoveLegal(Move move) {
        // Simulate the move
        ChessBoard testBoard = this.copy();
        testBoard.executeMove(move);

        // Check if the king is in check after the move
        return !testBoard.isKingInCheck(move.getPiece().getColor());
    }

    /**
     * Executes a move on the chessboard.
     *
     * @param move the move to execute
     */
    public void executeMove(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        ChessPiece piece = getPieceAt(from);

        // Reset en passant target
        Position newEnPassantTarget = null;

        if (move.isCastling()) {
            // Move the king
            setPieceAt(to, piece);
            setPieceAt(from, null);
            piece.setHasMoved(true);

            // Move the rook
            int rookFromCol = to.col() > from.col() ? 7 : 0;
            int rookToCol = to.col() > from.col() ? 5 : 3;
            Position rookFrom = new Position(from.row(), rookFromCol);
            Position rookTo = new Position(from.row(), rookToCol);
            ChessPiece rook = getPieceAt(rookFrom);
            setPieceAt(rookTo, rook);
            setPieceAt(rookFrom, null);
            rook.setHasMoved(true);
        } else if (move.isEnPassant()) {
            // Move the pawn
            setPieceAt(to, piece);
            setPieceAt(from, null);
            piece.setHasMoved(true);

            // Remove the captured pawn
            Position capturedPos = new Position(from.row(), to.col());
            setPieceAt(capturedPos, null);
        } else if (move.isPromotion()) {
            // Create the new promoted piece
            ChessPiece promotedPiece = new ChessPiece(move.getPromotionType(), piece.getColor());
            promotedPiece.setHasMoved(true);
            setPieceAt(to, promotedPiece);
            setPieceAt(from, null);
        } else {
            // Normal move
            setPieceAt(to, piece);
            setPieceAt(from, null);
            piece.setHasMoved(true);

            // Check if it's a pawn double move
            if (piece.getType() == ChessPieceType.PAWN && Math.abs(to.row() - from.row()) == 2) {
                newEnPassantTarget = new Position((from.row() + to.row()) / 2, from.col());
            }
        }

        setEnPassantTarget(newEnPassantTarget);
    }

    /**
     * Creates a deep copy of the chessboard.
     *
     * @return a new ChessBoard instance with the same piece positions and en passant target
     */
    public ChessBoard copy() {
        ChessBoard copy = new ChessBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                copy.squares[row][col] = piece != null ? piece.copy() : null;
            }
        }
        copy.enPassantTarget = this.enPassantTarget;
        return copy;
    }

    /**
     * Produces a compact string key that uniquely identifies the board position for a given side to move.
     * Used to detect threefold repetition.
     * The key encodes every square's piece (type + color) and the current en passant target.
     *
     * @param sideToMove the color of the player whose turn it is
     * @return a string key representing the position
     */
    public String toPositionKey(ChessColor sideToMove) {
        StringBuilder sb = new StringBuilder(67);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                if (piece == null) {
                    sb.append('.');
                } else {
                    sb.append(piece.getSymbol());
                }
            }
        }
        sb.append(sideToMove == ChessColor.WHITE ? 'W' : 'B');
        sb.append(enPassantTarget != null ? enPassantTarget.toAlgebraic() : "-");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");
        for (int row = 0; row < 8; row++) {
            sb.append(8 - row).append(" ");
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                sb.append(piece != null ? piece.getSymbol() : '.').append(" ");
            }
            sb.append(8 - row).append("\n");
        }
        sb.append("  a b c d e f g h\n");
        return sb.toString();
    }
}
