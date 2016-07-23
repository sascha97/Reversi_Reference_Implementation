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

import reversi.actor.alphabeta.OrderedLegalMoves;
import reversi.actor.alphabeta.StaticOrderedLegalMoves;
import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.GamePosition;
import reversi.evaluation.Evaluation;
import reversi.player.Player;

import java.util.List;

/**
 * This is an Actor with an AlphaBeta search Algorithm implemented.
 *
 * The Actor is playing automatically so that a human can play against it.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public class AlphaBetaActor extends ComputerActor {
    //the interface that returns a ordered game move list
    private OrderedLegalMoves legalMoveSearcher;

    public AlphaBetaActor() {
        super("AlphaBeta");

        //create the legalMoveSearcher
        legalMoveSearcher = new StaticOrderedLegalMoves();
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
        //Get the player and the board
        Player player = gamePosition.getCurrentPlayer();
        Board board = gamePosition.getBoard();

        //Return the best move found by the AlphaBeta algorithm
        return searchImpl(player, board, LOOSING_VALUE, WINNING_VALUE, depth, evaluation);
    }

    /**
     * This is the AlphaBeta implementation of the search method.
     *
     * @param player     The player who's turn it is.
     * @param board      The board with its current game state.
     * @param alpha      The best value of the current player.
     * @param beta       The best value of the opponent player.
     * @param depth      How many moves should be evaluated by the computer.
     * @param evaluation The evaluation algorithm how a GamePosition is evaluated.
     * @return The best move of the AlphaBetaActor
     */
    private SearchNode searchImpl(Player player, Board board, int alpha, int beta, int depth, Evaluation evaluation) {
        //If interrupted stop Searching as soon as possible
        if (isInterrupted()) {
            return new SearchNode(null, 0);
        }

        //get the opponent of the current player
        Player opponent = player.getOpponent();

        //If depth is reached stop searching and just return an evaluation value of the current position
        if (depth == 0 || !board.hasAnyPlayerAnyLegalMoves()) {
            return new SearchNode(null, evaluation.evaluateGame(new GamePosition(board, player)));
        }

        //Get all legal moves of the current player sorted
        List<GameMove> legalMoves = legalMoveSearcher.getSortedList(board, player);

        //The best search node
        //The first node hat the lowest possible evaluation value.
        SearchNode node = new SearchNode(null, alpha);

        //Iterate over all possible moves and evaluate them
        for (GameMove move : legalMoves) {
            //The GamePosition and board after making the move
            GamePosition position = board.makeMove(move, player);
            Board cBoard = position.getBoard();

            //The evaluation value of the current board.
            int value = searchImpl(opponent, cBoard, -beta, -node.getEvaluationValue(), depth - 1,
                    evaluation).negated().getEvaluationValue();

            //Change SearchNode if the new node is a better move for the game.
            if (value > node.getEvaluationValue()) {
                node = new SearchNode(move, value);
            }
            //If the move can't be done because the opponent would prevent this from happening stop searching.
            if (node.getEvaluationValue() >= beta) {
                break;
            }
        }

        return node;
    }
}
