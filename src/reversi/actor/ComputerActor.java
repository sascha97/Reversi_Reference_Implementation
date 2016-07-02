/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.actor;

import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.GamePosition;
import reversi.evaluation.Evaluation;
import reversi.evaluation.MobilityEvaluation;
import reversi.game.ReversiGameConfiguration;
import reversi.player.Player;

/**
 * This is the base class for any ComputerActor needed in the ReversiGame.
 *
 * A ComputerActor is responsible for making a move in the game without needing any feedback.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public abstract class ComputerActor extends Actor {
    //The depth of how many GamePositions will be evaluated.
    private final int DEPTH;

    //How any GamePosition will be evaluated.
    private final Evaluation evaluation;

    //Constants declaring the MAXIMAL and MINIMAL VALUE for WINNING OR LOOSING the ReversiGame.
    protected final int WINNING_VALUE = 2_000_000_000;
    protected final int LOOSING_VALUE = -2_000_000_000;

    protected ComputerActor(String name) {
        super(name);

        //Get the configuration and load the search depth from the config file.
        ReversiGameConfiguration config = ReversiGameConfiguration.getInstance();
        DEPTH = Integer.parseInt(config.getProperty(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, "5"));
        //Use the mobility evaluation for all computer actors.
        evaluation = new MobilityEvaluation();
    }

    /**
     * This is the base Strategy that all ComputerActors will use. They just make the best move that will be returned
     * by the search(GamePosition, int, Evaluation) method.
     *
     * @return
     */
    @Override
    public Strategy getStrategy() {
        return new Strategy() {
            @Override
            public GameMove move(GamePosition gamePosition) {
                SearchNode node = search(gamePosition, DEPTH, evaluation);
                return node.getGameMove();
            }
        };
    }

    /**
     * The method will evaluate the Board if no move can be made any more.
     * <p>
     * The game will be won by any player who has more pieces on the board.
     *
     * @param board  The board that should be evaluated.
     * @param player The players view from which the board should be evaluated.
     * @return
     */
    protected int finalValue(final Board board, final Player player) {
        int result = 0;

        //Check who has more pieces on the board, and then set if the player is winning or not.
        switch (Integer.signum(board.countDifference(player))) {
            case -1:
                result = LOOSING_VALUE;
                break;
            case 1:
                result = WINNING_VALUE;
                break;
        }

        return result;
    }

    /**
     * This method should be called when any computer search is done, because it should be possible to stop the search
     * if a game has to be interrupted.
     *
     * @return true if the algorithm should be interupted. false if the algorithm should not be interrupted.
     */
    protected final boolean isInterrupted() {
        //returns if the GameThread is interrupted, this is the only thread calling this
        return Thread.currentThread().isInterrupted();
    }

    /**
     * This method returns the best move that the ComputerActor can do assuming that the human player is also
     * always playing with its best move.
     *
     * @param gamePosition The current GamePosition that has to be evaluated
     * @param depth How many moves the computer should evaluate to find its best move
     * @param evaluation The evaluation algorithm how a GamePosition is evaluated.
     *
     * @return The best move for the ComputerActor
     */
    protected abstract SearchNode search(GamePosition gamePosition, int depth, Evaluation evaluation);
}
