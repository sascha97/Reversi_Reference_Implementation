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
    private List<GamePosition> gamePositionList;

    //The configuration file of the ReversiGame
    private ReversiGameConfiguration config = ReversiGameConfiguration.getInstance();

    private Player humanPlayer;

    /**
     * Constructor of the ReversiGame.
     *
     * @param actorsPair The actors responsible for playing the game.
     */
    public ReversiGame(ActorsPair actorsPair) {
        super(actorsPair);

        //Create empty game position list.
        gamePositionList = new ArrayList<>();

        //Get the color that the human player wants to play
        String humanColor = config.getProperty(ReversiGameConfiguration.HUMAN_PLAYER_COLOR, "black");

        //Set the human player to the right color
        if (humanColor.toLowerCase().contains("black")) {
            actorsPair.setHumanPlayer(Player.BLACK);
            humanPlayer = Player.BLACK;
        } else {
            actorsPair.setHumanPlayer(Player.WHITE);
            humanPlayer = Player.WHITE;
        }

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

                System.out.println("Removed");
            }

            //Remove the last human move
            this.gamePositionList.remove(gamePositionList.size() - 1); //own move

            //Get the position before the last human move has been made.
            GamePosition currentGamePosition = this.gamePositionList.get(gamePositionList.size() - 1);

            //Set the GamePosition as current game position
            setGamePosition(currentGamePosition);
        }
    }
}
