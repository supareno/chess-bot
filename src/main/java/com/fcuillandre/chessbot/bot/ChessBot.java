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
package com.fcuillandre.chessbot.bot;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;

/**
 * Interface for a chess bot that can determine moves based on the game state.
 *
 * @author fcuillandre
 * @version 0.1
 */
public interface ChessBot {

    /**
     * Get the next move for the given game state.
     * @param game The current chess game state.
     * @return The move chosen by the bot.
     */
    Move getMove(ChessGame game);
}
