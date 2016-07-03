/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.ui;

import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.GamePosition;
import reversi.board.Square;
import reversi.board.SquareState;
import reversi.game.Game;
import reversi.player.Player;

import java.util.List;
import java.util.Observable;
import java.util.Scanner;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 03. July 2016
 */
public class ConsoleGameView extends GameView {
    private ThreadEvent resultsReady = new ThreadEvent();
    private String userInput;

    public ConsoleGameView(Game gameModel) {
        super(gameModel);

        inputThread();
    }

    private void inputThread() {
        Thread inputThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    userInput = scanner.nextLine().trim().toLowerCase();

                    if (userInput.contains("newgame")) {
                        gameModel.startNewGame();
                        gameModel.play();
                    } else {
                        resultsReady.signal();
                    }
                }
            }
        };

        inputThread.start();
    }

    @Override
    protected String requestUserInput() throws InterruptedException {
        System.out.println(RES.getString("ui.message.request.input"));
        resultsReady.await();

        return userInput;
    }

    @Override
    protected void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    protected void showAllLegalMoves() {
        GamePosition gamePosition = gameModel.getGamePosition();
        Player currentPlayer = gamePosition.getCurrentPlayer();
        List<GameMove> legalMoves = gamePosition.getBoard().getAllLegalMoves(currentPlayer);

        boolean first = true;
        StringBuilder moves = new StringBuilder();

        for (GameMove move : legalMoves) {
            if (first) {
                first = false;
            } else {
                moves.append(",");
            }

            moves.append(move.getSquare().getSquareName());
        }
        System.out.printf(RES.getString("ui.label.possible.moves"), moves.toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            Board board = gameModel.getGamePosition().getBoard();
            printBoard(board);
        } else {
            System.out.println(arg);
        }
    }

    private void printBoard(Board board) {
        int boardWidth = board.getBoardWidth();
        int boardHeight = board.getBoardHeight();

        System.out.print(" ");
        for (int i = 0; i < boardWidth; i++) {
            char c = (char) ('A' + i);
            System.out.printf("%2c", c);
        }
        System.out.println();
        for (int y = 0; y < boardWidth; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < boardHeight; x++) {
                Square square = board.getSquare(x, y);

                String s = "";
                if (square.getSquareState() == SquareState.BLACK) {
                    s = "B";
                } else if (square.getSquareState() == SquareState.WHITE) {
                    s = "W";
                }

                System.out.printf("%-2s", s);
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
