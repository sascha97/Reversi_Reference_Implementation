/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.game;

import reversi.board.GamePosition;
import reversi.board.ReversiBoard;
import reversi.player.ActorsPair;
import reversi.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the concrete implementation of a ReversiGame
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public class ReversiGame extends Game {
    //The current game position
    private GamePosition currentGamePosition;

    //The list containing all game positions
    private final List<GamePosition> gamePositionList;

    //The humanPlayer field
    private final Player humanPlayer;

    /**
     * Constructor of the ReversiGame.
     *
     * @param actorsPair The actors responsible for playing the game.
     */
    public ReversiGame(ActorsPair actorsPair) {
        super(actorsPair);

        //Create empty game position list.
        gamePositionList = new ArrayList<>();

        //Set the human player
        humanPlayer = determineHumanPlayer();

        startNewGame();
    }

    /**
     * This method returns the current GamePosition.
     *
     * @return The current gamePosition.
     */
    @Override
    public GamePosition getGamePosition() {
        return currentGamePosition;
    }

    /**
     * This method sets the game position to a new game position done in a subclass
     *
     * @param gamePosition The new GamePosition of the game.
     */
    @Override
    protected void setGamePosition(GamePosition gamePosition) {
        //get the new game position and add it to the list of game positions if it is not already added
        this.currentGamePosition = gamePosition;
        if (!gamePositionList.contains(gamePosition)) {
            this.gamePositionList.add(gamePosition);
        }
    }

    /**
     * This method handles the stuff that has to be done when a new game should be started.
     */
    @Override
    public void onStartNewGame() {
        //Create a new reversi board
        ReversiBoard reversiBoard = new ReversiBoard();
        //clear the list so that all previous game positions will be deleted
        gamePositionList.clear();
        //create a new GamePosition and set it as current gamePosition
        setGamePosition(new GamePosition(reversiBoard, Player.BLACK));
    }

    /**
     * This method handles the stuff that has to be done when a move should be taken back.
     * <p>
     * The method does not work properly if the computer has done more than one move
     */
    @Override
    public void onTakeBackMove() {
        //Move can only be taken back if at least two moves have been made
        if (this.gamePositionList.size() > 2) {
            //Take multiple computer moves back
            while (gamePositionList.get(gamePositionList.size() - 2).getCurrentPlayer() != humanPlayer) {
                //Remove computer move
                gamePositionList.remove(gamePositionList.size() - 1);
            }

            //Remove the last human move
            this.gamePositionList.remove(gamePositionList.size() - 1); //own move

            //Get the position before the last human move has been made.
            GamePosition currentGamePosition = this.gamePositionList.get(gamePositionList.size() - 1);

            //Set the GamePosition as current game position
            setGamePosition(currentGamePosition);
        }
    }

    @Override
    public String toString() {
        return "ReversiGame{" +
                "humanPlayer=" + humanPlayer +
                ", currentGamePosition=" + currentGamePosition +
                '}';
    }
}
