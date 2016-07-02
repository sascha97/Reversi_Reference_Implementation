/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.actor;

import reversi.board.GameMove;
import reversi.board.GamePosition;

/**
 * A Strategy is used so that an Actor can make a move. The next move is just dependent on the current GamePosition
 * of the ReversiGame.
 *
 * The responsibility of any Strategy is just to make a move given the current GamePosition. A Strategy should not be
 * able to evaluate the GameMove. For evaluation of a GamePosition {@see reversi.evaluation.Evaluation}
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public interface Strategy {
    /**
     * This method is just responsible for making a move given the current GamePosition.
     *
     * @param gamePosition The current GamePosition
     * @return The move that should be made
     * @throws InterruptedException If user decides to end the program while a move should be made.
     */
    GameMove move(GamePosition gamePosition) throws InterruptedException;
}
