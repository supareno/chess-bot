package com.fcuillandre.chessbot;

import com.fcuillandre.chessbot.game.ChessGame;

/**
 * Main class to start the Chess application.
 *
 * @author FCuillandre
 * @version 1.0
 */
public class ChessApp {

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        new com.fcuillandre.chessbot.ui.ChessGameFrame(game);
    }
}