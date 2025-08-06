package com.fcuillandre.chessbot.ui;

import com.fcuillandre.chessbot.board.ChessBoard;
import com.fcuillandre.chessbot.pieces.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.utils.ChessUtils;

public class ChessBoardPanel extends JPanel {
    private final JButton[][] squares = new JButton[8][8];
    private Coordinate selected = null;
    private MoveListener moveListener;

    private final JPanel gridPanel = new JPanel(new GridLayout(8, 8));
    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final JLabel[] colLabels = new JLabel[8];
    private final JLabel[] rowLabels = new JLabel[8];

    public interface MoveListener {
        void onMove(Coordinate from, Coordinate to);
    }

    public void setMoveListener(MoveListener listener) {
        this.moveListener = listener;
    }

    public ChessBoardPanel(ChessBoard board) {
        setLayout(new BorderLayout());
        // Labels colonnes (A-H)
        JPanel colLabelPanel = new JPanel(new GridLayout(1, 9));
        colLabelPanel.add(new JLabel("")); // coin vide
        for (int y = 0; y < 8; y++) {
            colLabels[y] = new JLabel(String.valueOf((char)('A' + y)), SwingConstants.CENTER);
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
        for (int x = 7; x >= 0 ; x--) {
            for (int y = 0; y < 8; y++) {

                int realX = x;
                int realY = y;

                JButton button = new JButton();
                button.setFocusPainted(false);
                button.setOpaque(true);
                button.setBackground(((x + y + 1) % 2 == 0) ? Color.WHITE : Color.LIGHT_GRAY);
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.addActionListener(e -> handleSquareClick(realX, realY));
                squares[x][y] = button;
                gridPanel.add(button);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
        updateBoard(board);
    }

    private void handleSquareClick(int x, int y) {

        ChessUtils.log("click on " + x + " / " + y);

        if (selected == null) {
            selected = new Coordinate(x, y);
            squares[x][y].setBackground(Color.YELLOW);
        } else {
            if (moveListener != null) {
                moveListener.onMove(selected, new Coordinate(x, y));
            }
            resetSelection();
        }
    }

    private void resetSelection() {
        for (int x = 7; x >= 0 ; x--) {
            for (int y = 0; y < 8; y++) {
                squares[x][y].setBackground((x + y + 1) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
            }
        }
        selected = null;
    }

    public void updateBoard(ChessBoard board) {
        for (int x = 7 ; x >= 0 ; x--) {
            for (int y = 0; y < 8; y++) {
                int displayX = 7 - x;
                ChessPiece piece = board.getPieceAt(x, y);
                squares[x][y].setText( getBtnLabel(piece, x, y));
            }
        }
    }

    private String getBtnLabel(ChessPiece piece, int x, int y) {
        if (piece == null) {
            return "";
        }
        return piece.getType().name().substring(0, 1);
    }
}
