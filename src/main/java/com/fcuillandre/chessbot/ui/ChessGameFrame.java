package com.fcuillandre.chessbot.ui;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChessGameFrame extends JFrame {

    private final ChessBoardPanel boardPanel;
    private final ChessGame game;
    private final DefaultListModel<String> moveListModel = new DefaultListModel<>();
    private final JList<String> moveList = new JList<>(moveListModel);
    private final java.util.List<String> moveHistory = new ArrayList<>();
    private final JLabel turnLabel = new JLabel();

    public ChessGameFrame(ChessGame game) {
        super("Jeu d'échecs");
        this.game = game;
        this.boardPanel = new ChessBoardPanel(game.getBoard());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        moveList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        moveList.setPreferredSize(new Dimension(120, 600));
        add(new JScrollPane(moveList), BorderLayout.EAST);
        add(turnLabel, BorderLayout.NORTH);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        boardPanel.setMoveListener((from, to) -> handleMove(from, to));
        updateTurnLabel();
    }

    private void handleMove(Coordinate from, Coordinate to) {
        System.out.println("from " + from + " to " + to);
        Move move = new Move(from, to);
        if (game.isValidMove(move)) {
            game.makeMove(move);
            moveHistory.add(formatMove(from, to));
            moveListModel.addElement(formatMove(from, to));
            refresh();
            updateTurnLabel();
        } else {
            JOptionPane.showMessageDialog(this, "Coup invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatMove(Coordinate from, Coordinate to) {
        char colFrom = (char) ('a' + from.getY());
        int rowFrom = 1 + from.getX();
        char colTo = (char) ('a' + to.getY());
        int rowTo = 1 + to.getX();
        return "" + colFrom + rowFrom + "-" + colTo + rowTo;
    }

    private void updateTurnLabel() {
        turnLabel.setText("Au tour des " + (game.isWhiteTurn() ? "blancs" : "noirs"));
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void refresh() {
        boardPanel.updateBoard(game.getBoard());
    }
}
