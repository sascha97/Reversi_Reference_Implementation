/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Q11_Reversi".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.ui;

import reversi.board.GameMove;
import reversi.board.Square;
import reversi.game.Game;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 17. May 2016
 */
public class GraphicalGameController extends GameController {
    public GraphicalGameController(Game gameModel) {
        super(gameModel);
    }

    @Override
    public void play() {
        gameModel.play();
    }

    @Override
    protected GameMove convertInput(String userInput) {
        if (userInput == null) {
            return null;
        }
        userInput = userInput.trim().toLowerCase();

        char cYPos = userInput.charAt(0);
        char cXPos = userInput.charAt(1);

        int yPos = cXPos - '0';
        int xPos = cYPos - '0';

        Square square = gameModel.getGamePosition().getBoard().getSquare(xPos, yPos);
        return new GameMove(square);
    }
}
