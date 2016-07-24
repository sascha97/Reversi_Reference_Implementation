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
 * This Evaluation evaluates the GamePosition based on the difference of pieces. If a player has more pieces than the
 * opponent the evaluation returns a positive value. If the opponent has more pieces than the player then a negative
 * difference value will be calculated.
 * <p>
 * This Evaluation evaluates also the GamePosition based on the mobility of the players. In this Evaluation it is
 * important that it is possible to make as many moves as possible in the next move. This value will be calculated.
 * <p>
 * This Evaluation evaluates also the GamePosition based on the number of corner squares captured by a player, the
 * same is for the number of captured corner squares.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 23. July 2016
 */
public class MixedEvaluation implements Evaluation {
    /**
     * This method evaluates a given GamePosition.
     *
     * @param gamePosition The GamePosition which should be evaluated.
     * @return A number representing the value of the given GamePosition
     */
    @Override
    public int evaluateGame(GamePosition gamePosition) {
        int difference = gamePosition.getBoard().countDifference(gamePosition.getCurrentPlayer());
        int mobility = gamePosition.getBoard().getAllLegalMoves(gamePosition.getCurrentPlayer()).size();
        int corner = countCornerSquares(gamePosition.getBoard(), gamePosition.getCurrentPlayer());
        int edge = countEdgeSquares(gamePosition.getBoard(), gamePosition.getCurrentPlayer());

        return difference * 2 + mobility * 10 + corner * 100 + edge;
    }

    /**
     * This method counts the difference of corner squares. If the opponent has more corner squares than the own
     * player a negative value will be returned.
     *
     * @param board  The board on which the corner squares should be counted.
     * @param player The player in whose favour should be counted.
     * @return The difference of corners taken by the players
     */
    private int countCornerSquares(Board board, Player player) {
        int corners = 0;

        for (int x = 0; x < board.getBoardWidth(); x++) {
            for (int y = 0; y < board.getBoardHeight(); y++) {
                Square square = board.getSquare(x, y);

                if (square.isCornerSquare()) {
                    if (square.getSquareState() == player.getSquareState()) {
                        corners++;
                    } else if (square.getSquareState() == player.getOpponent().getSquareState()) {
                        corners--;
                    }
                }
            }
        }

        return corners;
    }

    /**
     * This method counts the difference of edge squares. If the opponent has more edge squares than the own
     * player a negative value will be returned.
     *
     * @param board The board on which the corner squares should be counted.
     * @param player The player in whose favour should be counted.
     * @return The difference of edge squares taken by the players
     */
    private int countEdgeSquares(Board board, Player player) {
        int edges = 0;

        for (int x = 0; x < board.getBoardWidth(); x++) {
            for (int y = 0; y < board.getBoardHeight(); y++) {
                Square square = board.getSquare(x, y);

                //count only edge squares
                if (square.isEdgeSquare() && !square.isEdgeSquare()) {
                    if (square.getSquareState() == player.getSquareState()) {
                        edges++;
                    } else if (square.getSquareState() == player.getOpponent().getSquareState()) {
                        edges--;
                    }
                }
            }
        }

        return edges;
    }
}
