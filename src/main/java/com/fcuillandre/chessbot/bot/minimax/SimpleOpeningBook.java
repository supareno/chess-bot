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

package com.fcuillandre.chessbot.bot.minimax;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.MovedPiece;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A simple opening book implementation using a hardcoded map of move sequences.
 * <p>
 * The keys are strings representing the move history, and the values are lists of possible book moves
 * represented as simple move strings (e.g., "E2E4").
 * <br>
 * This class provides a method to get a book move based on the current game state.
 * <br>
 * Example usage:
 * <pre>
 *     Move bookMove = SimpleOpeningBook.getBookMove(game);
 *     if (bookMove != null) {
 *         // Play the book move
 *     }
 * </pre>
 * <br>
 * The opening book includes common chess openings such as Sicilian Defense, French Defense, and Ruy Lopez.
 * </p>
 *
 * <h2>Thread Safety</h2>
 * This class is thread-safe for read operations as the opening book is immutable after static initialization.
 *
 * <h2>Extending the Book</h2>
 * To add new openings, add entries to the static block using the move history key and a list of book moves.
 *
 * @author fcuillandre
 * @version 0.1
 */
public final class SimpleOpeningBook {

    /**
     * Map from move history string to a list of book moves (as simple move strings)
     * Example key: "E2E4 E7E5 G1F3"
     * Example value: ["B8C6", "D7D6", ...]
     */
    private static final Map<String, List<String>> openingBook = new HashMap<>();

    static {
        // First opening:
        // 1. e4
        openingBook.put("E2E4", Arrays.asList("E7E5", "C7C5", "E7E6", "C7C6", "D7D6", "D7D5"));
        // 1. e4 e5 2. Nf3
        openingBook.put("E2E4 E7E5 G1F3", Arrays.asList("B8C6", "D7D6", "G8F6", "D7D5", "G7G6"));
        // Italian Game: e4 e5 2. Nf3 Nc6 3. Bc4
        openingBook.put("E2E4 E7E5 G1F3 B8C6 F1C4", Arrays.asList("F8C5", "G8F6", "F8E7", "D7D6"));
        // Ruy Lopez: 1. e4 e5 2. Nf3 Nc6 3. Bb5
        openingBook.put("E2E4 E7E5 G1F3 B8C6 F1B5", Arrays.asList("A7A6", "G8F6", "F8C5", "F8E7"));
        // Ruy Lopez exchange variation: 1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Bxc6
        openingBook.put("E2E4 E7E5 G1F3 B8C6 F1B5 A7A6 B5C6", Arrays.asList("D7C6", "B7C6"));
        // Sicilian Defense
        //  1. e4 c5
        openingBook.put("E2E4 C7C5 D2D4", Arrays.asList("D7D5", "C7D5", "E7E6", "G8F6"));
        //  1. e4 c5 2. Nf3
        openingBook.put("E2E4 C7C5 G1F3", Arrays.asList("D7D6", "E7E6", "B8C6", "G8F6"));

        // French Defense
        //  1. e4 e6 2. d4
        openingBook.put("E2E4 E7E6 D2D4", Arrays.asList("D7D5"));
        //  1. e4 e6 2. d4 d5
        openingBook.put("E2E4 E7E6 D2D4 D7D5 B1C3", Arrays.asList("G8F6"));

        // Vienna Game: 1. e4 e5 2. Nc3
        openingBook.put("E2E4 E7E5 B1C3", Arrays.asList("B8C6", "G8F6", "F1C4"));
        // Scottish Game: 1. e4 e5 2. Nf3 Nc6 3. d4
        openingBook.put("E2E4 E7E5 G1F3 B8C6 D2D4", Arrays.asList("E5D4", "F6E7", "D7D6"));
        // Caro-Kann Defense: 1. e4 c6
        openingBook.put("E2E4 C7C6 D2D4", Arrays.asList("D7D5"));

        // First opening:
        // 1. d4
        openingBook.put("D2D4", Arrays.asList("D7D5", "E7E6", "C7C5", "G8F6"));
        // Queen's Gambit: 1. d4 d5 2. c4
        openingBook.put("D2D4 D7D5 C2C4", Arrays.asList("D5C4", "E7E6", "C7C6"));
        // Nimzo-Indian Defense: 1. d4 Nf6 2. c4 e6 3. Nc3
        openingBook.put("D2D4 G8F6 C2C4 E7E6 B1C3", Arrays.asList("F8B4"));
    }


    /**
     * Returns a book move for the given game state, or null if no book move is found.
     * <p>
     * The move returned is a random choice among the possible book moves for the current position.
     * </p>
     *
     * @param game the current chess game
     * @return a book move as a {@link Move}, or null if not found
     */
    public static Move getBookMove(ChessGame game) {
        String key = getMoveHistoryKey(game);
        List<String> moves = openingBook.get(key);
        if (moves == null || moves.isEmpty()) return null;
        String moveStr = moves.get(new Random().nextInt(moves.size()));
        return parseMove(moveStr);
    }

    /**
     * Builds the move history key for the current game state.
     *
     * @param game the current chess game
     * @return a string representing the move history
     */
    private static String getMoveHistoryKey(ChessGame game) {
        List<MovedPiece> history = game.getMoveHistory();
        return history.stream()
                .map(SimpleOpeningBook::toSimpleMove)
                .collect(Collectors.joining(" "));
    }

    /**
     * Converts a moved piece to a simple move string (e.g., "E2E4").
     *
     * @param movedPiece the moved piece
     * @return the move string
     */
    private static String toSimpleMove(MovedPiece movedPiece) {
        return toSimpleMove(new Move(movedPiece.getStart(), movedPiece.getEnd()));
    }

    /**
     * Converts a move to a simple move string (e.g., "E2E4").
     *
     * @param move the move
     * @return the move string
     */
    private static String toSimpleMove(Move move) {
        char fromFile = (char) ('A' + move.getStart().getY());
        char fromRank = (char) ('1' + move.getStart().getX());
        char toFile = (char) ('A' + move.getEnd().getY());
        char toRank = (char) ('1' + move.getEnd().getX());
        return "" + fromFile + fromRank + toFile + toRank;
    }

    /**
     * Parses a move string (e.g., "E2E4") into a {@link Move} object.
     *
     * @param moveStr the move string
     * @return the corresponding {@link Move}
     */
    private static Move parseMove(String moveStr) {
        int fromY = moveStr.charAt(0) - 'A';
        int fromX = moveStr.charAt(1) - '1';
        int toY = moveStr.charAt(2) - 'A';
        int toX = moveStr.charAt(3) - '1';
        return new Move(new Coordinate(fromX, fromY), new Coordinate(toX, toY));
    }
}
