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
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public class MobilityEvaluation implements Evaluation {
    @Override
    public int evaluateGame(GamePosition gamePosition) {
        //The most possible moves
        return gamePosition.getBoard().getAllLegalMoves(gamePosition.getCurrentPlayer()).size();
    }
}
