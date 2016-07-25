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
import reversi.player.Player;

/**
 * This Evaluation uses different Evaluations at different stages of the game. In the first 20 moves positional is
 * mobility and front tiles are more important.
 * <p>
 * In the mid stage corners edges, mobility and taking disks again if more important.
 * <p>
 * In the final stage of the game it is just important to get as many disks as possible.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 25. July 2016
 */
public class ChangingEvaluation implements Evaluation {
    //The different eval functions used in the evaluation
    private Evaluation countDifference = new CountDifferenceEvaluation();
    private Evaluation mobility = new MobilityEvaluation();
    private Evaluation frontTile = new FrontTileEvaluation();
    private Evaluation positional = new PositionalEvaluation();
    private Evaluation midGameEval = new MixedEvaluation();

    @Override
    public int evaluateGame(GamePosition gamePosition) {
        //Get the board and the player
        Board board = gamePosition.getBoard();
        Player player = gamePosition.getCurrentPlayer();

        int numberOfEmptyDisks = getNumberOfEmptyDisks(board);

        //If there are more than 40 moves to make look for a good start position and good mobility
        if (numberOfEmptyDisks > 40) {
            int position = positional.evaluateGame(gamePosition);
            int mobility = this.mobility.evaluateGame(gamePosition);
            int frontTile = this.frontTile.evaluateGame(gamePosition);

            return position * 2 + mobility * 4 + frontTile * 6;
        } else if (numberOfEmptyDisks > 15) {
            return midGameEval.evaluateGame(gamePosition);
        } else {
            int midEval = midGameEval.evaluateGame(gamePosition);
            int difference = countDifference.evaluateGame(gamePosition);

            return midEval * 2 + difference * 30;
        }
    }

    private int getNumberOfEmptyDisks(Board board) {
        //This returns the number of empty disks of the board
        return board.countPieces(Player.NONE);
    }
}
