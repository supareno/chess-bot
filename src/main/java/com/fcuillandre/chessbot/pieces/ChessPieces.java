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
package com.fcuillandre.chessbot.pieces;

import lombok.NoArgsConstructor;

/**
 * This class provides static instances of all chess pieces for both colors.
 * It includes pieces for white and black, such as rooks, knights, bishops, queens, kings, and pawns.
 * Each piece is represented by a `ChessPiece` object with its color and type.
 *
 * @author fcuillandre
 * @version 0.1
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
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
