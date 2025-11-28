package com.fcuillandre.chessbot.bot;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;

/**
 * Interface for a chess bot that can determine moves based on the game state.
 *
 * @author fcuillandre
 * @version 1.0
 */
public interface ChessBot {

    /**
     * Get the next move for the given game state.
     * @param game The current chess game state.
     * @return The move chosen by the bot.
     */
    Move getMove(ChessGame game);
}
