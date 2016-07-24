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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This is an Actor with an AlphaBeta search Algorithm implemented. The search is performed by the evaluation
 * <p>
 * The Actor is playing automatically so that a human can play against it.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 24. July 2016
 */
public class AlphaBetaActor2 extends ComputerActor {
    public AlphaBetaActor2() {
        super("AlphaBeta2");
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
        return searchImpl(player, board, LOOSING_VALUE, WINNING_VALUE, depth, evaluation, 0);
    }

    /**
     * This is the AlphaBeta implementation of the search method.
     *
     * @param player              The player who's turn it is.
     * @param board               The board with its current game state.
     * @param alpha               The best value of the current player.
     * @param beta                The best value of the opponent player.
     * @param depth               How many moves should be evaluated by the computer.
     * @param evaluation          The evaluation algorithm how a GamePosition is evaluated.
     * @param evaluationNodeValue The best evaluation value of the best SearchNode.
     * @return The best move of the AlphaBetaActor
     */
    private SearchNode searchImpl(Player player, Board board, int alpha, int beta,
                                  int depth, Evaluation evaluation, int evaluationNodeValue) {
        //If interrupted stop Searching as soon as possible
        if (isInterrupted()) {
            return new SearchNode(null, 0);
        }

        //get the opponent of the current player
        Player opponent = player.getOpponent();

        //If depth is reached stop searching and just return an evaluation value of the current position
        if (depth == 0 || !board.hasAnyPlayerAnyLegalMoves()) {
            return new SearchNode(null, evaluationNodeValue);
        }

        //Get all legal moves of the current player sorted
        SortedMap<GameMove, GameMoveData> legalMoves = getSortedMoves(player, board, evaluation);

        //The best search node
        //The first node hat the lowest possible evaluation value.
        SearchNode node = new SearchNode(null, alpha);

        if (legalMoves.isEmpty()) {
            node = searchImpl(opponent, board, -beta, -alpha, depth - 1, evaluation, -evaluationNodeValue).negated();
        } else {
            //Iterate over all possible moves and evaluate them
            for (GameMove move : legalMoves.keySet()) {
                //The GamePosition and the board after making the move
                GamePosition gamePosition = legalMoves.get(move).getGamePosition();
                Board cBoard = gamePosition.getBoard();

                //The evaluation value of the current game position
                int value = searchImpl(opponent, cBoard, -beta, -node.getEvaluationValue(), depth - 1, evaluation,
                        -legalMoves.get(move).getEvaluationValue()).negated().getEvaluationValue();

                //Change SearchNode if the new node is a better move for the game.
                if (value > node.getEvaluationValue()) {
                    node = new SearchNode(move, value);
                }
                //If the move can't be done because the opponent would prevent this from happening stop searching.
                if (node.getEvaluationValue() >= beta) {
                    break;
                }
            }
        }


        return node;
    }

    /**
     * This method creates a map where data is linked to the game move.
     *
     * @param player     The player whose turn it is
     * @param board      The current board state
     * @param evaluation The evaluation which should be used for evaluating the position
     * @return A sorted map which contains all legal moves sorted by their evaluation value
     */
    private SortedMap<GameMove, GameMoveData> getSortedMoves(Player player, Board board, Evaluation evaluation) {
        //Get all legal moves for the player from the board
        List<GameMove> legalMoveList = board.getAllLegalMoves(player);
        //Create the map where the data should be linked to the move
        Map<GameMove, GameMoveData> evaluatedMoves = new HashMap<>();

        //Iterate over all legal moves and evaluate them
        for (GameMove move : legalMoveList) {
            //make the move
            GamePosition gamePosition = board.makeMove(move, player);
            //create the game move data which holds the game position and evaluate game position
            GameMoveData gameMoveData = new GameMoveData(gamePosition, evaluation.evaluateGame(gamePosition));

            //add the move to the map evaluated moves
            evaluatedMoves.put(move, gameMoveData);
        }

        //Create sorted map so that the game moves are sorted by the evaluation value
        SortedMap<GameMove, GameMoveData> sortedMap = new TreeMap<>(new GameMoveComparator(evaluatedMoves));
        //Add all entries to the map and sort them using the comparator
        sortedMap.putAll(evaluatedMoves);

        return sortedMap;
    }

    /**
     * This class is used for comparing the map created in getSortedMoves(...)
     */
    private static class GameMoveComparator implements Comparator<GameMove> {
        //The map with the values
        private Map<GameMove, GameMoveData> evaluatedMoves;

        private GameMoveComparator(Map<GameMove, GameMoveData> evaluatedMoves) {
            this.evaluatedMoves = evaluatedMoves;
        }

        @Override
        public int compare(GameMove gameMove1, GameMove gameMove2) {
            //Get the evaluation values from the evaluation function
            int evaluationValue1 = evaluatedMoves.get(gameMove1).getEvaluationValue();
            int evaluationValue2 = evaluatedMoves.get(gameMove2).getEvaluationValue();

            //if the evaluation values are the same moves still have to be ordered
            if (evaluationValue1 == evaluationValue2) {
                //board coordinates are used for ordering
                String string1 = gameMove1.getSquare().getSquareName();
                String string2 = gameMove2.getSquare().getSquareName();

                return string1.compareTo(string2);
            }

            //compare the two evaluation values
            return Integer.compare(evaluationValue1, evaluationValue2);
        }
    }

    //This class holds game move data
    private static class GameMoveData {
        //stores the evaluation value
        private int evaluationValue;
        //stores the game position
        private GamePosition gamePosition;

        private GameMoveData(GamePosition gamePosition, int evaluationValue) {
            this.evaluationValue = evaluationValue;
            this.gamePosition = gamePosition;
        }

        private GamePosition getGamePosition() {
            return gamePosition;
        }

        private int getEvaluationValue() {
            return evaluationValue;
        }
    }
}
