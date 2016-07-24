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
package reversi.actor;

import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.GamePosition;
import reversi.evaluation.Evaluation;
import reversi.player.Player;

import java.util.List;
import java.util.Random;

/**
 * This is an Actor with that makes Random moves.
 *
 * The Actor is playing automatically so that a human can play against it.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public class RandomActor extends ComputerActor {
    private final Random random = new Random();

    public RandomActor() {
        super("Random Actor");
    }

    /**
     * This method returns the best move that the ComputerActor can do assuming that the human player is also
     * always playing with its best move.
     *
     * @param gamePosition The current GamePosition that has to be evaluated
     * @param depth        How many moves the computer should evaluate to find its best move
     * @param evaluation   The evaluation algorithm how a GamePosition is evaluated.
     * @return The best move for the ComputerActor
     */
    @Override
    protected SearchNode search(GamePosition gamePosition, int depth, Evaluation evaluation) {
        //Get the current Board and player
        Board board = gamePosition.getBoard();
        Player currentPlayer = gamePosition.getCurrentPlayer();

        //Get all legal moves
        List<GameMove> legalMoves = board.getAllLegalMoves(currentPlayer);
        //An random number representing one of the elements present in the list
        int r = random.nextInt(legalMoves.size());

        //Returns a random possible move.
        GameMove move = legalMoves.get(r);

        return new SearchNode(move, 0);
    }
}
