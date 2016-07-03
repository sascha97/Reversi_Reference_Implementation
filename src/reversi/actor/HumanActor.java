/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Q11_Reversi".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.actor;

import reversi.board.GameMove;
import reversi.board.GamePosition;

import java.util.ResourceBundle;

/**
 * This is the HumanActor of the ReversiGame.
 *
 * The HumanActor is dependend on any user input.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 16. May 2016
 */

public class HumanActor extends Actor {
    //The interface that allows the actor to request its input.
    private HumanActable humanActable;

    private final ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

    /**
     * The interface that is required so that any HumanActor can request its input.
     */
    public interface HumanActable {
        /**
         * This method is called when a user input should be requested.
         *
         * @throws InterruptedException If the game is paused during the request of a user input
         */
        void requestUserInput() throws InterruptedException;

        /**
         * This method sends an message that should be displayed on the user interface
         *
         * @param message The message that should be displayed on the screen.
         */
        void sendMessage(String message);

        /**
         * This method gets the user input.
         *
         * @return The move entered by the user.
         */
        GameMove getUserInput();
    }

    public HumanActor() {
        super("Human Actor");
    }

    public void addHumanActable(HumanActable humanActable) {
        this.humanActable = humanActable;
    }

    @Override
    public Strategy getStrategy() {
        return humanStrategy;
    }

    private Strategy humanStrategy = new Strategy() {
        @Override
        public GameMove move(GamePosition gamePosition) throws InterruptedException {
            //This method requests the user to do the input
            humanActable.requestUserInput();

            while (!gamePosition.getBoard().isMoveLegal(humanActable.getUserInput(), gamePosition.getCurrentPlayer())) {
                humanActable.sendMessage(RES.getString("message.invalid.move"));
                humanActable.requestUserInput();
            }

            return humanActable.getUserInput();
        }
    };
}
