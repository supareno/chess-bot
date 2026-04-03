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

import com.fcuillandre.chessbot.model.ChessBoard;
import com.fcuillandre.chessbot.model.ChessColor;
import com.fcuillandre.chessbot.model.Move;
import com.fcuillandre.chessbot.model.ChessPieceType;
import com.fcuillandre.chessbot.engine.GameEngine;
import com.fcuillandre.chessbot.engine.GameListener;
import com.fcuillandre.chessbot.engine.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * Panel displaying move history.
 *
 * @author fcuillandre
 * @since 0.1
 */
public class MoveHistoryPanel extends JPanel implements GameListener {

    private final JTextArea historyArea;
    private final JLabel titleLabel;
    private final GameEngine gameEngine;
    private int moveNumber;

    public MoveHistoryPanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.moveNumber = 1;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 0));

        titleLabel = new JLabel(Messages.get("history.title"), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        gameEngine.addGameListener(this);
    }

    public void clear() {
        historyArea.setText("");
        moveNumber = 1;
    }

    /** Re-applies translated labels after a locale change. */
    public void refreshLabels() {
        titleLabel.setText(Messages.get("history.title"));
    }

    @Override
    public void onBoardChanged(ChessBoard board) {
        // Nothing to do here
    }

    @Override
    public void onGameStateChanged(GameState state, ChessColor currentPlayer) {
        if (state == GameState.CHECKMATE) {
            historyArea.append("\n" + Messages.get(state.getMessageKey()) + "! ");
            historyArea.append(currentPlayer.opposite() == ChessColor.WHITE
                    ? Messages.get("history.whites.win")
                    : Messages.get("history.blacks.win"));
        } else if (state.isGameOver()) {
            historyArea.append("\n" + Messages.get(state.getMessageKey()));
        }
    }

    @Override
    public void onMoveMade(Move move) {
        String moveNotation = formatMove(move);

        if (move.getPiece().getColor() == ChessColor.WHITE) {
            historyArea.append(moveNumber + ". " + moveNotation + " ");
        } else {
            historyArea.append(moveNotation + "\n");
            moveNumber++;
        }

        // Auto-scroll
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    private String formatMove(Move move) {
        StringBuilder sb = new StringBuilder();

        if (move.isCastling()) {
            if (move.getTo().col() > move.getFrom().col()) {
                sb.append("O-O");
            } else {
                sb.append("O-O-O");
            }
        } else {
            ChessPieceType type = move.getPiece().getType();
            if (type != ChessPieceType.PAWN) {
                sb.append(type.getNotation());
            }

            if (move.isCapture()) {
                if (type == ChessPieceType.PAWN) {
                    sb.append(move.getFrom().toAlgebraic().charAt(0));
                }
                sb.append("x");
            }

            sb.append(move.getTo().toAlgebraic());

            if (move.isPromotion()) {
                sb.append("=").append(move.getPromotionType().getNotation());
            }
        }

        GameState state = gameEngine.getGameState();
        if (state == GameState.CHECKMATE) {
            sb.append("#");
        } else if (state == GameState.CHECK) {
            sb.append("+");
        }

        return sb.toString();
    }
}
