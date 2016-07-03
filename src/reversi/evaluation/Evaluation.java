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
 * This interface is just responsible for evaluating a GamePosition.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public interface Evaluation {
    int evaluateGame(GamePosition gamePosition);
}
