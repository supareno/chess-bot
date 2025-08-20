package com.fcuillandre.chessbot.game;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.board.ChessCaseEnumeration;
import com.fcuillandre.chessbot.game.checkers.*;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import com.fcuillandre.chessbot.utils.ChessUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * ChessGame represents a chess game with a board, move history, and game state.
 * It handles the logic for making moves, checking validity, and tracking special conditions like castling and en passant.
 * <p>
 * This class is responsible for managing the game state, including the current turn, move history, and board configuration.
 * It provides methods to make moves, check their validity, and undo moves.
 * </p>
 *
 * @author FCuillandre
 * @version 1.0
 */
public class ChessGame {

    @Getter
    private final java.util.List<Move> moveHistory = new java.util.ArrayList<>();
    @Getter
    private ChessBoard board;
    @Getter
    private boolean whiteTurn = true;
    private boolean gameStarted = false;
    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;
    private boolean whiteKingsideRookMoved = false;
    private boolean whiteQueensideRookMoved = false;
    private boolean blackKingsideRookMoved = false;
    private boolean blackQueensideRookMoved = false;
    @Getter
    private Move lastMove = null;
    @Setter
    @Getter
    private boolean enPassant = false;

    /**
     * Constructor for ChessGame.
     * Initializes a new chess board.
     */
    public ChessGame() {
        board = new ChessBoard();
    }

    /**
     * Checks if it's the white player's turn.
     * <p>It is using MoveChecker implementation of each piece to check if the move is valid or not</p>
     *
     * @return true if it's white's turn, false otherwise
     * @see MoveChecker
     */
    public boolean isValidMove(Move move) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        ChessPiece piece = this.board.getPieceAt(startX, startY);
        if (piece == null) {
            ChessUtils.log("No piece at starting position: " + this.board.getCaseAt(startX, startY));
            return false;
        }
        if ((isWhiteTurn() && piece.getColor() == ChessColor.BLACK) || (!isWhiteTurn() && piece.getColor() == ChessColor.WHITE)) {

            ChessUtils.log("It's not your turn to move this piece: " + piece);
            ChessUtils.log(" - white turn : " + isWhiteTurn());
            ChessUtils.log(" - piece color: " + piece.getColor());
            return false;
        }

