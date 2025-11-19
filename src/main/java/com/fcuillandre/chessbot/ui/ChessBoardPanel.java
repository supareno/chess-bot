package com.fcuillandre.chessbot.ui;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

/**
 * ChessBoardPanel is a JPanel that displays a chess board with buttons for each square.
 * It allows users to click on squares to select pieces and make moves.
 * The board updates based on the current state of the ChessGame.
 *
 * @author FCuillandre
 * @version 1.0
 */
public final class ChessBoardPanel extends JPanel {

    private final SquarePanel[][] squares = new SquarePanel[8][8];
    private final ChessGame game;
    private final ChessGameFrame chessGameFrame;
    private final JPanel gridPanel = new JPanel(new GridLayout(8, 8));
    private Coordinate selected = null;

    @Getter
    @Setter
    private MoveListener moveListener;

    /**
     * Constructor for ChessBoardPanel.
     * Initializes the chess board with buttons and labels for columns and rows.
     *
     * @param game The ChessGame instance that this panel will display.
     */
    public ChessBoardPanel(ChessGameFrame chessGameFrame, ChessGame game) {
        this.chessGameFrame = chessGameFrame;
        this.game = game;

        setLayout(new BorderLayout());
        // Remove old column and row label panels
        // Add only the gridPanel to the center
        gridPanel.setPreferredSize(new Dimension(480, 480));
        for (int x = 7; x >= 0; x--) {
            for (int y = 0; y < 8; y++) {

                SquarePanel panel = new SquarePanel(x, y);
                squares[x][y] = panel;
                gridPanel.add(panel);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
        updateBoard(this.game.getBoard());
    }

    /**
     * Handles a click on a square at the given coordinates.
     * If no piece is selected, selects the piece at the clicked square.
     * If a piece is already selected, attempts to move it to the clicked square.
     *
     * @param x The x-coordinate of the clicked square (0-7).
     * @param y The y-coordinate of the clicked square (0-7).
     */
    private void handleSquareClick(int x, int y) {
        if (selected == null) {
            ChessPiece piece = this.game.getBoard().getPieceAt(x, y);
            String title = this.chessGameFrame.getMessages().getString("dialog.error_title");
            if (piece != null && piece.getColor().equals(ChessColor.WHITE) && !this.game.isWhiteTurn()) {
                String message = this.chessGameFrame.getMessages().getString("dialog.not_white_turn_to_play");
                JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
                return;
            } else if (piece != null && piece.getColor().equals(ChessColor.BLACK) && this.game.isWhiteTurn()) {
                String message = this.chessGameFrame.getMessages().getString("dialog.not_black_turn_to_play");
                JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
                return;

            }
            selected = new Coordinate(x, y);
            squares[x][y].setSelected(true);
        } else {
            // Si on clique deux fois sur la même case, annule la sélection
            if (selected.getX() == x && selected.getY() == y) {
                resetSelection();
                return;
            }
            if (moveListener != null) {
                moveListener.onMove(selected, new Coordinate(x, y));
            }
            resetSelection();
        }
    }

    private void resetSelection() {
        for (int x = 7; x >= 0; x--) {
            for (int y = 0; y < 8; y++) {
                squares[x][y].setSelected(false);
            }
        }
        selected = null;
    }

    /**
     * Updates the chess board display based on the current state of the ChessBoard.
     * <p>Each button's text is set to the unicode representation of the piece at that square.</p>
     *
     * @param board The ChessBoard to update the display from.
     */
    public void updateBoard(ChessBoard board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ChessPiece piece = board.getPieceAt(x, y);
                squares[x][y].setPiece(piece);
            }
        }
    }

    private String getPieceUnicode(ChessPiece piece) {
        if (piece == null) return "";
        ChessPieceType type = piece.getType();
        ChessColor color = piece.getColor();
        switch (type) {
            case KING:
                return color == ChessColor.WHITE ? "\u2654" : "\u265A";
            case QUEEN:
                return color == ChessColor.WHITE ? "\u2655" : "\u265B";
            case ROOK:
                return color == ChessColor.WHITE ? "\u2656" : "\u265C";
            case BISHOP:
                return color == ChessColor.WHITE ? "\u2657" : "\u265D";
            case KNIGHT:
                return color == ChessColor.WHITE ? "\u2658" : "\u265E";
            case PAWN:
                return color == ChessColor.WHITE ? "\u2659" : "\u265F";
            default:
                return "";
        }
    }

    public interface MoveListener {
        void onMove(Coordinate from, Coordinate to);
    }

    private class SquarePanel extends JPanel {
        private final int x, y;
        private final JLabel letterLabel;
        private final JLabel numberLabel;
        private final JLabel pieceLabel;
        private boolean selected = false;

        public SquarePanel(int x, int y) {
            this.x = x;
            this.y = y;
            setLayout(new BorderLayout());
            setBackground(((x + y + 1) % 2 == 0) ? Color.WHITE : Color.LIGHT_GRAY);
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            // Number label (first column)
            if (y == 0) {
                numberLabel = new JLabel(String.valueOf(x + 1), SwingConstants.LEFT);
                numberLabel.setFont(new Font("Arial", Font.BOLD, 12));
                add(numberLabel, BorderLayout.WEST);
            } else {
                numberLabel = null;
            }
            // Letter label (first row)
            if (x == 0) {
                letterLabel = new JLabel(String.valueOf((char) ('A' + y)), SwingConstants.CENTER);
                letterLabel.setFont(new Font("Arial", Font.BOLD, 12));
                add(letterLabel, BorderLayout.SOUTH);
            } else {
                letterLabel = null;
            }
            // Piece label (center)
            pieceLabel = new JLabel("", SwingConstants.CENTER);
            pieceLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 30));
            add(pieceLabel, BorderLayout.CENTER);
            // Mouse click
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    handleSquareClick(x, y);
                }
            });
        }

        public void setPiece(ChessPiece piece) {
            pieceLabel.setText(getPieceUnicode(piece));
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            setBackground(selected ? Color.YELLOW : ((x + y + 1) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY));
        }
    }
}
