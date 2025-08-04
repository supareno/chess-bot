package com.fcuillandre.chessbot.board;

import com.fcuillandre.chessbot.game.Coordinate;
import lombok.Getter;

/**
 * Enumeration representing the chess cases on a standard chessboard.
 * Each case is identified by its coordinates (x, y) and can be accessed by its name (e.g., A1, B2).
 * The coordinates are based on the standard chessboard layout, where A1 is at (0, 7) and H8 is at (7, 0).
 *
 * @author FCuillandre
 * @version 1.0
 */
@Getter
public enum ChessCaseEnumeration {

    // A
    A1(0, 7),
    A2(1, 7),
    A3(2, 7),
    A4(3, 7),
    A5(4, 7),
    A6(5, 7),
    A7(6, 7),
    A8(7, 7),

    // B
    B1(0, 6),
    B2(1, 6),
    B3(2, 6),
    B4(3, 6),
    B5(4, 6),
    B6(5, 6),
    B7(6, 6),
    B8(7, 6),

    // C
    C1(0, 5),
    C2(1, 5),
    C3(2, 5),
    C4(3, 5),
    C5(4, 5),
    C6(5, 5),
    C7(6, 5),
    C8(7, 5),

    // D
    D1(0, 4),
    D2(1, 4),
    D3(2, 4),
    D4(3, 4),
    D5(4, 4),
    D6(5, 4),
    D7(6, 4),
    D8(7, 4),

    //
    E1(0, 3),
    E2(1, 3),
    E3(2, 3),
    E4(3, 3),
    E5(4, 3),
    E6(5, 3),
    E7(6, 3),
    E8(7, 3),

    // F
    F1(0, 2),
    F2(1, 2),
    F3(2, 2),
    F4(3, 2),
    F5(4, 2),
    F6(5, 2),
    F7(6, 2),
    F8(7, 2),

    // G
    G1(0, 1),
    G2(1, 1),
    G3(2, 1),
    G4(3, 1),
    G5(4, 1),
    G6(5, 1),
    G7(6, 1),
    G8(7, 1),

    // H
    H1(0, 0),
    H2(1, 0),
    H3(2, 0),
    H4(3, 0),
    H5(4, 0),
    H6(5, 0),
    H7(6, 0),
    H8(7, 0);

    private final Coordinate coordinate;

    ChessCaseEnumeration(int x, int y) {
        this.coordinate = new Coordinate(x, y);
    }


    /**
     * Returns the chess case name in the format "A1", "B2", etc.
     * @return the chess case name
     */
    public static ChessCaseEnumeration fromString(String caseName) {
        try {
            return ChessCaseEnumeration.valueOf(caseName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid chess case name: " + caseName, e);
        }
    }
}
