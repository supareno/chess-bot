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

import com.fcuillandre.chessbot.bot.ChessBot;
import com.fcuillandre.chessbot.bot.RandomChessBot;
import com.fcuillandre.chessbot.bot.minimax.MinimaxChessBot;
import com.fcuillandre.chessbot.bot.minimax.MinimaxPlusChessBot;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.game.Coordinate;
import com.fcuillandre.chessbot.game.Move;
import com.fcuillandre.chessbot.game.MovedPiece;
import com.fcuillandre.chessbot.pieces.ChessColor;
import com.fcuillandre.chessbot.pieces.ChessPiece;
import com.fcuillandre.chessbot.pieces.ChessPieceType;
import com.fcuillandre.chessbot.utils.ChessMoveFormatterUtils;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.fcuillandre.chessbot.ui.ChessAppFonts.*;

/**
 * This class represents the main frame for the chess game UI.
 * It contains the chess board panel, a list of moves, and a label indicating whose turn it is.
 * The frame allows players to make moves and displays the history of moves made in the game.
 *
 * @author fcuillandre
 * @version 0.1
 */
public class ChessGameFrame extends JFrame {


    public static final String MINIMAX_PLUS = "minimax_plus";
    public static final String MINIMAX = "minimax";
    public static final String RANDOM = "random";
    private static final com.fcuillandre.chessbot.pieces.ChessPieceType[] PROMOTION_OPTIONS = {
            com.fcuillandre.chessbot.pieces.ChessPieceType.QUEEN,
            com.fcuillandre.chessbot.pieces.ChessPieceType.ROOK,
            com.fcuillandre.chessbot.pieces.ChessPieceType.BISHOP,
            com.fcuillandre.chessbot.pieces.ChessPieceType.KNIGHT
    };
    public static final String RANDOM_LEVEL_STARS = "\u2605\u2606\u2606";
    public static final String MINIMAX_LEVEL_STARS = "\u2605\u2605\u2606";
    public static final String MINIMAX_PLUS_LEVEL_STARS = "\u2605\u2605\u2605";
    @Getter
    private final DefaultListModel<String> moveListModel = new DefaultListModel<>();
    @Getter
    private final JList<String> moveList = new JList<>(moveListModel);
    private final JLabel turnLabel = new JLabel();
    private final JLabel botLevelLabel = new JLabel();
    private ChessBot bot = new MinimaxChessBot();
    private ChessBoardPanel boardPanel;
    private ChessGame game;
    private boolean gameOver = false;
    private EndGameStatus endGameStatus = EndGameStatus.ONGOING;
    private Locale currentLocale = Locale.getDefault();
    @Getter
    private ResourceBundle messages = ResourceBundle.getBundle("messages", currentLocale);
    private JMenuBar menuBar;
    private JMenu homeMenu;
    private JMenu helpMenu;
    private JMenu languageMenu;
    private JMenu botLevelMenu;
    private JMenuItem newGameItem;
    private JMenuItem quitItem;
    private JMenuItem infosItem;
    private JMenuItem englishItem;
    private JMenuItem frenchItem;
    private JRadioButtonMenuItem randomBotItem;
    private JRadioButtonMenuItem minimaxBotItem;
    private JRadioButtonMenuItem minimaxPlusBotItem;
    private ButtonGroup botLevelGroup;

    /**
     * Constructor for the ChessGameFrame.
     * Initializes the frame with the given chess game, sets up the board panel,
     * and configures the layout and components.
     *
     * @param game The chess game to be displayed in the frame.
     */
    public ChessGameFrame(ChessGame game) {
        super(ResourceBundle.getBundle("messages", Locale.getDefault()).getString("app.title"));
        this.game = game;
        this.boardPanel = new ChessBoardPanel(this, game);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());
        boardPanel.setPreferredSize(new Dimension(600, 600));
        boardPanel.setMinimumSize(new Dimension(600, 600));
        boardPanel.setMaximumSize(new Dimension(600, 600));
        add(boardPanel, BorderLayout.CENTER);
        moveList.setFont(MONOSPACED_FONT_BOLD_14);
        moveList.setMinimumSize(new Dimension(180, 600));

