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
import reversi.player.Player;

import java.util.List;
import java.util.Random;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public class RandomActor extends Actor {
    public RandomActor() {
        super("Random Actor");
    }

    @Override
    public Strategy getStrategy() {
        return randomStrategy;
    }

    private final static Strategy randomStrategy = new Strategy() {
        private Random random = new Random();

        @Override
        public GameMove move(GamePosition gamePosition) {
            Board board = gamePosition.getBoard();
            Player currentPlayer = gamePosition.getCurrentPlayer();

            List<GameMove> legalMoves = board.getAllLegalMoves(currentPlayer);
            int r = this.random.nextInt(legalMoves.size());

            return legalMoves.get(r);
        }
    };
}
