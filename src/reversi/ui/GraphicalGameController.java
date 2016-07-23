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
     * This method converts the input from a graphical user interface to a game move.
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
