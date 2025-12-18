package com.fcuillandre.chessbot.bot.zobrist;

import com.fcuillandre.chessbot.utils.ChessUtils;

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
 * @version 1.0
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
        ChessUtils.log("Zobrist table size: " + table.size());
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
