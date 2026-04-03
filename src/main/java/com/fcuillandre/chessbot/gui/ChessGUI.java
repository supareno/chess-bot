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

import com.fcuillandre.chessbot.bot.ChessBot;
import com.fcuillandre.chessbot.engine.GameEngine;
import com.fcuillandre.chessbot.engine.GameListener;
import com.fcuillandre.chessbot.engine.GameState;
import com.fcuillandre.chessbot.model.ChessBoard;
import com.fcuillandre.chessbot.model.ChessColor;
import com.fcuillandre.chessbot.model.ChessPieceType;
import com.fcuillandre.chessbot.model.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Locale;

/**
 * Graphic User Interface for the Chess game using Java Swing.
 * This class manages the main window, menu, status bar, and integrates the chess board and move history panels.
 * It also handles user interactions and updates the display based on game events.
 *
 * @author fcuillandre
 * @since 1.0
 */
public class ChessGUI extends JFrame implements GameListener {

    private final GameEngine gameEngine;
    private final BoardPanel boardPanel;
    private final MoveHistoryPanel moveHistoryPanel;
    private final JLabel currentPlayerLabel;
    private final JLabel gameStateLabel;

    // Menu items kept as fields so they can be re-labelled on locale change
    private JMenu homeMenu;
    private JMenuItem newGameItem;
    private JMenuItem quitItem;
    private JMenu languageMenu;
    private JMenuItem englishItem;
    private JMenuItem frenchItem;
    private JMenu helpMenu;
    private JMenuItem infosItem;

    private ChessBot bot;
    private ChessColor playerColor;
    private boolean isVsBot;

    public ChessGUI() {
        super(Messages.get("app.title"));

        this.gameEngine = new GameEngine();
        this.isVsBot = false;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setJMenuBar(createMenuBar());

        boardPanel = new BoardPanel(gameEngine, this);
        moveHistoryPanel = new MoveHistoryPanel(gameEngine);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, java.awt.Color.GRAY),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        currentPlayerLabel = new JLabel(Messages.get("status.turn", Messages.get("status.white")));
        currentPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gameStateLabel = new JLabel("");
        gameStateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gameStateLabel.setForeground(java.awt.Color.RED);
        statusBar.add(currentPlayerLabel);
        statusBar.add(gameStateLabel);

        add(boardPanel, BorderLayout.CENTER);
        add(moveHistoryPanel, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);

