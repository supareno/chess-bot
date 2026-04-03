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
package com.fcuillandre.chessbot.gui;

import com.fcuillandre.chessbot.engine.GameEngine;
import com.fcuillandre.chessbot.engine.GameState;
import com.fcuillandre.chessbot.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import static com.fcuillandre.chessbot.gui.ChessAppFontConstants.ARIAL_FONT_BOLD_12;
import static com.fcuillandre.chessbot.gui.ChessAppFontConstants.SEGOE_UI_SYMBOL_FONT_PLAIN_56;

/**
 * Panel representing the chessboard.
 * This class is responsible for rendering the chessboard, pieces, and highlights for legal moves,
 * last move, and check status. It also handles mouse interactions for selecting pieces and making moves.
 *
 * <p>The board can be flipped to display from Black's perspective. Squares are rendered at
 * {@value #SQUARE_SIZE}px each, for a total board size of 560x560 pixels.</p>
 *
 * @author fcuillandre
 * @since 1.0
 */
public class BoardPanel extends JPanel implements MouseListener {

    /**
     * Size in pixels of a single board square.
     */
    private static final int SQUARE_SIZE = 70;

    /**
     * Background color for light squares.
     */
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);

    /**
     * Background color for dark squares.
     */
    private static final Color DARK_SQUARE = new Color(181, 136, 99);

    /**
     * Overlay color used to highlight squares that are legal move destinations.
     */
    private static final Color HIGHLIGHT_COLOR = new Color(186, 202, 68, 180);

    /**
     * Overlay color used to highlight the currently selected square.
     */
    private static final Color SELECTED_COLOR = new Color(246, 246, 105, 200);

    /**
     * Overlay color used to highlight the from/to squares of the last move played.
     */
    private static final Color LAST_MOVE_COLOR = new Color(205, 210, 106, 150);

    /**
     * Overlay color used to highlight the king's square when it is in check.
     */
    private static final Color CHECK_COLOR = new Color(255, 0, 0, 100);

    private final GameEngine gameEngine;
    private final ChessGUI parentGUI;
    private Position selectedPosition;
    private List<Move> legalMovesForSelected;
    private boolean boardFlipped;

    /**
     * Constructs a new {@code BoardPanel}.
     *
     * @param gameEngine the game engine providing board state, legal moves and game rules
     * @param parentGUI  the parent {@link ChessGUI} used to delegate promotion choices
     */
    public BoardPanel(GameEngine gameEngine, ChessGUI parentGUI) {
        this.gameEngine = gameEngine;
        this.parentGUI = parentGUI;
        this.selectedPosition = null;
        this.legalMovesForSelected = null;
        this.boardFlipped = false;

        setPreferredSize(new Dimension(SQUARE_SIZE * 8, SQUARE_SIZE * 8));
        addMouseListener(this);
    }

    /**
     * Returns whether the board is currently displayed from Black's perspective.
     *
     * @return {@code true} if the board is flipped (Black at the bottom), {@code false} otherwise
     */
    public boolean isBoardFlipped() {
        return boardFlipped;
    }

    /**
     * Sets the board orientation and repaints the panel.
     *
     * @param flipped {@code true} to display the board from Black's perspective,
     *                {@code false} for the default White-at-bottom orientation
     */
    public void setBoardFlipped(boolean flipped) {
        this.boardFlipped = flipped;
        repaint();
    }

    /**
     * Clears the current piece selection and its associated legal-move highlights,
     * then repaints the panel.
     */
    public void clearSelection() {
        selectedPosition = null;
        legalMovesForSelected = null;
        repaint();
    }

    /**
     * Paints the board by delegating to the individual draw helpers in the correct Z-order:
     * board squares → last-move highlight → check highlight → selected-square highlight →
     * legal-move dots → pieces → coordinate labels.
     *
     * @param g the {@link Graphics} context provided by Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawBoard(g2d);
        drawLastMove(g2d);
        drawCheck(g2d);
        drawSelectedSquare(g2d);
        drawLegalMoves(g2d);
        drawPieces(g2d);
        drawCoordinates(g2d);
    }

    /**
     * Draws the 64 alternating light and dark squares of the chessboard,
     * respecting the current board orientation.
     *
     * @param g the {@link Graphics2D} context to draw on
     */
    private void drawBoard(Graphics2D g) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int displayRow = boardFlipped ? 7 - row : row;
                int displayCol = boardFlipped ? 7 - col : col;

                boolean isLight = (row + col) % 2 == 0;
                g.setColor(isLight ? LIGHT_SQUARE : DARK_SQUARE);
                g.fillRect(displayCol * SQUARE_SIZE, displayRow * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    /**
     * Highlights the from- and to-squares of the last move played, if any.
     *
     * @param g the {@link Graphics2D} context to draw on
     */
    private void drawLastMove(Graphics2D g) {
        Move lastMove = gameEngine.getLastMove();
        if (lastMove != null) {
            g.setColor(LAST_MOVE_COLOR);
            highlightSquare(g, lastMove.getFrom());
            highlightSquare(g, lastMove.getTo());
        }
    }

    /**
     * Highlights the current player's king square in red when the game state is
     * {@link GameState#CHECK} or {@link GameState#CHECKMATE}.
     *
     * @param g the {@link Graphics2D} context to draw on
     */
    private void drawCheck(Graphics2D g) {
        if (gameEngine.getGameState() == GameState.CHECK ||
                gameEngine.getGameState() == GameState.CHECKMATE) {
            Position kingPos = gameEngine.getBoard().findKing(gameEngine.getCurrentPlayer());
            if (kingPos != null) {
                g.setColor(CHECK_COLOR);
                highlightSquare(g, kingPos);
            }
        }
    }

    /**
     * Highlights the currently selected square, if any.
     *
     * @param g the {@link Graphics2D} context to draw on
     */
    private void drawSelectedSquare(Graphics2D g) {
        if (selectedPosition != null) {
            g.setColor(SELECTED_COLOR);
            highlightSquare(g, selectedPosition);
        }
    }

    /**
     * Draws legal-move indicators for the selected piece:
     * <ul>
     *   <li>A large circle for capture destinations.</li>
     *   <li>A small dot for quiet (non-capture) destinations.</li>
     * </ul>
     *
     * @param g the {@link Graphics2D} context to draw on
     */
    private void drawLegalMoves(Graphics2D g) {
        if (legalMovesForSelected != null) {
            g.setColor(HIGHLIGHT_COLOR);
            for (Move move : legalMovesForSelected) {
                int displayRow = boardFlipped ? 7 - move.getTo().row() : move.getTo().row();
                int displayCol = boardFlipped ? 7 - move.getTo().col() : move.getTo().col();

                int centerX = displayCol * SQUARE_SIZE + SQUARE_SIZE / 2;
                int centerY = displayRow * SQUARE_SIZE + SQUARE_SIZE / 2;

                if (move.isCapture()) {
                    // Circle for capture moves
                    g.fillOval(centerX - SQUARE_SIZE / 3, centerY - SQUARE_SIZE / 3,
                            SQUARE_SIZE * 2 / 3, SQUARE_SIZE * 2 / 3);
                } else {
                    // Points for quiet moves
                    g.fillOval(centerX - SQUARE_SIZE / 6, centerY - SQUARE_SIZE / 6,
                            SQUARE_SIZE / 3, SQUARE_SIZE / 3);
                }
            }
        }
    }

    /**
     * Fills the square at the given board position with the current graphics color,
     * taking the board orientation into account.
     *
     * @param g   the {@link Graphics2D} context to draw on
     * @param pos the board position to highlight
     */
    private void highlightSquare(Graphics2D g, Position pos) {
        int displayRow = boardFlipped ? 7 - pos.row() : pos.row();
        int displayCol = boardFlipped ? 7 - pos.col() : pos.col();
        g.fillRect(displayCol * SQUARE_SIZE, displayRow * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    /**
     * Renders all pieces currently on the board using Unicode chess symbols
     * drawn with the "Segoe UI Symbol" font. A subtle drop shadow is applied
     * beneath each piece glyph.
     *
     * @param g the {@link Graphics2D} context to draw on
     */
    private void drawPieces(Graphics2D g) {
        ChessBoard board = gameEngine.getBoard();
        g.setFont(SEGOE_UI_SYMBOL_FONT_PLAIN_56);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                ChessPiece piece = board.getPieceAt(pos);

                if (piece != null) {
                    int displayRow = boardFlipped ? 7 - row : row;
                    int displayCol = boardFlipped ? 7 - col : col;

                    String symbol = String.valueOf(piece.getSymbol());
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(symbol);
                    int textHeight = fm.getAscent();

                    int x = displayCol * SQUARE_SIZE + (SQUARE_SIZE - textWidth) / 2;
                    int y = displayRow * SQUARE_SIZE + (SQUARE_SIZE + textHeight) / 2 - 5;

                    g.setColor(new Color(0, 0, 0, 50));
                    g.drawString(symbol, x + 2, y + 2);

                    g.setColor(Color.BLACK);
                    g.drawString(symbol, x, y);
                }
            }
        }
    }

    /**
     * Draws rank numbers (1–8) along the left edge and file letters (a–h) along
     * the bottom edge of the board, colored to contrast with their host square.
     * Labels are adjusted for board orientation.
     *
     * @param g the {@link Graphics2D} context to draw on
     */
    private void drawCoordinates(Graphics2D g) {
        g.setFont(ARIAL_FONT_BOLD_12);

        for (int i = 0; i < 8; i++) {
            int displayI = boardFlipped ? 7 - i : i;

            // Letters (columns)
            char colChar = (char) ('a' + i);
            boolean isLight = (7 + i) % 2 == 0;
            g.setColor(isLight ? DARK_SQUARE : LIGHT_SQUARE);
            g.drawString(String.valueOf(colChar), displayI * SQUARE_SIZE + SQUARE_SIZE - 12,
                    SQUARE_SIZE * 8 - 4);

            // Numbers (rows)
            int rowNum = boardFlipped ? i + 1 : 8 - i;
            isLight = i % 2 == 0;
            g.setColor(isLight ? DARK_SQUARE : LIGHT_SQUARE);
            g.drawString(String.valueOf(rowNum), 4, displayI * SQUARE_SIZE + 14);
        }
    }

    /**
     * Converts a pixel coordinate from a mouse event into a board {@link Position},
     * taking the current board orientation into account.
     *
     * @param x the x pixel coordinate of the mouse event
     * @param y the y pixel coordinate of the mouse event
     * @return the corresponding board {@link Position} (may be invalid if out of bounds)
     */
    private Position getPositionFromMouse(int x, int y) {
        int col = x / SQUARE_SIZE;
        int row = y / SQUARE_SIZE;

        if (boardFlipped) {
            col = 7 - col;
            row = 7 - row;
        }

        return new Position(row, col);
    }

    /**
     * Handles a mouse click on the board.
     *
     * <p>First click on a friendly piece selects it and computes its legal moves.
     * Second click either:</p>
     * <ul>
     *   <li>Deselects the piece if the same square is clicked again.</li>
     *   <li>Executes the move if the destination is among the legal moves,
     *       prompting for a promotion piece when applicable.</li>
     *   <li>Re-selects another friendly piece if one is clicked.</li>
     *   <li>Clears the selection otherwise.</li>
     * </ul>
     *
     * @param e the {@link MouseEvent} triggered by the user
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameEngine.getGameState().isGameOver()) {
            return;
        }

        Position clickedPos = getPositionFromMouse(e.getX(), e.getY());

        if (!clickedPos.isValid()) {
            return;
        }

        if (selectedPosition == null) {
            // first click - try to select a piece
            ChessPiece piece = gameEngine.getBoard().getPieceAt(clickedPos);
            if (piece != null && piece.getColor() == gameEngine.getCurrentPlayer()) {
                selectedPosition = clickedPos;
                legalMovesForSelected = gameEngine.getLegalMovesFrom(clickedPos);
                repaint();
            }
        } else {
            // Second click - try to move or select another piece
            if (clickedPos.equals(selectedPosition)) {
                // Deselect
                clearSelection();
            } else {
                // Search for a legal move matching the clicked destination
                Move targetMove = findMoveToPosition(clickedPos);

                if (targetMove != null) {
                    if (targetMove.isPromotion()) {
                        // Prompt the user to choose a piece type for promotion, then execute the move with the
                        // chosen type
                        ChessPieceType promotionType = parentGUI.askForPromotion();
                        if (promotionType != null) {
                            gameEngine.makeMove(selectedPosition, clickedPos, promotionType);
                        }
                    } else {
                        gameEngine.makeMove(targetMove);
                    }
                    clearSelection();
                } else {
                    // Verify if clicking on another friendly piece to select it
                    ChessPiece piece = gameEngine.getBoard().getPieceAt(clickedPos);
                    if (piece != null && piece.getColor() == gameEngine.getCurrentPlayer()) {
                        selectedPosition = clickedPos;
                        legalMovesForSelected = gameEngine.getLegalMovesFrom(clickedPos);
                        repaint();
                    } else {
                        clearSelection();
                    }
                }
            }
        }
    }

    /**
     * Searches the legal moves of the currently selected piece for a move
     * whose destination equals the given position.
     *
     * @param to the target board position
     * @return the matching {@link Move}, or {@code null} if none is found
     */
    private Move findMoveToPosition(Position to) {
        if (legalMovesForSelected == null) return null;

        for (Move move : legalMovesForSelected) {
            if (move.getTo().equals(to)) {
                return move;
            }
        }
        return null;
    }

    /**
     * Not used — required by {@link MouseListener}.
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Not used — required by {@link MouseListener}.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Not used — required by {@link MouseListener}.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Not used — required by {@link MouseListener}.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
