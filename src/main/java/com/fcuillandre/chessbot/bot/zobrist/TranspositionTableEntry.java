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
 * @version 1.0
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