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

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 15. May 2016
 */

public abstract class GameView implements Observer {
    protected Game gameModel;
    protected GameController gameController;

    public GameView(Game gameModel) {
        this.gameModel = gameModel;
        gameModel.addObserver(this);
    }

    protected abstract String requestUserInput() throws InterruptedException;

    protected abstract void showAllLegalMoves();

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void show() {
        gameController.play();
    }
}
