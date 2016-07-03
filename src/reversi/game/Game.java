/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
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
    protected Game(ActorsPair actorsPair) {
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
        //creates new game thread and starts the new game threa
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
     * @return The current GamePosiion of the game.
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
     * This method has to be called if the game has changed and the Obervers should be notified.
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

        //Get the actor who is repsonsible for making the moves
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

    //This class is responsible for making the moves without in another thread.
    private class GameThread extends Thread {
        @Override
        public void run() {
            //if the game is not interrupted or the game has legal moves
            while (!isInterrupted() && hasGameAnyLegalMoves()) {
                //notify the observers that something has changed
                gameHasChanged();

                try {
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
