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

import reversi.board.GameMove;
import reversi.board.GamePosition;

import java.util.ResourceBundle;

/**
 * This is the HumanActor of the ReversiGame.
 *
 * The HumanActor is dependent on any user input.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 16. May 2016
 */

public class HumanActor extends Actor {
    private final ResourceBundle RES = ResourceBundle.getBundle("strings/Values");
    //The interface that allows the actor to request its input.
    private HumanActable humanActable;
    private final Strategy humanStrategy = new Strategy() {
        @Override
        public GameMove move(GamePosition gamePosition) throws InterruptedException {
            //This method requests the user to do the input
            humanActable.requestUserInput();

            while (!gamePosition.getBoard().isMoveLegal(humanActable.getUserInput(), gamePosition.getCurrentPlayer())) {
                humanActable.sendMessage(RES.getString("game.message.invalid.move"));
                humanActable.requestUserInput();
            }

            return humanActable.getUserInput();
        }
    };

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
}
