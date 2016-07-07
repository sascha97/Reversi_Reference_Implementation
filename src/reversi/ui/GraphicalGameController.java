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
 * This is the Controller for a graphical user interface.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 17. May 2016
 */
public class GraphicalGameController extends GameController {
    /**
     * Constructor sets up the controller.
     *
     * @param gameModel The gameModel on which the game should be played.
     */
    public GraphicalGameController(Game gameModel) {
        super(gameModel);
    }

    /**
     * This method converts the input fro a graphical user interface to a game move.
     *
     * @param userInput The input of the user from a graphical user interface.
     * @return The GameMove that a user entered
     */
    @Override
    protected GameMove convertInput(String userInput) {
        //If user input is null it can not be converted to a game move
        if (userInput == null) {
            return null;
        }

        //trim the input
        userInput = userInput.trim().toLowerCase();

        //split the user input
        String[] coordinates = userInput.split(" ");

        //Convert to board coordinates
        int yPos = Integer.parseInt(coordinates[1]);
        int xPos = Integer.parseInt(coordinates[0]);

        //Get the square from the board and create a game move
        Square square = gameModel.getGamePosition().getBoard().getSquare(xPos, yPos);
        return new GameMove(square);
    }
}
