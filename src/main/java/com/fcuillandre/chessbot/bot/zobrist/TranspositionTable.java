package com.fcuillandre.chessbot.bot.zobrist;

import java.util.concurrent.ConcurrentHashMap;

public class TranspositionTable {

    private final ConcurrentHashMap<Long, TranspositionTableEntry> table;

    public TranspositionTable() {
        this.table = new ConcurrentHashMap<>();
    }

    public TranspositionTableEntry get(long key) {
        return table.get(key);
    }

    public void put(long key, TranspositionTableEntry entry) {
        table.put(key, entry);
    }

    public void clear() {
        table.clear();
    }
}

