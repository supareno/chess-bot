package com.fcuillandre.chessbot;

import com.fcuillandre.chessbot.bot.ChessBot;
import com.fcuillandre.chessbot.game.ChessGame;
import com.fcuillandre.chessbot.utils.ChessUtils;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        ChessBot bot = new ChessBot();

        Scanner scanner = new Scanner(System.in);

        //game.startGame();

        new com.fcuillandre.chessbot.ui.ChessGameFrame(game);

        /*while (!game.isGameOver()) {
            //bot.makeMove(game);
            game.displayBoard();


            String toPlay  = game.isWhiteTurn() ? "White" : "Black";

            ChessUtils.log(toPlay + " to play: enter your move (e.g., e2 e4): ");
            String userInput = scanner.nextLine();
            String[] move = userInput.split(" ");
            if (move.length != 2 || !isValidMove(userInput)) {
                ChessUtils.log("Invalid move format. Please use PGN notation (e.g., e2 e4 or D2 D4).");
                continue;
            }
            game.makeMove(move[0], move[1]);
        }

        scanner.close();

        ChessUtils.log("Game Over!");
        */

    }

    // private method to check if the move is a valid move that respect pgn notation
    private static boolean isValidMove(String move) {
        // Implement PGN validation logic here
        // This is a placeholder for the actual implementation
        return move.matches("[a-hA-H][1-8] [a-hA-H][1-8]");
    }
}