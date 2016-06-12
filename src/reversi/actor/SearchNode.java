/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.actor;

import reversi.board.GameMove;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public class SearchNode {
    private final GameMove gameMove;
    private final int evaluationValue;

    public SearchNode(GameMove gameMove, int evaluationValue) {
        this.gameMove = gameMove;
        this.evaluationValue = evaluationValue;
    }

    public GameMove getGameMove() {
        return gameMove;
    }

    public int getEvaluationValue() {
        return evaluationValue;
    }

    public SearchNode negated() {
        return new SearchNode(gameMove, -evaluationValue);
    }
}
