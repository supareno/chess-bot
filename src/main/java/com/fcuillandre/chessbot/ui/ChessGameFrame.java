package com.fcuillandre.chessbot.ui;

import com.fcuillandre.chessbot.bot.ChessBot;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.pieces.ChessColor;

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
    private final ChessBot bot = new ChessBot();
    private boolean gameOver = false;

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
        if (gameOver) return;
        Move move = new Move(from, to);
        if (game.isValidMove(move)) {
            game.makeMove(move); // move is added to the history in the makeMove method
            refreshMoveList(); // Nouvelle méthode pour afficher les paires
            // Vérification de la mise en échec du roi après le coup
            if (game.isKingInCheck()) {
                boardPanel.setKingInCheckCoordinate(game.getKingCoordinate(game.isWhiteTurn() ? ChessColor.WHITE : ChessColor.BLACK));
                updateTurnLabelWithCheck();
            } else {
                boardPanel.setKingInCheckCoordinate(null);
                updateTurnLabel();
            }

            refresh();

            // Check for checkmate after human move
            if (game.isCheckmate()) {
                gameOver = true;
                displayMessage(this, "Échec et mat ! Les " + (game.isWhiteTurn() ? "blancs" : "noirs") + " ont perdu.", "Fin de partie", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Check for stalemate after human move
            if (game.isStalemate()) {
                gameOver = true;
                displayMessage(this, "Partie nulle par pat !", "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Trigger bot move if it's now Black's turn

            if (!game.isWhiteTurn()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(3000); // Wait at least 3 seconds
                    } catch (InterruptedException ignored) {
                    }
                    if (gameOver) return;
                    Move botMove = bot.getRandomLegalMove(game);
                    if (botMove != null) {
                        SwingUtilities.invokeLater(() -> {
                            game.makeMove(botMove);
                            refreshMoveList();
                            refresh();
                            // Highlight king in check for the opponent (after move, turn has switched)
                            ChessColor opponentColor = game.isWhiteTurn() ? ChessColor.BLACK : ChessColor.WHITE;
                            if (game.isKingInCheck()) {
                                boardPanel.setKingInCheckCoordinate(game.getKingCoordinate(opponentColor));
                                updateTurnLabelWithCheck();
                            } else {
                                boardPanel.setKingInCheckCoordinate(null);
                                updateTurnLabel();
                            }
                            // Check for checkmate after bot move
                            if (game.isCheckmate()) {
                                gameOver = true;
                                displayMessage(this, "Échec et mat ! Les " + (game.isWhiteTurn() ? "blancs" : "noirs") + " ont perdu.", "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                            // Check for stalemate after bot move
                            if (game.isStalemate()) {
                                gameOver = true;
                                displayMessage(this, "Partie nulle par pat !", "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        });
                    }
                }).start();
            }
        } else {
            // Message spécifique si le roi reste en échec
            if (game.isKingInCheck()) {
                displayMessage(this, "Coup invalide : le roi est toujours en échec !", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                displayMessage(this, "Coup invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Pawn promotion check (after move, before bot move)
        Move lastMove = game.getLastMove();
        if (lastMove != null) {
            int endX = lastMove.getEnd().getX();
            int endY = lastMove.getEnd().getY();
            com.fcuillandre.chessbot.pieces.ChessPiece promotedPawn = game.getBoard().getPieceAt(endX, endY);
            if (promotedPawn != null && promotedPawn.getType() == com.fcuillandre.chessbot.pieces.ChessPieceType.PAWN) {
                if (promotedPawn.getColor() == com.fcuillandre.chessbot.pieces.ChessColor.WHITE && endX == 7) {
                    // Human promotion dialog
                    com.fcuillandre.chessbot.pieces.ChessPieceType[] options = {
                        com.fcuillandre.chessbot.pieces.ChessPieceType.QUEEN,
                        com.fcuillandre.chessbot.pieces.ChessPieceType.ROOK,
                        com.fcuillandre.chessbot.pieces.ChessPieceType.BISHOP,
                        com.fcuillandre.chessbot.pieces.ChessPieceType.KNIGHT
                    };
                    String[] optionNames = {"Queen", "Rook", "Bishop", "Knight"};
                    int choice = JOptionPane.showOptionDialog(this,
                            "Choose a piece for promotion:",
                            "Pawn Promotion",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            optionNames,
                            optionNames[0]);
                    if (choice >= 0 && choice < options.length) {
                        game.promotePawn(new com.fcuillandre.chessbot.game.Coordinate(endX, endY), options[choice]);
                        refresh();
                    }
                } else if (promotedPawn.getColor() == com.fcuillandre.chessbot.pieces.ChessColor.BLACK && endX == 0) {
                    // Bot promotion: random
                    com.fcuillandre.chessbot.pieces.ChessPieceType[] options = {
                        com.fcuillandre.chessbot.pieces.ChessPieceType.QUEEN,
                        com.fcuillandre.chessbot.pieces.ChessPieceType.ROOK,
                        com.fcuillandre.chessbot.pieces.ChessPieceType.BISHOP,
                        com.fcuillandre.chessbot.pieces.ChessPieceType.KNIGHT
                    };
                    int idx = (int) (Math.random() * options.length);
                    game.promotePawn(new com.fcuillandre.chessbot.game.Coordinate(endX, endY), options[idx]);
                    refresh();
                }
            }
        }
    }

    /**
     * Displays a warning dialog when the king is in check.
     *
     * @param parentComponent the parent component for the dialog
     * @param message         the message to display
     * @param title           the title of the dialog
     * @param messageType     the type of message to be displayed
     */
    private void displayMessage(Component parentComponent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
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

    private void updateTurnLabelWithCheck() {
        turnLabel.setText("Au tour des " + (game.isWhiteTurn() ? "blancs" : "noirs") + " Attention le roi est en échec !");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void refresh() {
        boardPanel.updateBoard(game.getBoard());
    }
}
