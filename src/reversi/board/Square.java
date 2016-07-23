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
package reversi.board;

import reversi.game.ReversiGameConfiguration;

/**
 * This class represents a square on the reversi board
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public final class Square {
    //The configuration of a reversi game.
    private static final ReversiGameConfiguration configuration = ReversiGameConfiguration.getInstance();
    //The x-position of the Square on the board
    private final int xPosition;
    //The y-position of the Square on the board
    private final int yPosition;
    //The square state of the square
    private SquareState squareState;

    /**
     * Constructor to create a new Square.
     *
     * @param xPosition The x-position of the Square on the board
     * @param yPosition The y-position of the Square on the board
     */
    Square(int xPosition, int yPosition) {
        //Calls the other constructor assuming the square state is empty
        this(xPosition, yPosition, SquareState.EMPTY);
    }

    /**
     * Constructor to create a new Square with a given SquareState.
     *
     * @param xPosition   The x-position of the Square on the board.
     * @param yPosition   The y-position of the Square on the board.
     * @param squareState The SquareState of the square on the board.
     */
    Square(int xPosition, int yPosition, SquareState squareState) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.squareState = squareState;
    }

    /**
     * This method returns the size of the board loaded from the configuration.
     *
     * @return The board size.
     */
    private static int getBoardSize() {
        String sBoardSize = configuration.getProperty(ReversiGameConfiguration.BOARD_SIZE, "8");

        return Integer.parseInt(sBoardSize);
    }

    /**
     * This method returns a name of the square (e.g. A1, A2, A3, A4...)
     *
     * @return The name of the Square like in chess (A1, A2...)
     */
    public String getSquareName() {
        //Calculate the character part
        char character = (char) ('A' + this.xPosition);
        //Calculate the number part
        int number = yPosition + 1;

        return character + "" + number;
    }

    /**
     * This method returns the SquareState of the square.
     *
     * @return The SquareState of the square.
     */
    public SquareState getSquareState() {
        return squareState;
    }

    /**
     * This method allows it to modify the square state.
     *
     * @param squareState The SquareState to which the SquareState should be changed to.
     */
    void setSquareState(SquareState squareState) {
        //If squareState exists set the square state to the new square state.
        if (squareState != null) {
            this.squareState = squareState;
        }
    }

    /**
     * This method checks if a square is a corner square.
     *
     * @return true if the square is a corner square - false if not.
     */
    public boolean isCornerSquare() {
        //Get the size of the board
        int boardSize = getBoardSize();

        //check if the square is on the right position ot be a corner square
        return (xPosition == 0 && yPosition == 0) ||
                (xPosition == 0 && (yPosition + 1) == boardSize) ||
                ((xPosition + 1) == boardSize && (yPosition + 1) == boardSize) ||
                ((xPosition + 1) == boardSize && yPosition == 0);
    }

    /**
     * This method checks if a square is an edge square.
     *
     * @return true if the square is an edge square - false if not.
     */
    public boolean isEdgeSquare() {
        //Get the size of the board
        int boardSize = getBoardSize();

        //Check if the square is on the right position ot be an edge square
        return (xPosition == 0) || (yPosition == 0) || ((xPosition + 1) == boardSize) || ((yPosition + 1) == boardSize);
    }

    /**
     * This method returns the x-position of the square on the board.
     *
     * @return The x-position of the square on the board.
     */
    public int getXPosition() {
        return this.xPosition;
    }

    /**
     * This method returns the y-position of the square on the board.
     *
     * @return The y-position of the square on the board.
     */
    public int getYPosition() {
        return this.yPosition;
    }

    /**
     * Checks if a Square is equal. Any Square can just be equal if the x and y Position of a square and the
     * state of a square  are equal
     *
     * @param o The object that should be checked for equality.
     *
     * @return true if the passed in object is the same - false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Square square = (Square) o;

        if (xPosition != square.xPosition) {
            return false;
        }
        if (yPosition != square.yPosition) {
            return false;
        }
        return squareState == square.squareState;

    }

    @Override
    public int hashCode() {
        int result = xPosition;
        result = 31 * result + yPosition;
        result = 31 * result + squareState.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Square{" +
                "xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                ", squareState=" + squareState +
                '}';
    }
}
