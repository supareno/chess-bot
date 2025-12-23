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
package com.fcuillandre.chessbot.bot.zobrist;

import lombok.Getter;

/**
 * Represents an entry in the transposition table for chess engine search algorithms.
 * <p>
 * Each entry stores the evaluation score, search depth, and a flag indicating the type of bound.
 * This is used to optimize search by caching previously evaluated positions.
 * </p>
 *
 * @author fcuillandre
 * @version 0.1
 */
@Getter
public final class TranspositionTableEntry {

    private final Flag flag;
    private final int score;
    private final int depth;

    /**
     * Constructs a new transposition table entry.
     *
     * @param depth the search depth
     * @param score the evaluation score
     * @param flag  the type of bound for the score
     */
    public TranspositionTableEntry(int depth, int score, Flag flag) {
        this.flag = flag;
        this.score = score;
        this.depth = depth;
    }


    /**
     * Enum representing the type of bound for a transposition table entry.
     * <ul>
     *     <li>UPPERBOUND: The score is an upper bound.</li>
     *     <li>LOWERBOUND: The score is a lower bound.</li>
     *     <li>EXACT: The score is exact.</li>
     * </ul>
     */
    public enum Flag {

        UPPERBOUND,
        LOWERBOUND,
        EXACT
    }

}