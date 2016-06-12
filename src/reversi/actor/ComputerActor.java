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
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public abstract class ComputerActor extends Actor {
    private final int DEPTH;

    private final Evaluation evaluation;

    protected final int WINNING_VALUE = 2_000_000_000;
    protected final int LOOSING_VALUE = -2_000_000_000;

    protected ComputerActor(String name) {
        super(name);

        ReversiGameConfiguration config = ReversiGameConfiguration.getInstance();

        DEPTH = Integer.parseInt(config.getProperty(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, "5"));
        evaluation = new MobilityEvaluation();
    }

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

    protected int finalValue(final Board board, final Player player) {
        int result = 0;

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

    protected final boolean isInterrupted() {
        //returns if the GameThread is interrupted, this is the only thread calling this
        return Thread.currentThread().isInterrupted();
    }

    protected abstract SearchNode search(GamePosition gamePosition, int depth, Evaluation evaluation);
}
