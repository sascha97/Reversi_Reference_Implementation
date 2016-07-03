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

import java.util.Scanner;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 03. July 2016
 */
public class ConsoleGameController extends GameController {
    public ConsoleGameController(Game gameModel) {
        super(gameModel);
    }

    @Override
    public void play() {
        gameModel.play();
    }

    @Override
    protected GameMove convertInput(String userInput) {
        GameMove result;

        userInput = userInput.trim().toLowerCase();

        char letter = userInput.charAt(0);
        char number = userInput.charAt(1);

        int yPos = number - '1';
        int xPos = letter - 'a';

        Square square = gameModel.getGamePosition().getBoard().getSquare(xPos, yPos);
        result = new GameMove(square);

        return result;
    }
}
