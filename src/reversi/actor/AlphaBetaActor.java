/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.actor;

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
    public AlphaBetaActor() {
        super("AlphaBeta");
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
                    node = searchImpl(opponent, board, -beta, -alpha, depth - 1, evaluation).negated();
                } else {
                    //Else assume the game is done after this move and evaluate the "final" position
                    node = new SearchNode(null, finalValue(board, player));
                }
            } else {
                //The first node hat the lowest possible evaluation value.
                node = new SearchNode(legalMoves.get(0), alpha);

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
            }
        }

        return node;
    }
}
