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
 * This is an Actor with that makes Random moves.
 *
 * The Actor is playing automatically so that a human can play against it.
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

    /*
     * This Strategy returns a Random move.
     */
    private static final Strategy randomStrategy = new Strategy() {
        private final Random random = new Random();

        @Override
        public GameMove move(GamePosition gamePosition) {
            //Get the current Board and player
            Board board = gamePosition.getBoard();
            Player currentPlayer = gamePosition.getCurrentPlayer();

            //Get all legal moves
            List<GameMove> legalMoves = board.getAllLegalMoves(currentPlayer);
            //An random number representing one of the elements present in the list
            int r = random.nextInt(legalMoves.size());

            //Returns a random possible move.
            return legalMoves.get(r);
        }
    };
}
