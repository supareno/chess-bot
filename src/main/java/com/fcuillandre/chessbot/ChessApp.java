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
package com.fcuillandre.chessbot;

import com.fcuillandre.chessbot.gui.ChessGUI;
import com.fcuillandre.chessbot.utils.ChessAppUtils;

import javax.swing.*;

/**
 * Entrypoint for the Chess application.
 * Initializes the GUI and sets the Look and Feel to match the system's native appearance.
 *
 * @author fcuillandre
 * @version 0.1
 */
public class ChessApp {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ChessAppUtils.log(e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            new ChessGUI();
        });
        //
        ChessAppUtils.log("""
                      
                       .__                                               \s
                  ____ |  |__   ____   ______ ___________  ______ ______ \s
                _/ ___\\|  |  \\_/ __ \\ /  ___//  ___/\\__  \\ \\____ \\\\____ \\\s
                \\  \\___|   Y  \\  ___/ \\___ \\ \\___ \\  / __ \\|  |_> >  |_> >
                 \\___  >___|  /\\___  >____  >____  >(____  /   __/|   __/\s
                     \\/     \\/     \\/     \\/     \\/      \\/|__|   |__|   \s
                
                Made with love by fcuillandre
                Powered by Java
                """);

    }
}
