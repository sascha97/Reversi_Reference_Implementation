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
import reversi.evaluation.MobilityEvaluation;
import reversi.game.ReversiGameConfiguration;
import reversi.player.Player;

/**
 * This is the base class for any ComputerActor needed in the ReversiGame.
 *
 * A ComputerActor is responsible for making a move in the game without needing any feedback.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public abstract class ComputerActor extends Actor {
    //The depth of how many GamePositions will be evaluated.
    private int DEPTH;

    //How any GamePosition will be evaluated.
    private final Evaluation evaluation;

    //Constants declaring the MAXIMAL and MINIMAL VALUE for WINNING OR LOOSING the ReversiGame.
    final int WINNING_VALUE = 2_000_000_000;
    final int LOOSING_VALUE = -2_000_000_000;

    ComputerActor(String name) {
        super(name);

        refreshActor();

        //Use the mobility evaluation for all computer actors.
        evaluation = new MobilityEvaluation();
    }

    @Override
    public void refreshActor() {
        //Get the configuration and load the search depth from the config file.
        ReversiGameConfiguration config = ReversiGameConfiguration.getInstance();
        DEPTH = Integer.parseInt(config.getProperty(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, "5"));
    }

    /**
     * This is the base Strategy that all ComputerActors will use. They just make the best move that will be returned
     * by the search(GamePosition, int, Evaluation) method.
     *
     * @return The base Strategy for every computer actor.
     */
    @Override
    public Strategy getStrategy() {
        return new Strategy() {
            @Override
            public GameMove move(GamePosition gamePosition) {
                SearchNode node = search(gamePosition, DEPTH, evaluation);
                return node.getGameMove();
            }
        };
    }

    /**
     * The method will evaluate the Board if no move can be made any more.
     * <p>
     * The game will be won by any player who has more pieces on the board.
     *
     * @param board  The board that should be evaluated.
     * @param player The player's view from which the board should be evaluated.
     *
     * @return An evaluation value of the board.
     */
    int finalValue(Board board, Player player) {
        int result = 0;

        //Check who has more pieces on the board, and then set if the player is winning or not.
        switch (Integer.signum(board.countDifference(player))) {
            case -1:
                result = LOOSING_VALUE;
                break;
            case 1:
                result = WINNING_VALUE;
                break;
        }

        return result;
    }

    /**
     * This method should be called when any computer search is done, because it should be possible to stop the search
     * if a game has to be interrupted.
     *
     * @return true if the algorithm should be interrupted. false if the algorithm should not be interrupted.
     */
    final boolean isInterrupted() {
        //returns if the GameThread is interrupted, this is the only thread calling this
        return Thread.currentThread().isInterrupted();
    }

    /**
     * This method returns the best move that the ComputerActor can do assuming that the human player is also
     * always playing with its best move.
     *
     * @param gamePosition The current GamePosition that has to be evaluated
     * @param depth How many moves the computer should evaluate to find its best move
     * @param evaluation The evaluation algorithm how a GamePosition is evaluated.
     *
     * @return The best move for the ComputerActor
     */
    protected abstract SearchNode search(GamePosition gamePosition, int depth, Evaluation evaluation);

    @Override
    public String toString() {
        return "ComputerActor{" +
                "DEPTH=" + DEPTH +
                ", evaluation=" + evaluation +
                ", WINNING_VALUE=" + WINNING_VALUE +
                ", LOOSING_VALUE=" + LOOSING_VALUE +
                '}';
    }
}
