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

import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe transposition table for storing chess board evaluations.
 * <p>
 * This class uses a {@link ConcurrentHashMap} to cache board positions (by Zobrist hash)
 * and their associated {@link TranspositionTableEntry} results. It is used to optimize
 * chess engine search algorithms by avoiding redundant calculations for previously
 * evaluated positions.
 * </p>
 * <p>
 * This class is immutable and cannot be extended.
 * </p>
 *
 * @author fcuillandre
 * @version 0.1
 */
public final class TranspositionTable {

    private final ConcurrentHashMap<Long, TranspositionTableEntry> table;

    /**
     * Constructs a new, empty transposition table.
     */
    public TranspositionTable() {
        this.table = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the entry associated with the given Zobrist hash key.
     *
     * @param key the Zobrist hash of the board position
     * @return the corresponding {@link TranspositionTableEntry}, or null if not present
     */
    public TranspositionTableEntry get(long key) {
        return table.get(key);
    }

    /**
     * Stores an entry in the table for the given Zobrist hash key.
     *
     * @param key   the Zobrist hash of the board position
     * @param entry the entry to store
     */
    public void put(long key, TranspositionTableEntry entry) {
        table.put(key, entry);
    }

    /**
     * Removes all entries from the transposition table.
     */
    public void clear() {
        table.clear();
    }
}
