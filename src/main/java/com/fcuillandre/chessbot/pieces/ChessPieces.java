package com.fcuillandre.chessbot.pieces;

/**
 * This class provides static instances of all chess pieces for both colors.
 * It includes pieces for white and black, such as rooks, knights, bishops, queens, kings, and pawns.
 * Each piece is represented by a `ChessPiece` object with its color and type.
 *
 * @author FCuillandre
 * @version 1.0
 */
public final class ChessPieces {

    public static final ChessPiece WHITE_ROOK = new ChessPiece(ChessColor.WHITE, ChessPieceType.ROOK);
    public static final ChessPiece WHITE_KNIGHT = new ChessPiece(ChessColor.WHITE, ChessPieceType.KNIGHT);
    public static final ChessPiece WHITE_BISHOP = new ChessPiece(ChessColor.WHITE, ChessPieceType.BISHOP);
    public static final ChessPiece WHITE_QUEEN = new ChessPiece(ChessColor.WHITE, ChessPieceType.QUEEN);
    public static final ChessPiece WHITE_KING = new ChessPiece(ChessColor.WHITE, ChessPieceType.KING);
    public static final ChessPiece WHITE_PAWN = new ChessPiece(ChessColor.WHITE, ChessPieceType.PAWN);

    public static final ChessPiece BLACK_ROOK = new ChessPiece(ChessColor.BLACK, ChessPieceType.ROOK);
    public static final ChessPiece BLACK_KNIGHT = new ChessPiece(ChessColor.BLACK, ChessPieceType.KNIGHT);
    public static final ChessPiece BLACK_BISHOP = new ChessPiece(ChessColor.BLACK, ChessPieceType.BISHOP);
    public static final ChessPiece BLACK_QUEEN = new ChessPiece(ChessColor.BLACK, ChessPieceType.QUEEN);
    public static final ChessPiece BLACK_KING = new ChessPiece(ChessColor.BLACK, ChessPieceType.KING);
    public static final ChessPiece BLACK_PAWN = new ChessPiece(ChessColor.BLACK, ChessPieceType.PAWN);

}
