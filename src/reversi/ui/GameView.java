/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Q11_Reversi".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.ui;

import reversi.game.Game;

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
    //The game controller
    GameController gameController;

    //The resource bundle containing internationalized strings
    final ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

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
