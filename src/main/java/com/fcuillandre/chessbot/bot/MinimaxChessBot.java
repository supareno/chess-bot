package com.fcuillandre.chessbot.bot;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.checkers.*;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimax-based chess bot with alpha-beta pruning.
 * <p>
 * Uses a simple material evaluation and searches to a fixed depth. This implementation operates on board copies
 * to avoid mutating the game state.
 * </p>
 *
 * @author fcuillandre
 * @version 1.0
 */
public class MinimaxChessBot implements ChessBot {

    /*
     * Depth is set to 4 plies (2 full moves).
     * Higher depths include latency and performance considerations and the application cannot play.
     */
    private final int maxDepth = 4;

    @Override
    public Move getMove(ChessGame game) {
        ChessColor toMove = ChessColor.BLACK;
        List<Move> legalMoves = generateLegalMovesUsingGame(game, toMove);
        if (legalMoves.isEmpty()) return null;
        int depth = game.isKingInCheck() ? Math.max(1, maxDepth - 1) : maxDepth;
        Move best = null;
        int bestScore = Integer.MIN_VALUE;
        ChessBoard board = game.getBoard();
        for (Move m : legalMoves) {
            ChessBoard copy = copyBoard(board);
            applyMoveOnBoard(copy, m);
            int score = minimax(copy, opposite(toMove), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, game);
            if (score > bestScore) {
                bestScore = score;
                best = m;
            }
        }
        return best;
    }

    private int minimax(ChessBoard board, ChessColor toMove, int depth, int alpha, int beta, ChessGame gameCtx) {
        if (depth == 0) {
            return evaluateBoard(board);
        }
        // Terminal checks based on available moves
        List<Move> moves = generateLegalMovesForColor(board, toMove, gameCtx);
        if (moves.isEmpty()) {
            // No legal moves: check if in check -> checkmate, else stalemate
            boolean inCheck = isKingInCheckForColor(board, toMove, gameCtx);
            if (inCheck) {
                // If side to move is checkmated, it's very bad for that side
                return (toMove == ChessColor.BLACK) ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE - 1;
            } else {
                // Stalemate: neutral outcome
                return 0;
            }
        }
        int value = Integer.MIN_VALUE;
        for (Move move : moves) {
            ChessBoard next = copyBoard(board);
            applyMoveOnBoard(next, move);
            int score = minimax(next, opposite(toMove), depth - 1, alpha, beta, gameCtx);
            value = Math.max(value, score);
            alpha = Math.max(alpha, value);
            if (alpha >= beta) break;
        }
        return value;
    }

    private void applyMoveOnBoard(ChessBoard board, Move move) {
        board.move(move);
        // Handle promotion on board copies used for search
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

    private List<Move> generateLegalMovesForColor(ChessBoard board, ChessColor color, ChessGame gameCtx) {
        List<Move> legal = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getColor() == color) {
                    MoveChecker checker = getChecker(piece.getType());
                    if (checker == null) continue;
                    for (int dx = 0; dx < 8; dx++) {
                        for (int dy = 0; dy < 8; dy++) {
                            if (x == dx && y == dy) continue;
                            Move m;
                            // If a pawn reaches last rank, include promotion in the generated move
                            if (piece.getType() == ChessPieceType.PAWN && dx == 0) {
                                m = new Move(new Coordinate(x, y), new Coordinate(dx, dy), ChessPieceType.QUEEN);
                            } else {
                                m = new Move(new Coordinate(x, y), new Coordinate(dx, dy));
                            }
                            if (checker.isValidMove(piece, m, board, gameCtx)) {
                                // Reject moves that leave own king in check
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

    private boolean isKingInCheckForColor(ChessBoard board, ChessColor color, ChessGame gameCtx) {
        Coordinate kingPos = findKing(board, color);
        if (kingPos == null) return false;
        ChessColor opponent = (color == ChessColor.WHITE) ? ChessColor.BLACK : ChessColor.WHITE;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece p = board.getPieceAt(x, y);
                if (p != null && p.getColor() == opponent) {
                    MoveChecker checker = getChecker(p.getType());
                    if (checker == null) continue;
                    Move attack = new Move(new Coordinate(x, y), kingPos);
                    if (checker.isValidMove(p, attack, board, gameCtx)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Coordinate findKing(ChessBoard board, ChessColor color) {
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

    private MoveChecker getChecker(ChessPieceType type) {
        return switch (type) {
            case PAWN -> new PawnMoveChecker();
            case ROOK -> new RookMoveChecker();
            case KNIGHT -> new KnightMoveChecker();
            case BISHOP -> new BishopMoveChecker();
            case QUEEN -> new QueenMoveChecker();
            case KING -> new KingMoveChecker();
        };
    }

    private ChessBoard copyBoard(ChessBoard original) {
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


    /**
     * Simple material evaluation from Black's perspective: positive means good for Black.
     */
    private int evaluateBoard(ChessBoard board) {
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

    private int pieceValue(ChessPieceType type) {
        return switch (type) {
            case PAWN -> 100;
            case KNIGHT, BISHOP -> 300;
            case ROOK -> 500;
            case QUEEN -> 900;
            case KING -> 20000;
        };
    }

    private ChessColor opposite(ChessColor c) {
        return (c == ChessColor.WHITE) ? ChessColor.BLACK : ChessColor.WHITE;
    }

    private List<Move> generateLegalMovesUsingGame(ChessGame game, ChessColor color) {
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
                            // ensure start square still has the piece (defensive) and move does not leave king in check
                            // (game.isValidMove already checks)
                            moves.add(m);
                        }
                    }
                }
            }
        }
        return moves;
    }
}
