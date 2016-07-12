/*
 * Copyright (c) 2016. Sascha Lutzenberger. All rights reserved.
 *
 * This file is part of the project "Reversi_Reference_Implementation"
 *
 * Redistribution and use in source and binary forms, without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - The author of this source code has given you the permission to use this
 *   source code.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - The code is not used in commercial projects, except you got the permission
 *   for using the code in any commerical projects from the author.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
