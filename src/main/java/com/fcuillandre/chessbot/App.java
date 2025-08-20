package com.fcuillandre.chessbot;

import com.fcuillandre.chessbot.game.ChessGame;

public class App {

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        //ChessBot bot = new ChessBot();
        new com.fcuillandre.chessbot.ui.ChessGameFrame(game);
    }
}