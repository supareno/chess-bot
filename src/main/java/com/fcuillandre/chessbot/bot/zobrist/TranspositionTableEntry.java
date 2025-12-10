package com.fcuillandre.chessbot.bot.zobrist;

import lombok.Getter;

@Getter
public class TranspositionTableEntry {


    private final Flag flag;
    private final int score;
    private final int depth;

    public TranspositionTableEntry(int depth, int score, Flag flag) {

        this.flag = flag;
        this.score = score;
        this.depth = depth;
    }


    public enum Flag {

        UPPERBOUND,
        LOWERBOUND,
        EXACT
        }

}