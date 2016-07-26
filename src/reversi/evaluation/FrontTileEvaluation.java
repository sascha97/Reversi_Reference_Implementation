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
import reversi.board.SquareState;
import reversi.player.Player;

import java.util.List;

/**
 * Each move is played to an empty square adjacent to an opponents disk and flips at least one of their disks.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 25. July 2016
 */
public class FrontTileEvaluation implements Evaluation {
    @Override
    public int evaluateGame(GamePosition gamePosition) {
        //Get the board and the current player
        Board board = gamePosition.getBoard();
        Player player = gamePosition.getCurrentPlayer();

        //Counter for front tile disks of player and opponent
        int ownFrontTileDisks = 0;
        int opponentFrontTileDisks = 0;

        //Iterate over the entire board
        for (int x = 0; x < board.getBoardWidth(); x++) {
            for (int y = 0; y < board.getBoardHeight(); y++) {
                Square square = board.getSquare(x, y);

                //Only applies when SquareState is EMPTY
                if (square.getSquareState() == SquareState.EMPTY) {
                    //Get all neighbours of the square
                    List<Square> neighbourList = BoardUtils.getNeighbourSquares(board, square);

                    //Check if the neighbour square is an own or opponents square
                    for (Square neighbour : neighbourList) {
                        //count the squares
                        if (neighbour.getSquareState() == player.getSquareState()) {
                            ownFrontTileDisks++;
                        } else if (neighbour.getSquareState() == player.getOpponent().getSquareState()) {
                            opponentFrontTileDisks++;
                        }
                    }
                }

            }
        }

        //play minimizing on FrontTile disks
        return opponentFrontTileDisks - ownFrontTileDisks;
    }
}
