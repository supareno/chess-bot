package com.fcuillandre.chessbot.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Board class.
 */
class ChessBoardTest {
    
    private ChessBoard board;
    
    @BeforeEach
    void setUp() {
        board = new ChessBoard();
        board.setupInitialPosition();
    }
    
    @Test
    void testInitialPosition() {
        // Check the rooks
        assertEquals(ChessPieceType.ROOK, board.getPieceAt(new Position(0, 0)).getType());
        assertEquals(ChessPieceType.ROOK, board.getPieceAt(new Position(0, 7)).getType());
        assertEquals(ChessPieceType.ROOK, board.getPieceAt(new Position(7, 0)).getType());
        assertEquals(ChessPieceType.ROOK, board.getPieceAt(new Position(7, 7)).getType());

        // Check the kings
        assertEquals(ChessPieceType.KING, board.getPieceAt(new Position(0, 4)).getType());
        assertEquals(ChessPieceType.KING, board.getPieceAt(new Position(7, 4)).getType());
        
        // Check the pawns
        for (int col = 0; col < 8; col++) {
            assertEquals(ChessPieceType.PAWN, board.getPieceAt(new Position(1, col)).getType());
            assertEquals(ChessColor.BLACK, board.getPieceAt(new Position(1, col)).getColor());
            assertEquals(ChessPieceType.PAWN, board.getPieceAt(new Position(6, col)).getType());
            assertEquals(ChessColor.WHITE, board.getPieceAt(new Position(6, col)).getColor());
        }
        
        // Check empty squares
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                assertNull(board.getPieceAt(new Position(row, col)));
            }
        }
    }
    
    @Test
    void testFindKing() {
        Position whiteKing = board.findKing(ChessColor.WHITE);
        Position blackKing = board.findKing(ChessColor.BLACK);
        
        assertEquals(new Position(7, 4), whiteKing);
        assertEquals(new Position(0, 4), blackKing);
    }
    
    @Test
    void testIsKingInCheck() {
        // Position initiale: pas d'échec
        assertFalse(board.isKingInCheck(ChessColor.WHITE));
        assertFalse(board.isKingInCheck(ChessColor.BLACK));
    }
    
    @Test
    void testLegalMovesCount() {
        // Initial position: 20 legal moves for white
        var moves = board.getAllLegalMoves(ChessColor.WHITE);
        assertEquals(20, moves.size());
    }
    
    @Test
    void testCopyBoard() {
        ChessBoard copy = board.copy();
        
        // Check that the copy is independent
        assertNotSame(board, copy);
        
        // Check that the pieces are identical
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                ChessPiece original = board.getPieceAt(pos);
                ChessPiece copied = copy.getPieceAt(pos);
                
                if (original == null) {
                    assertNull(copied);
                } else {
                    assertNotNull(copied);
                    assertEquals(original.getType(), copied.getType());
                    assertEquals(original.getColor(), copied.getColor());
                    assertNotSame(original, copied);
                }
            }
        }
    }
}
