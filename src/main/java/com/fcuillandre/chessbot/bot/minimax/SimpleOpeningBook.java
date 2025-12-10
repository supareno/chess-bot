package com.fcuillandre.chessbot.bot.minimax;

import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.MovedPiece;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.utils.ChessUtils;

import java.util.*;

public class SimpleOpeningBook {
    // Map from move history string to a list of book moves (as simple move strings)
    private static final Map<String, List<String>> openingBook = new HashMap<>();
    static {
        // First opening:
        // 1. e4
        openingBook.put("E2E4", Arrays.asList("E7E5", "C7C5", "E7E6", "C7C6", "D7D6", "D7D5"));
        // 1. e4 e5 2. Nf3
        openingBook.put("E2E4 E7E5 G1F3", Arrays.asList("B8C6", "D7D6", "G8F6", "D7D5", "G7G6", "E7E6"));
        // 1. e4 e5 2. Nf3 Nc6 3. Bc4
        openingBook.put("E2E4 E7E5 G1F3 B8C6 F1C4", Arrays.asList("F8C5", "G8F6", "F8E7", "D7D6"));
    }

    public static Move getBookMove(ChessGame game) {
        String key = getMoveHistoryKey(game);
        List<String> moves = openingBook.get(key);
        if (moves == null || moves.isEmpty()) return null;
        // Pick the first book move (could randomize)
        String moveStr = moves.get(0);
        return parseMove(moveStr);
    }

    private static String getMoveHistoryKey(ChessGame game) {
        List<MovedPiece> history = game.getMoveHistory();
        StringBuilder sb = new StringBuilder();
        for (MovedPiece mp : history) {
            sb.append(toSimpleMove(mp)).append(" ");
        }
        return sb.toString().trim();
    }

    private static String toSimpleMove(MovedPiece movedPiece) {
        return toSimpleMove(new Move(movedPiece.getStart(), movedPiece.getEnd()));
    }

    private static String toSimpleMove(Move move) {
        char fromFile = (char) ('A' + move.getStart().getY());
        char fromRank = (char) ('1' + move.getStart().getX());
        char toFile = (char) ('A' + move.getEnd().getY());
        char toRank = (char) ('1' + move.getEnd().getX());
        return "" + fromFile + fromRank + toFile + toRank;
    }

    private static Move parseMove(String moveStr) {
        int fromY = moveStr.charAt(0) - 'A';
        int fromX = moveStr.charAt(1) - '1';
        int toY = moveStr.charAt(2) - 'A';
        int toX = moveStr.charAt(3) - '1';
        return new Move(new Coordinate(fromX, fromY), new Coordinate(toX, toY));
    }
}

