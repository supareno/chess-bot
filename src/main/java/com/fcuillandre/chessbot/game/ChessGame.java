package com.fcuillandre.chessbot.game;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.board.ChessCaseEnumeration;
import com.fcuillandre.chessbot.game.checkers.*;
import com.fcuillandre.chessbot.pieces.*;
import com.fcuillandre.chessbot.utils.ChessUtils;
import lombok.Getter;

public class ChessGame {

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

    public ChessGame() {
        board = new ChessBoard();// ChessUtils.initializeBoard();
    }

    public boolean isValidMove(Move move) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
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

        return getMoveChecker(piece).isValidMove(piece, move, board, this);
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

    public void makeMove(Move move) {
        int startX = move.getStart().getX();
        int startY = move.getStart().getY();
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();

        if (isValidMove(move)) {
            ChessUtils.log("Move " + board.getCaseAt(startX, startY) + " " + board.getCaseAt(endX, endY) + " is a valid move");
            ChessUtils.log("---");
            this.board.move(move);
            ChessPiece piece = this.board.getPieceAt(startX, startY);
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
            whiteTurn = !whiteTurn; // Switch turn
        } else {
            ChessUtils.log("Argh, move " + board.getCaseAt(startX, startY) + " " + board.getCaseAt(endX, endY) + " is NOT a valid move");
            ChessUtils.log("---");
        }
    }

    // Pour compatibilité descendante
    public void makeMove(int startX, int startY, int endX, int endY) {
        makeMove(new Move(new Coordinate(startX, startY), new Coordinate(endX, endY)));
    }

    public void makeMove(String from, String to) {
        //ChessUtils.log("Making move from " + from + " to " + to);
        ChessCaseEnumeration caseEnumFrom = ChessCaseEnumeration.valueOf(from.toUpperCase());
        ChessCaseEnumeration caseEnumTo = ChessCaseEnumeration.valueOf(to.toUpperCase());
        this.makeMove(new Move(caseEnumFrom.getCoordinate(), caseEnumTo.getCoordinate()));
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

    // Getters pour le suivi du roque
    public boolean hasWhiteKingMoved() { return whiteKingMoved; }
    public boolean hasBlackKingMoved() { return blackKingMoved; }
    public boolean hasWhiteKingsideRookMoved() { return whiteKingsideRookMoved; }
    public boolean hasWhiteQueensideRookMoved() { return whiteQueensideRookMoved; }
    public boolean hasBlackKingsideRookMoved() { return blackKingsideRookMoved; }
    public boolean hasBlackQueensideRookMoved() { return blackQueensideRookMoved; }
}