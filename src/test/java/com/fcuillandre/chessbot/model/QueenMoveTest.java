package com.fcuillandre.chessbot.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for queen movement rules.
 *
 * @author fcuillandre
 * @since 1.0
 */
class QueenMoveTest {
    private ChessBoard board;
    @BeforeEach
    void setUp() {
        board = new ChessBoard();
    }
    @Test
    void queen_canMoveInAllDirections() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.D4.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.WHITE));
        Position queenPos = ChessCaseEnumeration.D4.getPosition();
        List<Move> queenMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(queenPos)).toList();
        assertEquals(27, queenMoves.size());
    }
    @Test
    void queen_blockedByOwnPiece() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.D1.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.D3.getPosition(), new ChessPiece(ChessPieceType.PAWN, ChessColor.WHITE));
        Position queenPos = ChessCaseEnumeration.D1.getPosition();
        List<Move> queenMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(queenPos)).toList();
        assertFalse(queenMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.D3.getPosition())));
    }
    @Test
    void queen_canCaptureEnemyPiece() {
        board.setPieceAt(ChessCaseEnumeration.E1.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.E8.getPosition(), new ChessPiece(ChessPieceType.KING, ChessColor.BLACK));
        board.setPieceAt(ChessCaseEnumeration.D1.getPosition(), new ChessPiece(ChessPieceType.QUEEN, ChessColor.WHITE));
        board.setPieceAt(ChessCaseEnumeration.D5.getPosition(), new ChessPiece(ChessPieceType.ROOK, ChessColor.BLACK));
        Position queenPos = ChessCaseEnumeration.D1.getPosition();
        List<Move> queenMoves = board.getAllLegalMoves(ChessColor.WHITE).stream()
                .filter(m -> m.getFrom().equals(queenPos)).toList();
        assertTrue(queenMoves.stream().anyMatch(m -> m.getTo().equals(ChessCaseEnumeration.D5.getPosition()) && m.isCapture()));
    }
}

