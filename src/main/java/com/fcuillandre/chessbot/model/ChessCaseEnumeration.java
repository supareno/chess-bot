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
package com.fcuillandre.chessbot.model;

import lombok.Getter;

/**
 * Enumeration representing the chess cases on a standard chessboard.
 * Each case is identified by its coordinates (x, y) and can be accessed by its name (e.g., A1, B2).
 * The coordinates are based on the standard chessboard layout, where A1 is at (7, 0) and H8 is at (0, 7).
 *
 * @author fcuillandre
 * @since 0.1
 */
@Getter
public enum ChessCaseEnumeration {

    // A column
    A1(7, 0),
    A2(6, 0),
    A3(5, 0),
    A4(4, 0),
    A5(3, 0),
    A6(2, 0),
    A7(1, 0),
    A8(0, 0),
    // B column
    B1(7, 1),
    B2(6, 1),
    B3(5, 1),
    B4(4, 1),
    B5(3, 1),
    B6(2, 1),
    B7(1, 1),
    B8(0, 1),
    // C column
    C1(7, 2),
    C2(6, 2),
    C3(5, 2),
    C4(4, 2),
    C5(3, 2),
    C6(2, 2),
    C7(1, 2),
    C8(0, 2),
    // D column
    D1(7, 3),
    D2(6, 3),
    D3(5, 3),
    D4(4, 3),
    D5(3, 3),
    D6(2, 3),
    D7(1, 3),
    D8(0, 3),
    // E column
    E1(7, 4),
    E2(6, 4),
    E3(5, 4),
    E4(4, 4),
    E5(3, 4),
    E6(2, 4),
    E7(1, 4),
    E8(0, 4),
    // F column
    F1(7, 5),
    F2(6, 5),
    F3(5, 5),
    F4(4, 5),
    F5(3, 5),
    F6(2, 5),
    F7(1, 5),
    F8(0, 5),
    // G column
    G1(7, 6),
    G2(6, 6),
    G3(5, 6),
    G4(4, 6),
    G5(3, 6),
    G6(2, 6),
    G7(1, 6),
    G8(0, 6),
    // H column
    H1(7, 7),
    H2(6, 7),
    H3(5, 7),
    H4(4, 7),
    H5(3, 7),
    H6(2, 7),
    H7(1, 7),
    H8(0, 7);

    private final Position position;

    /**
     * Constructor for the chess case enumeration.
     *
     * @param row The row index of the case (0-7).
     * @param col The column index of the case (0-7).
     */
    ChessCaseEnumeration(int row, int col) {
        this.position = new Position(row, col);
    }

}
