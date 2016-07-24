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
 *   for using the code in any commercial projects from the author.
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
package reversi.game;

import reversi.actor.Actor;
import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.GamePosition;
import reversi.player.ActorsPair;
import reversi.player.Player;

import java.util.Observable;

/**
 * This is the abstract base class representing a game. This class takes already care of the thread handling.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public abstract class Game extends Observable {
    //The Actors of the Game.
    private final ActorsPair ACTORS_PAIR;
    //The GameThread
    private GameThread gameThread;

    /**
     * Constructor for Game.
     *
     * @param actorsPair The actors of the game.
     */
    Game(ActorsPair actorsPair) {
        //Create a new GameThread
        gameThread = new GameThread();
        ACTORS_PAIR = actorsPair;
    }

    /**
     * This method returns the number of pieces of a given player.
     *
     * @param player The player whose pieces should be counted.
     * @return The number of pieces of the given player.
     */
    public final int getNumberOfPieces(Player player) {
        //Get the board and then count the pieces of the board.
        Board board = getGamePosition().getBoard();

        return board.countPieces(player);
    }

    /**
     * This method checks if game has any legal moves.
     *
     * @return true if game has any legal moves - false if game has no legal moves.
     */
    public final boolean hasGameAnyLegalMoves() {
        //Get the board and then check if game has legal moves
        Board board = getGamePosition().getBoard();

        return board.hasAnyPlayerAnyLegalMoves();
    }

    /**
     * This method has to be called if the game should be played.
     */
    public final void play() {
        //creates new game thread and starts the new game thread
        gameThread = new GameThread();
        gameThread.start();
    }

    /**
     * This method has to be called if the game should be ended.
     */
    public final void endGame() {
        //Wait for the current game to interrupt.
        interruptGameAndWaitForFinish();

        //Notify all the Observers that the Game has changed.
        gameHasChanged();
    }

    /**
     * This method sets up a new game
     */
    public final void startNewGame() {
        //Wait for the current game to interrupt.
        interruptGameAndWaitForFinish();

        //Determine human player and computer player
        Player humanPlayer = determineHumanPlayer();
        Player computerPlayer = humanPlayer.getOpponent();

        Actor computerActor = getComputerActor();

        //Set up the human player if the configuration was changed after the game was loaded set the human player now
        ACTORS_PAIR.setHumanPlayer(humanPlayer);

        if (computerActor != null) {
            ACTORS_PAIR.setActor(computerPlayer, computerActor);

            setChanged();
            notifyObservers(computerActor);
        }

        //refresh the actors
        ACTORS_PAIR.refreshAllActors();

        //Call the method on start new game so that subclasses are able to handle start game stuff.
        onStartNewGame();
    }

    /**
     * This method has to be called if a move should be taken back.
     */
    public final void takeBackMove() {
        //Wait for the current game to interrupt.
        interruptGameAndWaitForFinish();

        //Call the method onTakeBackMove() so that subclasses are able to handle take back stuff.
        onTakeBackMove();
        //call play again that the game is resumed
        play();
    }

    /**
     * This method returns the current game position.
     *
     * @return The current GamePosition of the game.
     */
    public abstract GamePosition getGamePosition();

    /**
     * This method sets the game position to a new game position done in a subclass
     *
     * @param gamePosition The new GamePosition of the game.
     */
    protected abstract void setGamePosition(GamePosition gamePosition);

    /**
     * This method has to be implemented by all subclasses to handle stuff that should be done when the game starts.
     */
    protected abstract void onStartNewGame();

    /**
     * This method has to be implemented by all subclasses to handle stuff that should be done when a move
     * is taken back.
     */
    protected abstract void onTakeBackMove();

    /**
     * This method returns the comptuer actor of the game
     *
     * @return the computer actor
     */
    protected abstract Actor getComputerActor();

    /**
     * This method has to be called if the game has changed and the Observers should be notified.
     */
    private void gameHasChanged() {
        //Set the state changed
        setChanged();
        //Notify the observers.
        notifyObservers();
    }

    /**
     * This method is responsible for making the moves.
     *
     * @throws InterruptedException if the thread is interrupted while making a move.
     */
    private void makeMove() throws InterruptedException {
        //Get the current GamePosition
        GamePosition currentGamePosition = getGamePosition();
        //Get the Current player
        Player currentPlayer = currentGamePosition.getCurrentPlayer();

        //Get the actor who is responsible for making the moves
        Actor actor = ACTORS_PAIR.getActor(currentPlayer);

        //If Thread is interrupted end the method.
        if (gameThread.isInterrupted()) {
            return;
        }

        //Get the move of the actor.
        GameMove move = actor.getStrategy().move(getGamePosition());

        //If gameThread is interrupted now end the method again
        if (gameThread.isInterrupted()) {
            return;
        }

        //make the move if the move is valid
        if (getGamePosition().getBoard().isMoveLegal(move, currentPlayer)) {
            GamePosition newGamePosition = getGamePosition().getBoard().makeMove(move, currentPlayer);
            setGamePosition(newGamePosition);
        }
    }

    /**
     * This method is responsible for interrupting a game and wait until it is interrupted.
     */
    private void interruptGameAndWaitForFinish() {
        //If the game is not already been interrupted interrupt the game
        if (!gameThread.isInterrupted()) {
            gameThread.interrupt();
            try {
                //wait for the thread to finish.
                gameThread.join();
            } catch (InterruptedException ie) {
                //print out the error messages to the console
                ie.printStackTrace();
            }
        }
    }

    /**
     * This method is responsible for determining which player is the human player.
     *
     * @return The player that represents the human player.
     */
    Player determineHumanPlayer() {
        //The human player
        Player humanPlayer;

        //Get the configuration
        ReversiGameConfiguration config = ReversiGameConfiguration.getInstance();
        //Get the color that the human player wants to play
        String humanColor = config.getProperty(ReversiGameConfiguration.HUMAN_PLAYER_COLOR, "black");

        //Set the human player to the right color
        if (humanColor.toLowerCase().contains("black")) {
            humanPlayer = Player.BLACK;
        } else {
            humanPlayer = Player.WHITE;
        }

        return humanPlayer;
    }

    //This class is responsible for making the moves without in another thread.
    private class GameThread extends Thread {
        @Override
        public void run() {
            //if the game is not interrupted or the game has legal moves
            while (!isInterrupted() && hasGameAnyLegalMoves()) {
                //notify the observers that something has changed
                gameHasChanged();

                try {
                    //Thread wait for a second until next move will be made
                    Thread.sleep(1000);

                    //Try to make the move
                    makeMove();
                } catch (InterruptedException e) {
                    //if interrupted interrupt thread again...
                    interrupt();
                }
            }

            //Display the update only if the thread was not interrupted
            if (!isInterrupted()) {
                //Notify the observers again
                gameHasChanged();
            }
        }
    }
}
