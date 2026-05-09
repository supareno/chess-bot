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

/**
 * Possible states of a chess game.
 *
 * @author fcuillandre
 * @since 1.0
 */
public enum GameState {

    PLAYING("In progress"),
    CHECK("Check"),
    CHECKMATE("Checkmate"),
    STALEMATE("Stalemate"),
    DRAW_FIFTY_MOVES("Draw (50 moves)"),
    DRAW_REPETITION("Draw (repetition)"),
    DRAW_INSUFFICIENT_MATERIAL("Draw (insufficient material)"),
    RESIGNED("Resigned");

    private final String description;

    GameState(final String description) {
        this.description = description;
    }

    /** Returns the default English description (used by non-GUI callers and tests). */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the message bundle key for this game state, so the GUI can look
     * up a localized label without creating a dependency on the GUI layer.
     */
    public String getMessageKey() {
        return switch (this) {
            case PLAYING -> "state.playing";
            case CHECK -> "state.check";
            case CHECKMATE -> "state.checkmate";
            case STALEMATE -> "state.stalemate";
            case DRAW_FIFTY_MOVES -> "state.draw.fifty";
            case DRAW_REPETITION -> "state.draw.repetition";
            case DRAW_INSUFFICIENT_MATERIAL -> "state.draw.material";
            case RESIGNED -> "state.resigned";
        };
    }

    public boolean isGameOver() {
        return this != PLAYING && this != CHECK;
    }
}
