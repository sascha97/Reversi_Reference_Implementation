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
package reversi.ui;

import reversi.actor.HumanActor;
import reversi.board.GameMove;
import reversi.game.Game;

/**
 * This is the abstract base class for any controller used in this program.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 15. May 2016
 */

public abstract class GameController implements HumanActor.HumanActable {
    /**
     * Those are the following valid game actions of the program.
     */
    enum GameAction {
        //Action to take back the last move of the human player
        TAKEBACK,
        //Action to resign the current game
        RESIGN,
        //Action to end the game (this will close the application)
        END_GAME,
        //Action to start a new game
        NEW_GAME
    }

    //The gameModel
    final Game gameModel;
    //The Game view
    private GameView gameView;

    //The last game move made by an user.
    private GameMove gameMove;

    /**
     * Constructor to create a GameController
     *
     * @param gameModel The model of the controller
     */
    GameController(Game gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * This method handles the UserGameAction.
     *
     * @param action The game action that the user has entered.
     */
    final void handleUserAction(GameAction action) {
        switch (action) {
            //Take back a move
            case TAKEBACK:
                gameModel.takeBackMove();
                break;
            //Start a new game
            case NEW_GAME:
                gameModel.startNewGame();
                gameModel.play();
                break;
            //resign current game
            case RESIGN:
                if (gameModel.hasGameAnyLegalMoves()) {
                    gameModel.endGame();
                }
                break;
            //End game and application
            case END_GAME:
                if (gameModel.hasGameAnyLegalMoves()) {
                    gameModel.endGame();
                }
                break;
        }
    }

    /**
     * This method sets the game view to the controller
     *
     * @param gameView The GameView that should be added to the controller
     */
    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * This method starts the game by calling the Game.play() method.
     */
    public final void play() {
        gameModel.play();
    }

    /**
     * This method is implemented so that the controller can act as an HumanActable.
     *
     * @throws InterruptedException if the game is interrupted whilst requesting an input
     */
    @Override
    public final void requestUserInput() throws InterruptedException {
        //Display all the legal moves to the user
        gameView.showAllLegalMoves();

        //Get the input of the user
        String userInput = gameView.requestUserInput();

        //Convert the input of the user to a game move
        this.gameMove = convertInput(userInput);
    }

    /**
     * This method allows the controller to display the messages send by HumanActable to the user.
     *
     * @param message The message that should be displayed on the screen.
     */
    @Override
    public void sendMessage(String message) {
        //Display the message from the HumanActable to the user.
        gameView.displayMessage(message);
    }

    /**
     * This method allows the controller to pass the GameMove back to the HumanActable.
     *
     * @return The requested GameMove.
     */
    @Override
    public final GameMove getUserInput() {
        return this.gameMove;
    }

    /**
     * This method has to be implemented by all subclasses, because they are responsible for handling the user input.
     *
     * @param userInput The input of the user from a view.
     * @return The GameMove that a user entered.
     */
    protected abstract GameMove convertInput(String userInput);
}
