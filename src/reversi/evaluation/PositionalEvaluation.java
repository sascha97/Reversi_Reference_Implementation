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
 *   for using the code in any commercial projects from the author.
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

package reversi.evaluation;

import reversi.board.Board;
import reversi.board.GamePosition;
import reversi.board.Square;
import reversi.player.Player;

/**
 * This Evaluation evaluates the GamePosition based on the weights of squares. The weight of all squares that contain
 * player disks are added together afterwards the weight of the opponent is substracted.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 25. July 2016
 */
public class PositionalEvaluation implements Evaluation {
    //The square weigher containing all square weights
    private SquareWeigher squareWeigher;

    /**
     * This method evaluates a given GamePosition.
     *
     * @param gamePosition The GamePosition which should be evaluated.
     * @return A number representing the value of the given GamePosition
     */
    @Override
    public int evaluateGame(GamePosition gamePosition) {
        //get the board from the game position
        Board board = gamePosition.getBoard();
        //get the current player and the opponent
        Player player = gamePosition.getCurrentPlayer();
        Player opponent = player.getOpponent();

        //get the right square weigher
        squareWeigher = SquareWeigher.getInstance(board);

        //the positional evaluation for own pieces
        int ownPositionalEvaluationValue = 0;
        //the positional evaluation for opponents pieces
        int opponentPositionalEvaluationValue = 0;

        //iterate over all squares
        for (int x = 0; x < board.getBoardWidth(); x++) {
            for (int y = 0; y < board.getBoardHeight(); y++) {
                Square square = board.getSquare(x, y);

                //if the square state is own player square state
                if (square.getSquareState() == player.getSquareState()) {
                    //add the positional value to the own value
                    ownPositionalEvaluationValue += squareWeigher.getWeighValue(square);
                } else if (square.getSquareState() == opponent.getSquareState()) {
                    //add the positional value to the opponent value
                    opponentPositionalEvaluationValue += squareWeigher.getWeighValue(square);
                }
            }
        }

        //return the difference of positional evaluation
        return ownPositionalEvaluationValue - opponentPositionalEvaluationValue;
    }
}
