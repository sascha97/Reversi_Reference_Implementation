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
package reversi.ui;

import reversi.game.Game;
import reversi.game.ThreadEvent;

import java.util.Observer;
import java.util.ResourceBundle;

/**
 * This is the abstract base class for all GameViews used in this program.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 15. May 2016
 */

public abstract class GameView implements Observer {
    //The game model
    final Game gameModel;
    //The resource bundle containing internationalized strings
    final ResourceBundle RES = ResourceBundle.getBundle("strings/Values");
    //Used for syncing data across multiple threads
    final ThreadEvent resultsReady = new ThreadEvent();
    //The game controller
    GameController gameController;

    /**
     * The constructor to set up the game view
     *
     * @param gameModel The gameModel which should be displayed.
     */
    GameView(Game gameModel) {
        //Set this as game view and register the observer
        this.gameModel = gameModel;
        gameModel.addObserver(this);
    }

    /**
     * This method is used to request any input from the user
     *
     * @return The input from the user
     * @throws InterruptedException If the program is interrupted whilst requesting any user input.
     */
    protected abstract String requestUserInput() throws InterruptedException;

    /**
     * This method is used to display any messages on the game view.
     *
     * @param message The message which should be displayed
     */
    protected abstract void displayMessage(String message);

    /**
     * This method is called to display all legal moves to the user.
     */
    protected abstract void showAllLegalMoves();

    /**
     * This method is used to set the GameController to the GameView.
     *
     * @param gameController The gameController that should be added to the game view.
     */
    public final void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * This method is called to show the view.
     */
    public void show() {
        gameController.play();
    }
}
