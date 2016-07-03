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
 * A SearchNode holds a move and its evaluation value, so that any ComputerActor is able to find its best possible move.
 *
 * Any SearchNode is immutable but can be negated.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
class SearchNode {
    //The GameMove that has to be made to get the following evaluation value.
    private final GameMove gameMove;
    //The evaluation value of the GameMove.
    private final int evaluationValue;

    /**
     * Constructor for any SearchNode, get the GameMove and its corresponding evaluation value and store it in the
     * immutable object.
     *
     * @param gameMove        The GameMove that is evaluated.
     * @param evaluationValue The corresponding evaluation value of the GameMove.
     */
    SearchNode(GameMove gameMove, int evaluationValue) {
        //Set up the attributes
        this.gameMove = gameMove;
        this.evaluationValue = evaluationValue;
    }

    /**
     * This method returns the GameMove stored in the object.
     *
     * @return The gameMove.
     */
    GameMove getGameMove() {
        return gameMove;
    }

    /**
     * This method gets the corresponding evaluation value of the GameMove
     *
     * @return The evaluation value.
     */
    int getEvaluationValue() {
        return evaluationValue;
    }

    /**
     * This method returns the current SearchNode with an negated evaluation value.
     *
     * @return Negated SearchNode
     */
    SearchNode negated() {
        return new SearchNode(gameMove, -evaluationValue);
    }

    @Override
    public String toString() {
        return "SearchNode{" +
                "gameMove=" + gameMove +
                ", evaluationValue=" + evaluationValue +
                '}';
    }
}
