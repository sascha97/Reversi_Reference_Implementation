/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Q11_Reversi".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.ui;

import reversi.actor.HumanActor;
import reversi.board.GameMove;
import reversi.game.Game;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 15. May 2016
 */

public abstract class GameController implements HumanActor.HumanActable {
    protected Game gameModel;
    protected GameView gameView;

    private GameMove gameMove = null;

    public GameController(Game gameModel) {
        this.gameModel = gameModel;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public abstract void play();

    @Override
    public final void requestUserInput() throws InterruptedException {
        gameView.showAllLegalMoves();

        String userInput = gameView.requestUserInput();

        this.gameMove = convertInput(userInput);
    }

    @Override
    public final GameMove getUserInput() {
        return this.gameMove;
    }

    protected abstract GameMove convertInput(String userInput);
}
