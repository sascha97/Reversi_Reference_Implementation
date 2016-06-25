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
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public abstract class Game extends Observable {
    private final ActorsPair ACTORS_PAIR;
    private GameThread gameThread;

    protected Game(ActorsPair actorsPair) {
        gameThread = new GameThread();
        ACTORS_PAIR = actorsPair;
    }

    public abstract GamePosition getGamePosition();

    protected abstract void setGamePosition(GamePosition gamePosition);

    public int getNumberOfPieces(Player player) {
        Board board = getGamePosition().getBoard();

        return board.countPieces(player);
    }

    public boolean hasGameAnyLegalMoves() {
        Board board = getGamePosition().getBoard();

        return board.hasAnyPlayerAnyLegalMoves();
    }

    private void makeMove() throws InterruptedException {
        GamePosition currentGamePosition = getGamePosition();
        Player currentPlayer = currentGamePosition.getCurrentPlayer();

        Actor actor = ACTORS_PAIR.getActor(currentPlayer);

        if (gameThread.isInterrupted()) {
            return;
        }

        GameMove move = actor.getStrategy().move(getGamePosition());

        if (gameThread.isInterrupted()) {
            return;
        }

        if (getGamePosition().getBoard().isMoveLegal(move, currentPlayer)) {
            GamePosition newGamePosition = getGamePosition().getBoard().makeMove(move, currentPlayer);
            setGamePosition(newGamePosition);
        } else {
            setChanged();
            notifyObservers("Your move was illegal please enter anoter one...");
            makeMove();
        }
    }

    public final void play() {
        gameThread = new GameThread();
        gameThread.start();
    }

    public final void endGame() {
        interruptGameAndWaitForFinish();

        gameHasChanged();
    }

    public void startNewGame() {
        interruptGameAndWaitForFinish();
    }

    public void takeBackMove() {
        interruptGameAndWaitForFinish();
    }

    protected final void gameHasChanged() {
        setChanged();
        notifyObservers();
    }

    private void interruptGameAndWaitForFinish() {
        if (!gameThread.isInterrupted()) {
            gameThread.interrupt();
            try {
                gameThread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    private class GameThread extends Thread {
        @Override
        public void run() {
            while (!isInterrupted() && hasGameAnyLegalMoves()) {
                gameHasChanged();

                try {
                    makeMove();
                } catch (InterruptedException e) {
                    interrupt();
                }
            }

            gameHasChanged();
        }
    }
}
