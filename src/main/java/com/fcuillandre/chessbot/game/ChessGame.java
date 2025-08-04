package com.fcuillandre.chessbot.game;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.pieces.*;
import com.fcuillandre.chessbot.utils.ChessUtils;

public class ChessGame {

    private ChessBoard board;
    private boolean whiteTurn = true;
    private boolean gameStarted = false;

    public ChessGame() {
        board = new ChessBoard();// ChessUtils.initializeBoard();
    }

    public boolean isValidMove(Move move) {
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();
        ChessPiece piece = this.board.getPieceAt(startX, startY);
        if (piece == null) {
            ChessUtils.log("No piece at starting position: " + this.board.getCaseAt(startX, startY));
            return false;
        }
        if ((whiteTurn && piece.getColor() == ChessColor.BLACK) || (!whiteTurn && piece.getColor() == ChessColor.WHITE)) {
            ChessUtils.log("It's not your turn to move this piece: " + piece);
            return false;
        }
        ChessUtils.log("Validating move for piece: " + piece + " from "
                + this.board.getCaseAt(startX, startY) + "(x: " + startX + ", y: " + startY + ") "
                + "to " + this.board.getCaseAt(endX, endY) +" (x: " + endX + ", y: " + endY + ")");

        boolean isValid = getMoveChecker(piece).isValidMove(piece, move, board);
        ChessUtils.log("Move is valid: " + isValid);

        return isValid;
    }

    // Pour compatibilité descendante
    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        return isValidMove(new Move(startX, startY, endX, endY));
    }

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
                // return new KnightMoveChecker();
                ChessUtils.log("Knight move checker not implemented yet.");
                break;
            case BISHOP:
                return new BishopMoveChecker();
            case QUEEN:
                // return new QueenMoveChecker();
                ChessUtils.log("Queen move checker not implemented yet.");
                break;
            case KING:
                // return new KingMoveChecker();
                ChessUtils.log("King move checker not implemented yet.");
                break;
            default:
                ChessUtils.log("No move checker found for piece type: " + piece.getType());
                break;
        }
        return null;
    }

    public void makeMove(Move move) {
        int startX = move.getStartX();
        int startY = move.getStartY();
        int endX = move.getEndX();
        int endY = move.getEndY();
        ChessUtils.log("Making move: (x " + startX + ", y " + startY + ") (x " + endX + ", y " + endY + ")");
        ChessUtils.log("Making move: " + board.getCaseAt(startX, startY) + " " + board.getCaseAt(endX, endY));

        if (isValidMove(move)) {
            ChessUtils.log("Valid move!");
            this.board.move(startX, startY, endX, endY);
            whiteTurn = !whiteTurn; // Switch turn
        }
    }

    // Pour compatibilité descendante
    public void makeMove(int startX, int startY, int endX, int endY) {
        makeMove(new Move(startX, startY, endX, endY));
    }

    public ChessBoard getBoard() {
        return board;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void startGame() {
        gameStarted = true;
        whiteTurn = true; // White starts first
    }

    public boolean isGameStarted() {
        return gameStarted; // Return true if the game has started
    }

    public boolean isGameOver() {
        return false;
    }

    public void displayBoard() {
        ChessUtils.printBoard(getBoard().getBoard());
    }
}