        return getMoveChecker(piece).isValidMove(piece, move, board, this);
    }

    /**
     * Returns the MoveChecker for the given piece type.
     * <p>This method is responsible for returning the correct MoveChecker based on the piece type.</p>
     *
     * @param piece The chess piece for which to get the MoveChecker
     * @return The MoveChecker for the piece, or null if no checker is found
     */
    private MoveChecker getMoveChecker(ChessPiece piece) {
        // Here you would return the appropriate MoveChecker based on the piece type
        if (piece == null) {
            return null; // No piece, no move checker
        }
        switch (piece.getType()) {
            case PAWN:
                return new PawnMoveChecker();
            case ROOK:
                return new RookMoveChecker();
            case KNIGHT:
                return new KnightMoveChecker();
            case BISHOP:
                return new BishopMoveChecker();
            case QUEEN:
                return new QueenMoveChecker();
            case KING:
                return new KingMoveChecker();
            default:
                ChessUtils.log("No move checker found for piece type: " + piece.getType());
                break;
        }
        return null;
    }

    /**
     * Makes a move on the chess board.
     * <p>This method checks if the move is valid, updates the board, and handles special cases like en passant and castling.</p>
     *
     * @param move The move to be made
     */
    public void makeMove(Move move) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        enPassant = false;
        if (isValidMove(move)) {
            ChessUtils.log("Move " + board.getCaseAt(startX, startY) + " " + board.getCaseAt(endX, endY) + " is a valid move");
            ChessUtils.log("---");
            ChessPiece piece = this.board.getPieceAt(startX, startY);
            boolean isKing = piece != null && piece.getType() == ChessPieceType.KING;
            boolean isCastling = isKing && Math.abs(endY - startY) == 2 && startX == endX;

            this.board.move(move);
            addMoveToHistory(move);
            if (enPassant) {
                // Retire le pion adverse pris en passant. le pion adverse est celui qui a été déplacé de deux cases lors du dernier coup
                Move lastMove = this.lastMove;
                int startXLast = lastMove.getStart().getX();
                int endXLast = lastMove.getEnd().getX();
                int endYLast = lastMove.getEnd().getY();
                board.getBoard()[endXLast][endYLast] = null;
            }
            if (isCastling) {
                // Déplacement de la tour lors du roque
                boolean kingside = endY < startY;
                int rookStartY = kingside ? 0 : 7;
                int rookEndY = kingside ? endY + 1 : endY - 1;
                this.board.move(
                        new Move(
                                new Coordinate(startX, rookStartY),
                                new Coordinate(startX, rookEndY)));
            }
            if (piece != null) {
                // Suivi du premier mouvement du roi et des tours
                if (piece.getType() == ChessPieceType.KING) {
                    if (piece.getColor() == ChessColor.WHITE) whiteKingMoved = true;
                    else blackKingMoved = true;
                } else if (piece.getType() == ChessPieceType.ROOK) {
                    if (piece.getColor() == ChessColor.WHITE) {
                        if (startX == 0 && startY == 0) whiteQueensideRookMoved = true;
                        if (startX == 7 && startY == 0) whiteKingsideRookMoved = true;
                    } else {
                        if (startX == 0 && startY == 7) blackQueensideRookMoved = true;
                        if (startX == 7 && startY == 7) blackKingsideRookMoved = true;
                    }
                }
            }
            lastMove = move;
            whiteTurn = !whiteTurn; // Switch turn
        } else {
            ChessUtils.log("Argh, move " + board.getCaseAt(startX, startY) + " " + board.getCaseAt(endX, endY) + " is NOT a valid move");
            ChessUtils.log("---");
        }
    }


    public void makeMove(String from, String to) {
        ChessCaseEnumeration caseEnumFrom = ChessCaseEnumeration.valueOf(from.toUpperCase());
        ChessCaseEnumeration caseEnumTo = ChessCaseEnumeration.valueOf(to.toUpperCase());
        this.makeMove(new Move(caseEnumFrom.getCoordinate(), caseEnumTo.getCoordinate()));
    }

    public void startGame() {
        gameStarted = true;
        whiteTurn = true; // White starts first
    }

    // Getters pour le suivi du roque
    public boolean hasWhiteKingMoved() {
        return whiteKingMoved;
    }

    public boolean hasBlackKingMoved() {
        return blackKingMoved;
    }

    public boolean hasWhiteKingsideRookMoved() {
        return whiteKingsideRookMoved;
    }

    public boolean hasWhiteQueensideRookMoved() {
        return whiteQueensideRookMoved;
    }

    public boolean hasBlackKingsideRookMoved() {
        return blackKingsideRookMoved;
    }

    public boolean hasBlackQueensideRookMoved() {
        return blackQueensideRookMoved;
    }

    public void addMoveToHistory(Move move) {
        moveHistory.add(move);
    }

    public void undoMove() {
        if (moveHistory.isEmpty()) {
            ChessUtils.log("No moves to undo.");
            return;
        }
        Move lastMove = moveHistory.remove(moveHistory.size() - 1);
        int startX = lastMove.getStart().getX();
        int startY = lastMove.getStart().getY();
        int endX = lastMove.getEnd().getX();
        int endY = lastMove.getEnd().getY();

        // Restore the piece at the starting position
        ChessPiece piece = this.board.getPieceAt(endX, endY);
        this.board.setPieceAt(startX, startY, piece);
        this.board.setPieceAt(endX, endY, null);

        // Handle special cases like en passant and castling
        if (enPassant && piece != null && piece.getType() == ChessPieceType.PAWN) {
            // Restore the pawn that was captured en passant
            this.board.setPieceAt(endX, endY + (piece.getColor() == ChessColor.WHITE ? -1 : 1), new ChessPiece(ChessColor.BLACK, ChessPieceType.PAWN));
        }

        // Reset the turn
        whiteTurn = !whiteTurn;
    }

    public void clearMoveHistory() {
        moveHistory.clear();
    }

}