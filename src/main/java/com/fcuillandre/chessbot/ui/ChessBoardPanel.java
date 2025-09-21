package com.fcuillandre.chessbot.ui;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import com.fcuillandre.chessbot.utils.ChessUtils;
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
public class ChessBoardPanel extends JPanel {

    private final JButton[][] squares = new JButton[8][8];
    private final ChessGame game;
    private final JPanel gridPanel = new JPanel(new GridLayout(8, 8));
    private final JLabel[] colLabels = new JLabel[8];
    private final JLabel[] rowLabels = new JLabel[8];
    private Coordinate selected = null;
    @Setter
    private Coordinate kingInCheckCoordinate = null;
    @Getter
    @Setter
    private MoveListener moveListener;

    /**
     * Constructor for ChessBoardPanel.
     * Initializes the chess board with buttons and labels for columns and rows.
     *
     * @param game The ChessGame instance that this panel will display.
     */
    public ChessBoardPanel(ChessGame game) {
        this.game = game;

        setLayout(new BorderLayout());
        // Labels colonnes (A-H)
        JPanel colLabelPanel = new JPanel(new GridLayout(1, 9));
        colLabelPanel.add(new JLabel("")); // coin vide
        for (int y = 0; y < 8; y++) {
            colLabels[y] = new JLabel(String.valueOf((char) ('A' + y)), SwingConstants.CENTER);
            colLabels[y].setFont(new Font("Arial", Font.BOLD, 16));
            colLabelPanel.add(colLabels[y]);
        }
        add(colLabelPanel, BorderLayout.NORTH);
        // Labels lignes (8-1)
        JPanel rowPanel = new JPanel(new GridLayout(8, 1));
        for (int x = 0; x < 8; x++) {
            rowLabels[x] = new JLabel(String.valueOf(8 - x), SwingConstants.CENTER);
            rowLabels[x].setFont(new Font("Arial", Font.BOLD, 16));
            rowPanel.add(rowLabels[x]);
        }
        add(rowPanel, BorderLayout.WEST);
        // Plateau
        gridPanel.setPreferredSize(new Dimension(480, 480));
        for (int x = 7; x >= 0; x--) {
            for (int y = 0; y < 8; y++) {

                int realX = x;
                int realY = y;

                JButton button = new JButton();
                button.setFocusPainted(false);
                button.setOpaque(true);
                button.setBackground(((x + y + 1) % 2 == 0) ? Color.WHITE : Color.LIGHT_GRAY);
                // Utilisation d'une police compatible Unicode pour les pièces d'échecs
                button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 36));
                button.addActionListener(e -> handleSquareClick(realX, realY));
                squares[x][y] = button;
                gridPanel.add(button);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
        updateBoard(this.game.getBoard());
    }

    private void handleSquareClick(int x, int y) {


        if (selected == null) {
            ChessPiece piece = this.game.getBoard().getPieceAt(x, y);
            if (piece != null && piece.getColor().equals(ChessColor.WHITE) && !this.game.isWhiteTurn()) {
                JOptionPane.showMessageDialog(this, "Ce n'est pas au blanc de jouer!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (piece != null && piece.getColor().equals(ChessColor.BLACK) && this.game.isWhiteTurn()) {
                JOptionPane.showMessageDialog(this, "Ce n'est pas au noir de jouer!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;

            }
            selected = new Coordinate(x, y);
            squares[x][y].setBackground(Color.YELLOW);
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
                squares[x][y].setBackground((x + y + 1) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
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
                JButton button = squares[x][y];
                ChessPiece piece = board.getPieceAt(x, y);
                Color bg = ((x + y + 1) % 2 == 0) ? Color.WHITE : Color.LIGHT_GRAY;
                if (piece != null) {
                    if (piece.getType() == ChessPieceType.KING && kingInCheckCoordinate != null) {
                        ChessUtils.log("Roi en échec en " + kingInCheckCoordinate);
                        //button.setBackground(Color.ORANGE);
                    }
                    button.setText(getBtnLabel(piece, x, y));
                } else {
                    button.setText("");
                }
                button.setBackground(((x + y + 1) % 2 == 0) ? Color.WHITE : Color.LIGHT_GRAY);

            }
        }
    }

    /**
     * Returns the label for a button based on the piece type and color.
     * <p>The String representation of a piece is an unicode letter representing a piece</p>
     *
     * @param piece The ChessPiece to get the label for.
     * @param x     The x-coordinate of the button.
     * @param y     The y-coordinate of the button.
     * @return A string representing the piece, or an empty string if no piece is present.
     */
    private String getBtnLabel(ChessPiece piece, int x, int y) {
        if (piece == null) {
            return "";
        }
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
}
