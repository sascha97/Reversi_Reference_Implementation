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
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public class AlphaBetaActor extends ComputerActor {
    public AlphaBetaActor() {
        super("AlphaBeta");
    }

    @Override
    protected SearchNode search(GamePosition gamePosition, int depth, Evaluation evaluation) {
        Player player = gamePosition.getCurrentPlayer();
        Board board = gamePosition.getBoard();

        return searchImpl(player, board, LOOSING_VALUE, WINNING_VALUE, depth, evaluation);
    }

    private SearchNode searchImpl(final Player player, final Board board, final int alpha, final int beta,
                                  final int depth, final Evaluation evaluation) {
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
                    node = searchImpl(opponent, board, -beta, -alpha, depth - 1, evaluation).negated();
                } else {
                    node = new SearchNode(null, finalValue(board, player));
                }
            } else {
                node = new SearchNode(legalMoves.get(0), alpha);

                for (GameMove move : legalMoves) {
                    GamePosition position = board.makeMove(move, player);
                    Board board2 = position.getBoard();

                    int val = searchImpl(opponent, board2, -beta, -node.getEvaluationValue(), depth - 1,
                            evaluation).negated().getEvaluationValue();
                    if (val > node.getEvaluationValue()) {
                        node = new SearchNode(move, val);
                    }
                    if (node.getEvaluationValue() >= beta) {
                        break;
                    }
                }
            }
        }
        return node;
    }
}
