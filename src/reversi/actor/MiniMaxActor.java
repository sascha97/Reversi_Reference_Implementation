/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Q11_Reversi".
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
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 17. May 2016
 */

public class MiniMaxActor extends ComputerActor {
    public MiniMaxActor() {
        super("MiniMaxActor");
    }

    @Override
    protected SearchNode search(GamePosition gamePosition, int depth, Evaluation evaluation) {
        return searchImpl(gamePosition.getCurrentPlayer(), gamePosition.getBoard(), depth, evaluation);
    }

    private SearchNode searchImpl(final Player player, final Board board, final int depth,
                                  final Evaluation evaluation) {
        //If interrupted stop Searching as soon as possible
        if (super.isInterrupted()) {
            return new SearchNode(null, 0);
        }

        SearchNode node;
        final Player opponent = player.getOpponent();
        if (depth == 0) {
            node = new SearchNode(null, evaluation.evaluateGame(new GamePosition(board, player)));
        } else {
            List<GameMove> legalMoves = board.getAllLegalMoves(player);
            if (legalMoves.isEmpty()) {
                if (board.hasAnyLegalMoves(opponent)) {
                    node = searchImpl(opponent, board, depth - 1, evaluation).negated();
                } else {
                    node = new SearchNode(null, finalValue(board, player));
                }
            } else {
                node = new SearchNode(null, Integer.MIN_VALUE);
                for (GameMove move : legalMoves) {
                    int value = searchImpl(opponent, board.makeMove(move, player).getBoard(), depth - 1,
                            evaluation).negated().getEvaluationValue();
                    if (value > node.getEvaluationValue()) {
                        node = new SearchNode(move, value);
                    }
                }
            }
        }
        return node;
    }
}
