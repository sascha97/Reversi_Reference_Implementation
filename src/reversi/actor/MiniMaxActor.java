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

import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.GamePosition;
import reversi.evaluation.Evaluation;
import reversi.player.Player;

import java.util.List;

/**
 * This is an Actor with an MiniMax search Algorithm implemented.
 *
 * The Actor is playing automatically so that a human can play against it.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 17. May 2016
 */
public class MiniMaxActor extends ComputerActor {
    public MiniMaxActor() {
        super("MiniMaxActor");
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
        return searchImpl(gamePosition.getCurrentPlayer(), gamePosition.getBoard(), depth, evaluation);
    }

    /**
     * This is the MiniMax implementation of the search method.
     *
     * @param player     The player who's turn it is.
     * @param board      The board with its current game state.
     * @param depth      How many moves should be evaluated by the computer.
     * @param evaluation The evaluation algorithm how a GamePosition is evaluated.
     * @return The best move of the MiniMaxActor
     */
    private SearchNode searchImpl(Player player, Board board, int depth, Evaluation evaluation) {
        //If interrupted stop Searching as soon as possible
        if (isInterrupted()) {
            return new SearchNode(null, 0);
        }

        //The best search node
        SearchNode node;
        //get the opponent of the current player
        Player opponent = player.getOpponent();

        //If depth is reached stop searching and just return an evaluation value of the current position
        if (depth == 0) {
            node = new SearchNode(null, evaluation.evaluateGame(new GamePosition(board, player)));
        } else {
            //Get all legal moves of the current player
            List<GameMove> legalMoves = board.getAllLegalMoves(player);
            //When there are no available moves then check what to do next
            if (legalMoves.isEmpty()) {
                //If the opponent has legal moves then evaluate the opponents moves
                if (board.hasAnyLegalMoves(opponent)) {
                    node = searchImpl(opponent, board, depth - 1, evaluation).negated();
                } else {
                    //Else assume the game is done after this move and evaluate the "final" position.
                    node = new SearchNode(null, finalValue(board, player));
                }
            } else {
                //The first node is has the lowest possible evaluation value.
                node = new SearchNode(null, Integer.MIN_VALUE);

                //Iterate over all possible moves and evaluate them
                for (GameMove move : legalMoves) {
                    //The GamePosition and Board after making the move
                    GamePosition position = board.makeMove(move, player);
                    Board cBoard = position.getBoard();

                    //The evaluation value of the current board
                    int value = searchImpl(opponent, cBoard, depth - 1, evaluation).negated().getEvaluationValue();

                    //Change SearchNode if the new node is a better move for the game.
                    if (value > node.getEvaluationValue()) {
                        node = new SearchNode(move, value);
                    }
                }
            }
        }

        return node;
    }
}
