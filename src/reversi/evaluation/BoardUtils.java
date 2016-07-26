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

package reversi.evaluation;

import reversi.board.Board;
import reversi.board.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides utility functions for the board.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 26. July 2016
 */
public class BoardUtils {
    private BoardUtils() {

    }

    /**
     * This method returns a list containing all corner squares on the given board.
     *
     * @param board The board on which the corner squares should be found
     * @return A list containing all corner squares of the board
     */
    public static List<Square> getCornerSquares(Board board) {
        //get the width and height of the board
        int boardWidth = board.getBoardWidth();
        int boardHeight = board.getBoardHeight();

        //the list where all corners should be stored
        List<Square> corners = new ArrayList<>();

        //Iterate over the board (only the necessary steps are made)
        for (int x = 0; x < boardWidth; x += boardWidth - 1) {
            for (int y = 0; y < boardHeight; y += boardHeight - 1) {
                Square corner = board.getSquare(x, y);
                corners.add(corner);
            }
        }

        return corners;
    }

    /**
     * This method returns a list containing all edge squares on the given board. This also includes corner squares.
     *
     * @param board The board on which the edge squares should be found and returned.
     * @return A list containing all edge squares of the board
     */
    public static List<Square> getEdgeSquares(Board board) {
        //get the width and height of the board
        int boardWidth = board.getBoardWidth();
        int boardHeight = board.getBoardHeight();

        //this is the value that will be skipped if the square is not an edge square
        int skip = boardWidth - 2;

        //the list where all edges should be stored
        List<Square> edges = new ArrayList<>();

        //iterate over all squares
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                //get the square of the board
                Square square = board.getSquare(x, y);

                if (square.isEdgeSquare()) {
                    edges.add(square);
                } else {
                    //the unnecessary squares can be skipped
                    //skip - 1 because the first square is already checked
                    y = y + skip - 1;
                }
            }
        }

        return edges;
    }

    /**
     * This method returns a list containing all x squares on the given board.
     *
     * @param board The board on which the x squares should be found
     * @return A list containing all x squares of the board
     */
    public static List<Square> getXSquares(Board board) {
        //get the width and height of the board
        int boardWidth = board.getBoardWidth();
        int boardHeight = board.getBoardHeight();

        //subtract two from width and height to get a smaller "inner" board.
        int xSquareWidth = boardWidth - 2;
        int xSquareHeight = boardHeight - 2;

        //the list where all corners should be stored
        List<Square> xSquares = new ArrayList<>();

        //iterate over a smaller board
        for (int x = 1; x <= xSquareWidth; x += xSquareWidth - 1) {
            for (int y = 1; y <= xSquareHeight; y += xSquareHeight - 1) {
                Square xSquare = board.getSquare(x, y);

                xSquares.add(xSquare);
            }
        }

        return xSquares;
    }

    /**
     * This method returns all neighbour squares of a square.
     *
     * @param board  The board from which the squares should be gotten.
     * @param square The square whose neighbours are searched.
     * @return A list containing all neighbour squares
     */
    public static List<Square> getNeighbourSquares(Board board, Square square) {
        //the list where all the neighbours should be stored
        List<Square> neighbours = new ArrayList<>();


        //the x and y position of the square
        int xPos = square.getXPosition();
        int yPos = square.getYPosition();

        //Go in each direction of the square
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = xPos + dx;
                int newY = yPos + dy;

                //if this is the original square continue without doing something
                if (newX == xPos && newY == yPos) {
                    continue;
                }

                //Get the square (returns null if square is not existent)
                Square neighbour = board.getSquare(newX, newY);

                //Add the square to the neighbours list
                if (neighbour != null) {
                    neighbours.add(neighbour);
                }
            }
        }

        return neighbours;
    }
}
