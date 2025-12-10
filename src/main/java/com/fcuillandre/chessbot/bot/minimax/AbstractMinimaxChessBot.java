package com.fcuillandre.chessbot.bot.minimax;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.bot.ChessBot;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.checkers.*;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMinimaxChessBot implements ChessBot {

    void applyMoveOnBoard(ChessBoard board, Move move) {
        board.move(move);
        int endX = move.getEnd().getX();
        int endY = move.getEnd().getY();
        ChessPiece piece = board.getPieceAt(endX, endY);
        if (piece != null && piece.getType() == ChessPieceType.PAWN) {
            // White promotes at x==7, Black promotes at x==0
            boolean promote = piece.getColor() == ChessColor.BLACK && endX == 0;
            if (promote) {
                ChessPieceType promoteTo = move.getPromotionPieceType() != null ? move.getPromotionPieceType() : ChessPieceType.QUEEN;
                board.setPieceAt(endX, endY, new ChessPiece(piece.getColor(), promoteTo));
            }
        }
    }

    List<Move> generateLegalMovesForColor(ChessBoard board, ChessColor color, ChessGame gameCtx) {
        List<Move> legal = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getColor() == color) {
                    MoveChecker checker = getChecker(piece.getType());
                    for (int dx = 0; dx < 8; dx++) {
                        for (int dy = 0; dy < 8; dy++) {
                            if (x == dx && y == dy) continue;
                            Move m;
                            if (piece.getType() == ChessPieceType.PAWN && dx == 0) {
                                m = new Move(new Coordinate(x, y), new Coordinate(dx, dy), ChessPieceType.QUEEN);
                            } else {
                                m = new Move(new Coordinate(x, y), new Coordinate(dx, dy));
                            }
                            if (checker.isValidMove(piece, m, board, gameCtx)) {
                                ChessBoard tmp = copyBoard(board);
                                applyMoveOnBoard(tmp, m);
                                if (!isKingInCheckForColor(tmp, color, gameCtx)) {
                                    legal.add(m);
                                }
                            }
                        }
                    }
                }
            }
        }
        return legal;
    }

    boolean isKingInCheckForColor(ChessBoard board, ChessColor color, ChessGame gameCtx) {
        Coordinate kingPos = findKing(board, color);
        if (kingPos == null) return false;
        ChessColor opponent = (color == ChessColor.WHITE) ? ChessColor.BLACK : ChessColor.WHITE;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece p = board.getPieceAt(x, y);
                if (p != null && p.getColor() == opponent) {
                    MoveChecker checker = getChecker(p.getType());
                    Move attack = new Move(new Coordinate(x, y), kingPos);
                    if (checker.isValidMove(p, attack, board, gameCtx)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    Coordinate findKing(ChessBoard board, ChessColor color) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece p = board.getPieceAt(x, y);
                if (p != null && p.getType() == ChessPieceType.KING && p.getColor() == color) {
                    return new Coordinate(x, y);
                }
            }
        }
        return null;
    }

    MoveChecker getChecker(ChessPieceType type) {
        return switch (type) {
            case PAWN -> new PawnMoveChecker();
            case ROOK -> new RookMoveChecker();
            case KNIGHT -> new KnightMoveChecker();
            case BISHOP -> new BishopMoveChecker();
            case QUEEN -> new QueenMoveChecker();
            case KING -> new KingMoveChecker();
        };
    }

    ChessBoard copyBoard(ChessBoard original) {
        ChessBoard copy = new ChessBoard();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece p = original.getPieceAt(x, y);
                if (p != null) {
                    copy.setPieceAt(x, y, new ChessPiece(p.getColor(), p.getType()));
                } else {
                    copy.setPieceAt(x, y, null);
                }
            }
        }
        return copy;
    }

    int evaluateBoard(ChessBoard board) {
        int score = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece p = board.getPieceAt(x, y);
                if (p != null) {
                    int val = pieceValue(p.getType());
                    score += (p.getColor() == ChessColor.BLACK) ? val : -val;
                }
            }
        }
        return score;
    }

    int pieceValue(ChessPieceType type) {
        return switch (type) {
            case PAWN -> 10;
            case KNIGHT, BISHOP -> 35;
            case ROOK -> 50;
            case QUEEN -> 100;
            case KING -> 20000;
        };
    }

    ChessColor opposite(ChessColor c) {
        return (c == ChessColor.WHITE) ? ChessColor.BLACK : ChessColor.WHITE;
    }

    List<Move> generateLegalMovesUsingGame(ChessGame game, ChessColor color) {
        List<Move> moves = new ArrayList<>();
        ChessBoard board = game.getBoard();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece p = board.getPieceAt(x, y);
                if (p == null || p.getColor() != color) continue;
                for (int dx = 0; dx < 8; dx++) {
                    for (int dy = 0; dy < 8; dy++) {
                        if (x == dx && y == dy) continue;
                        Move m = new Move(new Coordinate(x, y), new Coordinate(dx, dy));
                        if (game.isValidMove(m)) {
                            moves.add(m);
                        }
                    }
                }
            }
        }
        return moves;
    }
}
