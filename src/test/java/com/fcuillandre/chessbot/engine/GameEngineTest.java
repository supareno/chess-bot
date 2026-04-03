package com.fcuillandre.chessbot.engine;

import com.fcuillandre.chessbot.model.*;
import com.fcuillandre.chessbot.model.ChessColor;
import com.fcuillandre.chessbot.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GameEngine unit tests covering basic game flow, move execution, and game state transitions.
 */
class GameEngineTest {
    
    private GameEngine engine;
    
    @BeforeEach
    void setUp() {
        engine = new GameEngine();
        engine.newGame();
    }
    
    @Test
    void testNewGame() {
        assertEquals(ChessColor.WHITE, engine.getCurrentPlayer());
        assertEquals(GameState.PLAYING, engine.getGameState());
        assertTrue(engine.getMoveHistory().isEmpty());
    }
    
    @Test
    void testMakeValidMove() {
        // e2-e4
        boolean success = engine.makeMove(ChessCaseEnumeration.E2, ChessCaseEnumeration.E4);
        
        assertTrue(success);
        assertEquals(ChessColor.BLACK, engine.getCurrentPlayer());
        assertEquals(1, engine.getMoveHistory().size());
    }
    
    @Test
    void testMakeInvalidMove() {
        // Try to move a black piece when it's white's turn
        boolean success = engine.makeMove(ChessCaseEnumeration.E7, ChessCaseEnumeration.E5);
        
        assertFalse(success);
        assertEquals(ChessColor.WHITE, engine.getCurrentPlayer());
    }
    
    @Test
    void testScholarsMateFourMoves() {
        // 1. e4 e5
        assertTrue(engine.makeMove(ChessCaseEnumeration.E2, ChessCaseEnumeration.E4));
        assertTrue(engine.makeMove(ChessCaseEnumeration.E7, ChessCaseEnumeration.E5));
        
        // 2. Qh5 Nc6
        assertTrue(engine.makeMove(ChessCaseEnumeration.D1, ChessCaseEnumeration.H5));
        assertTrue(engine.makeMove(ChessCaseEnumeration.B8, ChessCaseEnumeration.C6));
        
        // 3. Bc4 Nf6
        assertTrue(engine.makeMove(ChessCaseEnumeration.F1, ChessCaseEnumeration.C4));
        assertTrue(engine.makeMove(ChessCaseEnumeration.G8, ChessCaseEnumeration.F6));
        
        // 4. Qxf7# (Mat du berger)
        assertTrue(engine.makeMove(ChessCaseEnumeration.H5, ChessCaseEnumeration.F7));
        
        assertEquals(GameState.CHECKMATE, engine.getGameState());
        assertEquals(ChessColor.WHITE, engine.getWinner());
    }
    
    @Test
    void testResign() {
        engine.resign();
        
        assertEquals(GameState.RESIGNED, engine.getGameState());
        assertEquals(ChessColor.BLACK, engine.getWinner()); // White resigns
    }
    
    @Test
    void testGetLegalMovesFromPawn() {
        Position pawnPos = new Position(6, 4); // Pion e2
        var moves = engine.getLegalMovesFrom(pawnPos);
        
        // Pawn can advance one or two squares
        assertEquals(2, moves.size());
    }
    
    @Test
    void testGetLegalMovesFromKnight() {
        Position knightPos = new Position(7, 1); // Cavalier b1
        var moves = engine.getLegalMovesFrom(knightPos);

        assertEquals(2, moves.size());
    }
}
