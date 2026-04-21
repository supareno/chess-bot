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

import com.fcuillandre.chessbot.model.ChessColor;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog to configure a new game.
 *
 * @author fcuillandre
 * @since 1.0
 */
public class NewGameDialog extends JDialog {

    private boolean confirmed;
    private boolean vsBot;
    private ChessColor playerColor;
    private int botDifficulty;
    
    private JRadioButton vsBotRadio;
    private JRadioButton vsHumanRadio;
    private JRadioButton whiteRadio;
    private JRadioButton blackRadio;
    private JComboBox<String> difficultyCombo;
    private JPanel colorPanel;
    private JPanel difficultyPanel;

    /**
     * Creates the new game dialog.
     * @param parent the parent frame for centering the dialog
     */
    public NewGameDialog(JFrame parent) {
        super(parent, Messages.get("newgame.title"), true);

        this.confirmed = false;
        this.vsBot = true;
        this.playerColor = ChessColor.WHITE;
        this.botDifficulty = 3;
        
        initComponents();
        
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Game mode section
        JPanel modePanel = createModePanel();
        mainPanel.add(modePanel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Color choice section
        colorPanel = createColorPanel();
        mainPanel.add(colorPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Difficulty section
        difficultyPanel = createDifficultyPanel();
        mainPanel.add(difficultyPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createModePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(Messages.get("newgame.mode.title")));

        vsBotRadio = new JRadioButton(Messages.get("newgame.mode.vsbot"), true);
        vsHumanRadio = new JRadioButton(Messages.get("newgame.mode.vshuman"));

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(vsBotRadio);
        modeGroup.add(vsHumanRadio);
        
        vsBotRadio.addActionListener(e -> updatePanelsVisibility());
        vsHumanRadio.addActionListener(e -> updatePanelsVisibility());
        
        panel.add(vsBotRadio);
        panel.add(vsHumanRadio);
        
        return panel;
    }
    
    private JPanel createColorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(Messages.get("newgame.color.title")));

        whiteRadio = new JRadioButton(Messages.get("newgame.color.white"), true);
        blackRadio = new JRadioButton(Messages.get("newgame.color.black"));

        whiteRadio.setFont(ChessAppFontConstants.SEGOE_UI_SYMBOL_FONT_PLAIN_14);
        blackRadio.setFont(ChessAppFontConstants.SEGOE_UI_SYMBOL_FONT_PLAIN_14);
        
        ButtonGroup colorGroup = new ButtonGroup();
        colorGroup.add(whiteRadio);
        colorGroup.add(blackRadio);
        
        panel.add(whiteRadio);
        panel.add(blackRadio);
        
        return panel;
    }
    
    private JPanel createDifficultyPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder(Messages.get("newgame.difficulty.title")));

        String[] difficulties = {
                Messages.get("newgame.difficulty.easy"),
                Messages.get("newgame.difficulty.medium"),
                Messages.get("newgame.difficulty.hard"),
                Messages.get("newgame.difficulty.expert")
        };
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setSelectedIndex(1); // Medium by default

        panel.add(new JLabel(Messages.get("newgame.difficulty.level")));
        panel.add(difficultyCombo);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton startButton = new JButton(Messages.get("newgame.button.start"));
        startButton.setPreferredSize(new Dimension(120, 35));
        startButton.addActionListener(e -> {
            confirmed = true;
            vsBot = vsBotRadio.isSelected();
            playerColor = whiteRadio.isSelected() ? ChessColor.WHITE : ChessColor.BLACK;
            botDifficulty = difficultyCombo.getSelectedIndex() + 2; // 2-5
            dispose();
        });
        
        JButton cancelButton = new JButton(Messages.get("newgame.button.cancel"));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        panel.add(startButton);
        panel.add(cancelButton);
        
        // Allow confirmation with Enter
        getRootPane().setDefaultButton(startButton);
        
        return panel;
    }
    
    private void updatePanelsVisibility() {
        boolean isBotMode = vsBotRadio.isSelected();
        colorPanel.setVisible(isBotMode);
        difficultyPanel.setVisible(isBotMode);
        pack();
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public boolean isVsBot() {
        return vsBot;
    }
    
    public ChessColor getPlayerColor() {
        return playerColor;
    }
    
    public int getBotDifficulty() {
        return botDifficulty;
    }
}
