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
public final class ChessGame {

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
        boolean valid = getMoveChecker(piece).isValidMove(piece, move, board, this);
        // Si le roi est en échec, n'autoriser que les coups qui le sortent de l'échec
        if (isKingInCheck()) {
            if (!doesMoveResolveCheck(move)) {
                return false;
            }
        } else {
            // Ne jamais autoriser un coup qui met son propre roi en échec
            if (!doesMoveResolveCheck(move)) {
                return false;
            }
        }
        return valid;
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
            // Pawn promotion detection (UI must call promotePawn after move if needed)
            // No automatic promotion here; UI will handle it after move
            lastMove = move;
            whiteTurn = !whiteTurn; // Switch turn
        } else {
            ChessUtils.log("Argh, move " + board.getCaseAt(startX, startY) + " " + board.getCaseAt(endX, endY) + " is NOT a valid move");
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

    /**
     * Undoes the last move made on the chess board.
     * <p>This method restores the board to its previous state before the last move was made.</p>
     */
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

    /**
     * Clears the move history.
     * <p>This method removes all recorded moves from the move history.</p>
     */
    public void clearMoveHistory() {
        moveHistory.clear();
    }

    /**
     * Checks if the king is in check.
     * <p>This method checks if the current player's king is under threat of capture.</p>
     *
     * @return true if the king is in check, false otherwise
     */
    public boolean isKingInCheck() {
        ChessColor currentColor = whiteTurn ? ChessColor.WHITE : ChessColor.BLACK;
        FoundPiece king = findKing(currentColor);

        // Check if any opposing pieces can attack the king's position
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getColor() != currentColor && king != null) {
                    Move potentialAttack = new Move(new Coordinate(x, y), new Coordinate(king.getCoordinate().getX(), king.getCoordinate().getY()));
                    if (getMoveChecker(piece).isValidMove(piece, potentialAttack, board, this)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Finds the king piece for the given color.
     *
     * @param color The color of the king to find
     * @return The king piece, or null if not found
     */
    private FoundPiece findKing(ChessColor color) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getType() == ChessPieceType.KING && piece.getColor() == color) {
                    return new FoundPiece(piece, new Coordinate(x, y));
                }
            }
        }
        return null;
    }

    /**
     * Checks if a move resolves a check situation.
     * @param move The move to check
     * @return true if the move resolves the check, false otherwise
     */
    private boolean doesMoveResolveCheck(Move move) {
        // Save board
        ChessPiece[][] backup = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece p = board.getPieceAt(i, j);
                backup[i][j] = p == null ? null : new ChessPiece(p.getColor(), p.getType());
            }
        }
        // Play the move
        board.move(move);
        boolean stillInCheck = isKingInCheck();
        // Cancel the move
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.setPieceAt(i, j, backup[i][j]);
            }
        }
        return !stillInCheck;
    }

    /**
     * Checks if the current player is in checkmate.
     * @return true if the current player is in checkmate, false otherwise
     */
    public boolean isCheckmate() {
        if (!isKingInCheck()) {
            return false;
        }
        ChessColor currentColor = whiteTurn ? ChessColor.WHITE : ChessColor.BLACK;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getColor() == currentColor) {
                    for (int dx = 0; dx < 8; dx++) {
                        for (int dy = 0; dy < 8; dy++) {
                            if (x == dx && y == dy) continue;
                            Move move = new Move(new Coordinate(x, y), new Coordinate(dx, dy));
                            if (isValidMove(move)) {
                                return false; // At least one legal move exists
                            }
                        }
                    }
                }
            }
        }
        return true; // No legal moves and king is in check
    }

    /**
     * Checks if the current player is in stalemate.
     * Stalemate occurs when the player is not in check and has no legal moves.
     *
     * @return true if stalemate, false otherwise
     */
    public boolean isStalemate() {
        if (isKingInCheck()) {
            return false;
        }
        ChessColor currentColor = whiteTurn ? ChessColor.WHITE : ChessColor.BLACK;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board.getPieceAt(x, y);
                if (piece != null && piece.getColor() == currentColor) {
                    for (int toX = 0; toX < 8; toX++) {
                        for (int toY = 0; toY < 8; toY++) {
                            if (x == toX && y == toY) continue;
                            Move move = new Move(new Coordinate(x, y), new Coordinate(toX, toY));
                            if (isValidMove(move)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Promotes a pawn at the given coordinate to the specified piece type.
     * @param coord The coordinate of the pawn to promote.
     * @param newType The type to promote to (must not be KING or PAWN).
     */
    public void promotePawn(Coordinate coord, ChessPieceType newType) {
        ChessPiece pawn = board.getPieceAt(coord.getX(), coord.getY());
        if (pawn == null || pawn.getType() != ChessPieceType.PAWN) return;
        if (newType == ChessPieceType.KING || newType == ChessPieceType.PAWN) return;
        board.setPieceAt(coord.getX(), coord.getY(), new ChessPiece(pawn.getColor(), newType));
    }
}