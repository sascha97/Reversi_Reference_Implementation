/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.evaluation;

import reversi.board.GamePosition;

/**
 * This Evaluation evaluates the GamePosition based on the difference of pieces. If a player has more pieces than the
 * opponent the evaluation returns a positive value. If the opponent has more pieces than the player then a negative
 * value will be returned.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public class CountDifferenceEvaluation implements Evaluation {
    @Override
    public int evaluateGame(GamePosition gamePosition) {
        //Returns the difference between two players
        return gamePosition.getBoard().countDifference(gamePosition.getCurrentPlayer());
    }
}
