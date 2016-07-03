/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.ui;

import reversi.board.GameMove;
import reversi.board.Square;
import reversi.game.Game;

/**
 * This is the Controller for a console based game.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 03. July 2016
 */
public class ConsoleGameController extends GameController {
    /**
     * Constructor sets up the controller.
     *
     * @param gameModel The gameModel on which the game should be played.
     */
    public ConsoleGameController(Game gameModel) {
        super(gameModel);
    }

    /**
     * This method converts the input from a console user to a game move.
     *
     * @param userInput The input of the user from a console.
     * @return The GameMove that a user entered.
     */
    @Override
    protected GameMove convertInput(String userInput) {
        //if user input is null or length is to little input can not be converted
        if (userInput == null || userInput.length() < 2) {
            return null;
        }

        //trim the imput
        userInput = userInput.trim().toLowerCase();

        //The letter should be at the first position
        char letter = userInput.charAt(0);
        //The number should be at the second position
        char number = userInput.charAt(1);

        //convert to board coordinates
        int yPos = number - '1';
        int xPos = letter - 'a';

        //Get the square from the board and create a game move
        Square square = gameModel.getGamePosition().getBoard().getSquare(xPos, yPos);
        return new GameMove(square);
    }
}
