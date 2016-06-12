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

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 16. May 2016
 */

public class HumanActor extends Actor {
    private HumanActable humanActable;

    public interface HumanActable {
        void requestUserInput() throws InterruptedException;

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

            return humanActable.getUserInput();
        }
    };
}