        JScrollPane moveListScrollPane = new JScrollPane(moveList);
        moveListScrollPane.setPreferredSize(new Dimension(180, 600));
        moveListScrollPane.setMinimumSize(new Dimension(180, 600));
        moveListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(moveListScrollPane, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(turnLabel, BorderLayout.CENTER);
        topPanel.add(botLevelLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        boardPanel.setMoveListener(this::handleMove);
        updateAllTexts();
    }

    private JMenuBar createMenuBar() {
        menuBar = new JMenuBar();
        homeMenu = new JMenu(messages.getString("menu.home"));
        newGameItem = new JMenuItem(messages.getString("menu.new_game"));
        quitItem = new JMenuItem(messages.getString("menu.quit"));
        newGameItem.addActionListener(e -> onNewGame());
        quitItem.addActionListener(e -> onQuit());
        homeMenu.add(newGameItem);
        homeMenu.add(quitItem);
        helpMenu = new JMenu(messages.getString("menu.help"));
        infosItem = new JMenuItem(messages.getString("menu.infos"));
        infosItem.addActionListener(e -> onInfos());
        helpMenu.add(infosItem);
        languageMenu = new JMenu(messages.getString("menu.language"));
        englishItem = new JMenuItem(messages.getString("menu.language_english"));
        frenchItem = new JMenuItem(messages.getString("menu.language_french"));
        englishItem.addActionListener(e -> switchLanguage(Locale.ENGLISH));
        frenchItem.addActionListener(e -> switchLanguage(Locale.FRENCH));
        languageMenu.add(englishItem);
        languageMenu.add(frenchItem);
        // Bot level menu
        botLevelMenu = createBotLevelMenu();
        menuBar.add(homeMenu);
        menuBar.add(botLevelMenu);
        menuBar.add(languageMenu);
        // add a bar menu separator between language and help menu
        menuBar.add(Box.createHorizontalStrut(30));
        menuBar.add(helpMenu);
        return menuBar;
    }

    /**
     * Creates the bot level menu and configures bot selection radio buttons.
     * To add a new bot, add a new radio button and update the logic here.
     */
    private JMenu createBotLevelMenu() {
        JMenu botMenu = new JMenu(messages.getString("menu.bot_level"));
        randomBotItem = new JRadioButtonMenuItem("");
        minimaxBotItem = new JRadioButtonMenuItem("");
        minimaxPlusBotItem = new JRadioButtonMenuItem("");
        botLevelGroup = new ButtonGroup();
        botLevelGroup.add(randomBotItem);
        botLevelGroup.add(minimaxBotItem);
        botLevelGroup.add(minimaxPlusBotItem);
        // Set initial selection
        if (bot instanceof com.fcuillandre.chessbot.bot.RandomChessBot) {
            randomBotItem.setSelected(true);
        } else if (bot instanceof MinimaxChessBot) {
            minimaxBotItem.setSelected(true);
        } else {
            minimaxPlusBotItem.setSelected(true);
        }

        randomBotItem.addActionListener(e -> onBotLevelChange(RANDOM));
        minimaxBotItem.addActionListener(e -> onBotLevelChange(MINIMAX));
        minimaxPlusBotItem.addActionListener(e -> onBotLevelChange(MINIMAX_PLUS));
        botMenu.add(randomBotItem);
        botMenu.add(minimaxBotItem);
        botMenu.add(minimaxPlusBotItem);
        return botMenu;
    }

    private void updateBotLevelLabel() {
        String levelKey = "label.level";
        String levelStars = "";
        if (bot instanceof RandomChessBot) {
            levelStars = RANDOM_LEVEL_STARS;
        } else if (bot instanceof MinimaxChessBot) {
            levelStars = MINIMAX_LEVEL_STARS;
        } else if (bot instanceof MinimaxPlusChessBot) {
            levelStars = MINIMAX_PLUS_LEVEL_STARS;
        }
        botLevelLabel.setText(messages.getString(levelKey) + ": " + levelStars);
        botLevelLabel.setFont(SEGOE_FONT_BOLD_16);
        botLevelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        botLevelLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
    }

    private void switchLanguage(Locale locale) {
        currentLocale = locale;
        messages = ResourceBundle.getBundle("messages", currentLocale);
        updateAllTexts();
    }

    private void onBotLevelChange(String level) {
        int result = JOptionPane.showConfirmDialog(this,
                messages.getString("dialog.new_game_confirm"),
                messages.getString("dialog.new_game_title"),
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            switch (level) {
                case MINIMAX_PLUS:
                    bot = new MinimaxPlusChessBot();
                    minimaxPlusBotItem.setSelected(true);
                    break;
                case MINIMAX:
                    bot = new MinimaxChessBot();
                    minimaxBotItem.setSelected(true);
                    break;
                case RANDOM:
                    bot = new RandomChessBot();
                    randomBotItem.setSelected(true);
                    break;
            }

            ChessGame newGame = new ChessGame();
            boardPanel.setMoveListener(null);
            getContentPane().remove(boardPanel);
            ChessBoardPanel newBoardPanel = new ChessBoardPanel(this, newGame);
            newBoardPanel.setPreferredSize(new Dimension(600, 600));
            add(newBoardPanel, BorderLayout.CENTER);
            this.moveListModel.clear();
            this.turnLabel.setText("");
            this.gameOver = false;
            this.boardPanel.setVisible(false);
            this.boardPanel.removeAll();
            this.game = newGame;
            this.boardPanel = newBoardPanel;
            newBoardPanel.setMoveListener(this::handleMove);
            revalidate();
            repaint();
            updateTurnLabel();
            updateBotLevelLabel();
        }
    }

    private void updateAllTexts() {
        setTitle(messages.getString("app.title"));
        homeMenu.setText(messages.getString("menu.home"));
        newGameItem.setText(messages.getString("menu.new_game"));
        quitItem.setText(messages.getString("menu.quit"));
        helpMenu.setText(messages.getString("menu.help"));
        infosItem.setText(messages.getString("menu.infos"));
        languageMenu.setText(messages.getString("menu.language"));
        englishItem.setText(messages.getString("menu.language_english"));
        frenchItem.setText(messages.getString("menu.language_french"));
        botLevelMenu.setText(messages.getString("menu.bot_level"));
        randomBotItem.setText(RANDOM_LEVEL_STARS + " " + messages.getString("menu.bot_random"));
        minimaxBotItem.setText(MINIMAX_LEVEL_STARS + " " + messages.getString("menu.bot_moderate"));
        minimaxPlusBotItem.setText(MINIMAX_PLUS_LEVEL_STARS + " " + messages.getString("menu.bot_moderate_plus"));
        // Update selection
        if (bot instanceof com.fcuillandre.chessbot.bot.RandomChessBot) {
            randomBotItem.setSelected(true);
        } else {
            minimaxBotItem.setSelected(true);
        }
        updateTurnLabel();
        updateBotLevelLabel();
        refreshMoveList();
    }

    private void onNewGame() {
        int result = JOptionPane.showConfirmDialog(this,
                messages.getString("dialog.new_game_confirm"),
                messages.getString("dialog.new_game_title"),
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            ChessGame newGame = new ChessGame();
            boardPanel.setMoveListener(null);
            getContentPane().remove(boardPanel);
            ChessBoardPanel newBoardPanel = new ChessBoardPanel(this, newGame);
            newBoardPanel.setPreferredSize(new Dimension(600, 600));
            add(newBoardPanel, BorderLayout.CENTER);
            this.moveListModel.clear();
            this.turnLabel.setText("");
            this.gameOver = false;
            this.boardPanel.setVisible(false);
            this.boardPanel.removeAll();

            this.game = newGame;
            this.boardPanel = newBoardPanel;

            newBoardPanel.setMoveListener(this::handleMove);
            revalidate();
            repaint();
            updateTurnLabel();
        }
    }

    private void onQuit() {
        int result = JOptionPane.showConfirmDialog(this,
                messages.getString("dialog.quit_confirm"),
                messages.getString("dialog.quit_title"),
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void onInfos() {
        displayMessage(this,
                messages.getString("dialog.about"),
                messages.getString("menu.infos"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleMove(Coordinate from, Coordinate to) {
        if (gameOver) return;
        Move move = new Move(from, to);
        if (game.isValidMove(move)) {
            game.makeMove(move);

            MovedPiece lastMovedPiece = game.getLastMove();

            if (game.isCheckmate()) {
                lastMovedPiece.setCheckmate(true);
                endGameStatus = EndGameStatus.CHECKMATE;
                gameOver = true;
                refreshAll();
                displayMessage(this,
                        MessageFormat.format(messages.getString("dialog.checkmate"), messages.getString(game.isWhiteTurn() ? "piece.white" : "piece.black")),
                        messages.getString("dialog.checkmate_title"),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (game.isStalemate()) {
                lastMovedPiece.setStalemate(true);
                endGameStatus = EndGameStatus.STALEMATE;
                gameOver = true;
                refreshAll();
                displayMessage(this,
                        messages.getString("dialog.stalemate"),
                        messages.getString("dialog.stalemate_title"),
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (!game.isWhiteTurn()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ignored) {
                    }
                    if (gameOver) return;
                    Move botMove = bot.getMove(game);
                    if (botMove != null) {
                        SwingUtilities.invokeLater(() -> {
                            game.makeMove(botMove);

                            if (game.isCheckmate()) {
                                lastMovedPiece.setCheckmate(true);
                                endGameStatus = EndGameStatus.CHECKMATE;
                                gameOver = true;
                                refreshAll();
                                displayMessage(this,
                                        MessageFormat.format(messages.getString("dialog.checkmate"), messages.getString(game.isWhiteTurn() ? "piece.white" : "piece.black")),
                                        messages.getString("dialog.checkmate_title"),
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                            if (game.isStalemate()) {
                                lastMovedPiece.setStalemate(true);
                                endGameStatus = EndGameStatus.STALEMATE;
                                gameOver = true;
                                refreshAll();
                                displayMessage(this,
                                        messages.getString("dialog.stalemate"),
                                        messages.getString("dialog.stalemate_title"),
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                            refreshAll();
                        });
                    }
                }).start();
            }
        } else {
            if (game.isKingInCheck()) {
                displayMessage(this,
                        messages.getString("dialog.king_still_in_check"),
                        messages.getString("dialog.invalid_move_title"),
                        JOptionPane.ERROR_MESSAGE);
            } else {
                displayMessage(this,
                        messages.getString("dialog.invalid_move"),
                        messages.getString("dialog.invalid_move_title"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        Move lastMove = new Move(game.getLastMove().getStart(), game.getLastMove().getEnd());
        if (lastMove != null) {
            MovedPiece lastMovedPiece = game.getLastMove();

            int endX = lastMove.getEnd().getX();
            int endY = lastMove.getEnd().getY();
            ChessPiece promotedPawn = game.getBoard().getPieceAt(endX, endY);
            if (promotedPawn != null && promotedPawn.getType() == ChessPieceType.PAWN) {
                if (promotedPawn.getColor() == ChessColor.WHITE && endX == 7) {
                    String[] optionNames = {
                            messages.getString("piece.queen"),
                            messages.getString("piece.rook"),
                            messages.getString("piece.bishop"),
                            messages.getString("piece.knight")
                    };
                    int choice = JOptionPane.showOptionDialog(this,
                            messages.getString("dialog.pawn_promotion"),
                            messages.getString("dialog.pawn_promotion_title"),
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            optionNames,
                            optionNames[0]);
                    if (choice >= 0 && choice < PROMOTION_OPTIONS.length) {
                        game.promotePawn(new com.fcuillandre.chessbot.game.Coordinate(endX, endY), PROMOTION_OPTIONS[choice]);
                        //refresh();
                        lastMovedPiece.setPromotionPieceType(PROMOTION_OPTIONS[choice]);
                    }
                } else if (promotedPawn.getColor() == com.fcuillandre.chessbot.pieces.ChessColor.BLACK && endX == 0) {
                    int idx = (int) (Math.random() * PROMOTION_OPTIONS.length);
                    game.promotePawn(new com.fcuillandre.chessbot.game.Coordinate(endX, endY), PROMOTION_OPTIONS[idx]);
                    //refresh();
                    lastMovedPiece.setPromotionPieceType(PROMOTION_OPTIONS[idx]);
                }
            }
        }
        // Update move list and turn label

        refreshAll();
    }

    private void refreshAll() {
        if (game.isKingInCheck()) {
            game.getLastMove().setCheck(true);
            updateTurnLabelWithCheck();
        } else {
            updateTurnLabel();
        }
        refreshMoveList();
        refresh();
    }

    private void displayMessage(Component parentComponent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
    }

    private void refreshMoveList() {
        moveListModel.clear();
        for (int i = 0; i < game.getMoveHistory().size(); i += 2) {
            com.fcuillandre.chessbot.game.MovedPiece whiteMove = game.getMoveHistory().get(i);
            com.fcuillandre.chessbot.game.MovedPiece blackMove = (i + 1 < game.getMoveHistory().size()) ? game.getMoveHistory().get(i + 1) : null;
            if (whiteMove == null && blackMove == null) continue;
            String line = this.getIndex(i) + ". ";
            if (whiteMove != null) {
                line += ChessMoveFormatterUtils.formatMove(whiteMove);
            }
            if (blackMove != null) {
                line += " " + ChessMoveFormatterUtils.formatMove(blackMove);
            }
            moveListModel.addElement(line);
        }
    }

    private String getIndex(int i) {
        if (i == 0) return "1";
        return String.valueOf((i / 2) + 1);
    }

    private void updateTurnLabel() {
        this.updateTurnLabel("");//messages.getString("label.turn"));
    }

    private void updateTurnLabelWithCheck() {
        this.updateTurnLabel(messages.getString("label.turn_check"));
    }

    private void updateTurnLabel(String turnLabel) {
        if (gameOver) {
            String msg = "";
            switch (endGameStatus) {
                case STALEMATE -> msg = messages.getString("dialog.stalemate");
                case CHECKMATE -> msg = messages.getString("dialog.checkmate");
                default -> msg = "";
            }
            this.turnLabel.setText(MessageFormat.format(msg, messages.getString(game.isWhiteTurn() ? "piece.white" : "piece.black")));
        } else {
            this.turnLabel.setText(MessageFormat.format(turnLabel, messages.getString(game.isWhiteTurn() ? "piece.white" : "piece.black")));
        }
        this.turnLabel.setFont(ARIAL_FONT_BOLD_16);
        this.turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void refresh() {
        boardPanel.updateBoard(game.getBoard());
    }
}
