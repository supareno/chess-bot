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
package com.fcuillandre.chessbot.ui;

import lombok.NoArgsConstructor;

import java.awt.*;

/**
 * ChessAppFonts is a utility class that defines commonly used fonts in the chess application UI.
 *
 * @author fcuillandre
 * @version 0.2
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ChessAppFonts {

    /**
     * Arial font, bold, size 12.
     */
    public static final Font ARIAL_FONT_BOLD_12 = new Font("Arial", Font.BOLD, 12);

    /**
     * Arial font, bold, size 18.
     */
    public static final Font ARIAL_FONT_BOLD_16 = new Font("Arial", Font.BOLD, 16);

    /**
     * Segoe UI Symbol font, bold, size 30.
     * This font is often used for displaying chess symbols.
     */
    public static final Font SEGOE_FONT_BOLD_30 = new Font("Segoe UI Symbol", Font.BOLD, 30);

    /**
     * Segoe UI Symbol font, bold, size 16.
     * This font is often used for displaying other symbols.
     */
    public static final Font SEGOE_FONT_BOLD_16 = new Font("Segoe UI Symbol", Font.BOLD, 16);

    /**
     * Monospaced font, plain, size 14.
     * This font is used for the move list panel.
     */
    public static final Font MONOSPACED_FONT_BOLD_14 = new Font("Monospaced", Font.PLAIN, 14);
}