        gameEngine.addGameListener(this);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        // Start with the new game dialog
        setVisible(true);
        showNewGameDialog();
    }

    /**
     * Creates the menu bar.
     *
     * @return the created JMenuBar
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Home menu
        homeMenu = new JMenu(Messages.get("menu.home"));
        homeMenu.setMnemonic(KeyEvent.VK_A);

        newGameItem = new JMenuItem(Messages.get("menu.home.newgame"));
        newGameItem.addActionListener(e -> showNewGameDialog());
        homeMenu.add(newGameItem);

        homeMenu.addSeparator();

        quitItem = new JMenuItem(Messages.get("menu.home.quit"));
        quitItem.addActionListener(e -> System.exit(0));
        homeMenu.add(quitItem);

        // Language menu
        languageMenu = new JMenu(Messages.get("menu.language"));
        languageMenu.setMnemonic(KeyEvent.VK_L);

        englishItem = new JMenuItem(Messages.get("menu.language.english"));
        englishItem.addActionListener(e -> switchLanguage(Locale.ENGLISH));
        languageMenu.add(englishItem);

        frenchItem = new JMenuItem(Messages.get("menu.language.french"));
        frenchItem.addActionListener(e -> switchLanguage(Locale.FRENCH));
        languageMenu.add(frenchItem);

        // Help menu
        helpMenu = new JMenu(Messages.get("menu.help"));
        helpMenu.setMnemonic(KeyEvent.VK_I);

        infosItem = new JMenuItem(Messages.get("menu.help.about"));
        infosItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(infosItem);

        menuBar.add(homeMenu);
        menuBar.add(languageMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * Switches the UI language and refreshes all labels.
     *
     * @param locale the target locale
     */
    private void switchLanguage(final Locale locale) {
        Messages.setLocale(locale);
        refreshMenuLabels();
        // Refresh status bar with current game state
        onGameStateChanged(gameEngine.getGameState(), gameEngine.getCurrentPlayer());
        // Refresh child panels
        moveHistoryPanel.refreshLabels();
    }

    /**
     * Re-applies translated strings to all menu items after a locale change.
     */
    private void refreshMenuLabels() {
        homeMenu.setText(Messages.get("menu.home"));
        newGameItem.setText(Messages.get("menu.home.newgame"));
        quitItem.setText(Messages.get("menu.home.quit"));
        languageMenu.setText(Messages.get("menu.language"));
        englishItem.setText(Messages.get("menu.language.english"));
        frenchItem.setText(Messages.get("menu.language.french"));
        helpMenu.setText(Messages.get("menu.help"));
        infosItem.setText(Messages.get("menu.help.about"));
    }

    /**
     * Shows the "About" dialog box.
     */
    private void showAboutDialog() {
        String message = Messages.get("about.message").replace("\\n", "\n");
        JOptionPane.showMessageDialog(this, message, Messages.get("about.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows the new game dialog.
     */
    public void showNewGameDialog() {
        NewGameDialog dialog = new NewGameDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            startNewGame(dialog.isVsBot(), dialog.getPlayerColor(), dialog.getBotDifficulty());
        }
    }

    /**
     * Starts a new game with the given settings.
     */
    public void startNewGame(boolean vsBot, ChessColor playerColor, int difficulty) {
        this.isVsBot = vsBot;
        this.playerColor = playerColor;

        if (vsBot) {
            this.bot = new ChessBot(playerColor.opposite(), difficulty);
        } else {
            this.bot = null;
        }

        boardPanel.setBoardFlipped(playerColor == ChessColor.BLACK);

        gameStateLabel.setText("");
        moveHistoryPanel.clear();
        boardPanel.clearSelection();

        gameEngine.newGame();

        if (isVsBot && playerColor == ChessColor.BLACK) {
            SwingUtilities.invokeLater(this::makeBotMove);
        }
    }

    /**
     * Flips the board orientation.
     */
    public void flipBoard() {
        boardPanel.setBoardFlipped(!boardPanel.isBoardFlipped());
    }

    /**
     * Asks the player which piece to promote to.
     */
    public ChessPieceType askForPromotion() {
        String[] options = {"Queen \u2655", "Rook \u2656", "Bishop \u2657", "Knight \u2658"};
        int choice = JOptionPane.showOptionDialog(this,
                Messages.get("promotion.message"),
                Messages.get("promotion.title"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        return switch (choice) {
            case 0 -> ChessPieceType.QUEEN;
            case 1 -> ChessPieceType.ROOK;
            case 2 -> ChessPieceType.BISHOP;
            case 3 -> ChessPieceType.KNIGHT;
            default -> ChessPieceType.QUEEN;
        };
    }

    /**
     * Makes the bot play its move if it's the bot's turn.
     */
    private void makeBotMove() {
        if (!isVsBot || bot == null || gameEngine.getGameState().isGameOver()) {
            return;
        }

        if (gameEngine.getCurrentPlayer() != bot.getColor()) {
            return;
        }

        // Deactivate interaction when bot is thinking
        boardPanel.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<Move, Void> worker = new SwingWorker<>() {
            @Override
            protected Move doInBackground() {
                return bot.findBestMove(gameEngine);
            }

            @Override
            protected void done() {
                try {
                    Move botMove = get();
                    if (botMove != null) {
                        gameEngine.makeMove(botMove);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    boardPanel.setEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };

        worker.execute();
    }

    @Override
    public void onBoardChanged(ChessBoard board) {
        boardPanel.repaint();
    }

    @Override
    public void onGameStateChanged(GameState state, ChessColor currentPlayer) {
        String playerText = currentPlayer == ChessColor.WHITE
                ? Messages.get("status.white")
                : Messages.get("status.black");
        currentPlayerLabel.setText(Messages.get("status.turn", playerText));

        if (state == GameState.CHECK) {
            gameStateLabel.setText(Messages.get("status.check"));
            gameStateLabel.setForeground(java.awt.Color.ORANGE);
        } else if (state == GameState.CHECKMATE) {
            String winner = currentPlayer.opposite() == ChessColor.WHITE
                    ? Messages.get("status.whites.win")
                    : Messages.get("status.blacks.win");
            gameStateLabel.setText(Messages.get("status.checkmate", winner));
            gameStateLabel.setForeground(java.awt.Color.RED);
        } else if (state.isGameOver()) {
            gameStateLabel.setText(Messages.get(state.getMessageKey()));
            gameStateLabel.setForeground(java.awt.Color.BLUE);
        } else {
            gameStateLabel.setText("");
        }
    }

    @Override
    public void onMoveMade(Move move) {
        if (isVsBot && !gameEngine.getGameState().isGameOver()) {
            if (gameEngine.getCurrentPlayer() == bot.getColor()) {
                Timer timer = new Timer(300, e -> makeBotMove());
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
}
