package com.fcuillandre.chessbot.ui;

import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.utils.ChessUtils;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the main frame for the chess game UI.
 * It contains the chess board panel, a list of moves, and a label indicating whose turn it is.
 * The frame allows players to make moves and displays the history of moves made in the game.
 *
 * @author FCuillandre
 * @version 1.0
 */
public class ChessGameFrame extends JFrame {

    private final ChessBoardPanel boardPanel;
    private final ChessGame game;
    private final DefaultListModel<String> moveListModel = new DefaultListModel<>();
    private final JList<String> moveList = new JList<>(moveListModel);
    private final JLabel turnLabel = new JLabel();

    /**
     * Constructor for the ChessGameFrame.
     * Initializes the frame with the given chess game, sets up the board panel,
     * and configures the layout and components.
     *
     * @param game The chess game to be displayed in the frame.
     */
    public ChessGameFrame(ChessGame game) {
        super("Jeu d'échecs");
        this.game = game;
        this.boardPanel = new ChessBoardPanel(game);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // Fixer la taille du plateau de jeu
        boardPanel.setPreferredSize(new Dimension(600, 600));
        boardPanel.setMinimumSize(new Dimension(600, 600));
        boardPanel.setMaximumSize(new Dimension(600, 600));
        add(boardPanel, BorderLayout.CENTER);
        // Fixer la taille de la liste des coups
        moveList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        moveList.setPreferredSize(new Dimension(180, 600));
        moveList.setMinimumSize(new Dimension(180, 600));
        moveList.setMaximumSize(new Dimension(180, 600));
        JScrollPane moveListScrollPane = new JScrollPane(moveList);
        moveListScrollPane.setPreferredSize(new Dimension(180, 600));
        moveListScrollPane.setMinimumSize(new Dimension(180, 600));
        moveListScrollPane.setMaximumSize(new Dimension(180, 600));
        add(moveListScrollPane, BorderLayout.EAST);
        add(turnLabel, BorderLayout.NORTH);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        boardPanel.setMoveListener(this::handleMove);
        updateTurnLabel();
    }

    private void handleMove(Coordinate from, Coordinate to) {
        ChessUtils.log("from " + from + " to " + to);
        Move move = new Move(from, to);
        if (game.isValidMove(move)) {
            game.makeMove(move); // move is added to the history in the makeMove method
            refreshMoveList(); // Nouvelle méthode pour afficher les paires
            refresh();
            updateTurnLabel();
        } else {
            JOptionPane.showMessageDialog(this, "Coup invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Affiche l'historique des coups par paires (blanc/noir)
    private void refreshMoveList() {
        moveListModel.clear();

        for (int i = 0; i < game.getMoveHistory().size(); i += 2) {
            Move whiteMove = game.getMoveHistory().get(i);
            Move blackMove = (i + 1 < game.getMoveHistory().size()) ? game.getMoveHistory().get(i + 1) : null;
            if (whiteMove == null && blackMove == null) continue; // Skip if both

            String line = this.getIndex(i) + " : ";
            if (whiteMove != null) {
                line += formatMove(whiteMove.getStart(), whiteMove.getEnd());
            }
            if (blackMove != null) {
                line += " / " + formatMove(blackMove.getStart(), blackMove.getEnd());
            }
            moveListModel.addElement(line);
        }
    }

    /**
     * Returns the index for the move list based on the move number.
     * The first move is indexed as "1", the second as "2", and so on.
     *
     * @param i The move number (0-based index).
     * @return The formatted index as a string.
     */
    private String getIndex(int i) {
        if (i == 0) return "1";
        return String.valueOf((i / 2) + 1);
    }

    /**
     * Formats the move from one coordinate to another as a string.
     * The format is "a1-b2" where 'a' is the column and '1' is the row.
     *
     * @param from The starting coordinate of the move.
     * @param to   The ending coordinate of the move.
     * @return The formatted move string.
     */
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
