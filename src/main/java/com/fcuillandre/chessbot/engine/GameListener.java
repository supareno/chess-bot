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
package com.fcuillandre.chessbot.engine;

import com.fcuillandre.chessbot.model.ChessBoard;
import com.fcuillandre.chessbot.model.ChessColor;
import com.fcuillandre.chessbot.model.Move;

/**
 * Interface for listening to game events.
 * This interface defines methods that are called when certain events occur in the game, such as changes to the
 * chessboard, changes in the game state, and when a move is made. Implementing this interface allows you to respond
 * to these events, for example by updating a user interface or logging game activity.
 *
 * @author fcuillandre
 * @since 1.0
 */
public interface GameListener {

    /**
     * Called when the chessboard changes.
     */
    void onBoardChanged(ChessBoard board);

    /**
     * Called when the game state changes.
     */
    void onGameStateChanged(GameState state, ChessColor currentPlayer);

    /**
     * Called when a move is made.
     */
    void onMoveMade(Move move);
}
