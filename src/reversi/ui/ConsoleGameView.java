/*
 * Copyright (c) 2016. Sascha Lutzenberger. All rights reserved.
 *
 * This file is part of the project "Reversi_Reference_Implementation"
 *
 * Redistribution and use in source and binary forms, without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - The author of this source code has given you the permission to use this
 *   source code.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - The code is not used in commercial projects, except you got the permission
 *   for using the code in any commerical projects from the author.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package reversi.ui;

import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.GamePosition;
import reversi.board.Square;
import reversi.board.SquareState;
import reversi.game.Game;
import reversi.game.ReversiGameConfiguration;
import reversi.player.Player;

import java.util.List;
import java.util.Observable;
import java.util.Scanner;

/**
 * This is an implementation of a GameView for the Console as a user interface.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 03. July 2016
 */
public class ConsoleGameView extends GameView {
    //Used for synchronizing the results over two threads...
    private final ThreadEvent resultsReady = new ThreadEvent();
    //The input of the user.
    private String userInput;

    //The symbols for displaying a player
    private final char WHITE_PLAYER;
    private final char BLACK_PLAYER;
    private final char EMPTY_PLAYER;

    /**
     * Constructor to set up the ConsoleGameView
     *
     * @param gameModel The game model which should be displayed.
     */
    public ConsoleGameView(Game gameModel) {
        super(gameModel);

        ReversiGameConfiguration configuration = ReversiGameConfiguration.getInstance();
        WHITE_PLAYER = configuration.getProperty(ReversiGameConfiguration.PLAYER_WHITE_CHAR, "W").charAt(0);
        BLACK_PLAYER = configuration.getProperty(ReversiGameConfiguration.PLAYER_BLACK_CHAR, "B").charAt(0);
        EMPTY_PLAYER = configuration.getProperty(ReversiGameConfiguration.PLAYER_EMPTY_CHAR, "-").charAt(0);

        //Start an input thread, only purpose of this input thread is to ask for the user input all the time...
        inputThread();
    }

    private void inputThread() {
        //Creates a new Thread that is constantly reading in all input from the console
        Thread inputThread = new Thread() {
            @Override
            public void run() {
                //The scanner to get the input from the console
                Scanner scanner = new Scanner(System.in);

                //if program should be exited set running to false
                boolean running = true;
                //input thread should run until user wants to exit the program
                while (running) {
                    //Store the user input in a variable variable
                    userInput = scanner.nextLine().trim().toLowerCase();

                    //If a new game should be started do so
                    if (userInput.contains(RES.getString("control.new"))) {
                        gameController.handleUserAction(GameController.GameAction.NEW_GAME);
                        //if a game should be resigned do so
                    } else if (userInput.contains(RES.getString("control.resign"))) {
                        gameController.handleUserAction(GameController.GameAction.RESIGN);
                        //if a game should be exited do so
                    } else if (userInput.contains(RES.getString("control.exit"))) {
                        gameController.handleUserAction(GameController.GameAction.END_GAME);
                        displayPlayerStatus();
                        //set running false so that input loop is ended
                        running = false;
                        //if a move should be taken back do so
                    } else if (userInput.contains(RES.getString("control.take.back"))) {
                        gameController.handleUserAction(GameController.GameAction.TAKEBACK);
                    } else {
                        //if no game action should be performed then tell the waiting thread that the input is ready
                        resultsReady.signal();
                    }
                }
            }
        };

        //Start the input thread
        inputThread.start();
    }

    /**
     * This method is used to request any input from the user
     *
     * @return The input from the user
     * @throws InterruptedException If the program is interrupted whilst requesting any user input.
     */
    @Override
    protected String requestUserInput() throws InterruptedException {
        //Display the message to the user
        displayMessage(RES.getString("ui.message.request.input"));
        //wait for the input
        resultsReady.await();

        //return the input
        return userInput;
    }

    /**
     * This method is used to display any messages on the console game view
     *
     * @param message The message which should be displayed
     */
    @Override
    protected void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * This method shows all legal moves on the console
     */
    @Override
    protected void showAllLegalMoves() {
        //Get the current game Position and the current player
        GamePosition gamePosition = gameModel.getGamePosition();
        Player currentPlayer = gamePosition.getCurrentPlayer();
        //Get all legal moves
        List<GameMove> legalMoves = gamePosition.getBoard().getAllLegalMoves(currentPlayer);

        //build the string
        boolean first = true;
        StringBuilder moves = new StringBuilder();
        for (GameMove move : legalMoves) {
            //add the comma as a separator
            if (first) {
                first = false;
            } else {
                moves.append(",");
            }

            //Append the next square name to the string
            moves.append(move.getSquare().getSquareName());
        }

        //Create a formatted string and display the message
        String message = String.format(RES.getString("ui.label.possible.moves"), moves.toString());
        displayMessage(message);
    }

    /**
     * This method is called when the observed object has changed.
     *
     * @param o   The Observable that has changed
     * @param arg An argument
     */
    @Override
    public void update(Observable o, Object arg) {
        //Print the current board
        Board board = gameModel.getGamePosition().getBoard();
        printBoard(board);

        //if game is finished display the statistics to the user
        if (!gameModel.hasGameAnyLegalMoves()) {
            //display the status of all players
            displayPlayerStatus();
        }
    }

    private void displayPlayerStatus() {
        //get the white player status from the resource bundle and insert the number of white disks
        String whitePlayerStatus = String.format(RES.getString("ui.label.player.white"),
                gameModel.getNumberOfPieces(Player.WHITE));
        //get the black player status from the resource bundle and insert the number of black disks
        String blackPlayerStatus = String.format(RES.getString("ui.label.player.black"),
                gameModel.getNumberOfPieces(Player.BLACK));

        //Display the white player status
        displayMessage(whitePlayerStatus);
        //display the black player status
        displayMessage(blackPlayerStatus);
    }

    /**
     * This method is responsible for printing out a board.
     *
     * @param board The board that should be printed
     */
    private void printBoard(Board board) {
        //Get the width and height of a board
        int boardWidth = board.getBoardWidth();
        int boardHeight = board.getBoardHeight();

        //Print out the letters on the top row
        System.out.print(" ");
        for (int i = 0; i < boardWidth; i++) {
            char c = (char) ('A' + i);
            System.out.printf("%2c", c);
        }
        System.out.println();
        //Print the numbers on the left of the board
        for (int y = 0; y < boardWidth; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < boardHeight; x++) {
                //Get the square
                Square square = board.getSquare(x, y);

                //only non empty squares will be printed.
                char s = EMPTY_PLAYER;
                if (square.getSquareState() == SquareState.BLACK) {
                    s = BLACK_PLAYER;
                } else if (square.getSquareState() == SquareState.WHITE) {
                    s = WHITE_PLAYER;
                }

                System.out.printf("%-2c", s);
            }
            System.out.println();
        }
    }

    private static class ThreadEvent {
        private final Object lock = new Object();

        void await() throws InterruptedException {
            synchronized (lock) {
                lock.wait();
            }
        }

        void signal() {
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}
