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
 * This class is Responsible for weighing squares of a board.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 25. July 2016
 */
public class SquareWeigher {
    /*
     * The WEIGHTS FOR DIFFERENT SQUARES
     */
    //The weight of a disc on a corner
    private static final int WEIGHT_CORNER = 120;
    //The weight of an bad edge square (also known as C square)
    private static final int WEIGHT_EDGE_BAD = -20;
    //The weight of an good edge square (also known as A square)
    private static final int WEIGHT_EDGE_GOOD = 20;
    //The weight of a disk on an default edge square (also known as B square)
    private static final int WEIGHT_EDGE_DEFAULT = 5;
    //The weight of a disk on a board square that is diagonally adjacent to a corner (also known as X square)
    private static final int WEIGHT_BOARD_EDGE_BAD = -40;
    //The weight of a disk on a board square that is vertically and diagonally adjacent to the board edges
    private static final int WEIGHT_BOARD_BAD = -5;
    //The weight of a disk on a board square that is diagonally adjacent to a X square
    private static final int WEIGHT_BOARD_GOOD = 15;
    //The weight of a default inner board square
    private static final int WEIGHT_BOARD_DEFAULT = 3;

    private static final List<SquareWeigher> squareWeigherList = new ArrayList<>();

    //array representing the weights of different squares
    private final int[][] squareWeights;
    private final int boardWidth;
    private final int boardHeight;

    private SquareWeigher(Board board) {
        boardWidth = board.getBoardWidth();
        boardHeight = board.getBoardHeight();

        squareWeights = new int[boardWidth][boardHeight];

        initialize(board);
    }

    /**
     * This method makes sure that for each board size there is only one SquareWeigher
     *
     * @param board The board for which the weigher is needed
     * @return The SquareWeigher for the board.
     */
    public static SquareWeigher getInstance(Board board) {
        SquareWeigher instance = null;

        //Search if there is already a square weigher existing
        for (SquareWeigher weigher : squareWeigherList) {
            if (weigher.boardWidth == board.getBoardWidth() && weigher.boardHeight == board.getBoardHeight()) {
                instance = weigher;
            }
        }

        //If there is no existing square weigher then create a new one
        if (instance == null) {
            instance = new SquareWeigher(board);
            squareWeigherList.add(instance);
        }

        return instance;
    }

    /**
     * The method is used to get the square weight of a given square based on its location on the board.
     *
     * @param square The square whose weight should be gotten.
     * @return The weight value of the square
     */
    public int getWeighValue(Square square) {
        //get the x and y position of the square
        int xPos = square.getXPosition();
        int yPos = square.getYPosition();

        //return the value stored in the array
        return squareWeights[xPos][yPos];
    }

    private void initialize(Board board) {
        for (int x = 0; x < squareWeights.length; x++) {
            for (int y = 0; y < squareWeights[x].length; y++) {
                squareWeights[x][y] = getSquareWeight(board.getSquare(x, y));
            }

        }
    }

    /**
     * This method calculates the square weight for each square of the board.
     *
     * @param square The square for which the value should be calculated.
     * @return The weighted value of the given square
     */
    private int getSquareWeight(Square square) {
        //if square is in corner
        if (square.isCornerSquare()) {
            //get the corner weight value
            return WEIGHT_CORNER;
        }

        //if square is an edge square but no corner square
        if (square.isEdgeSquare()) {
            //if the edge square is one square away from the corner then return WEIGHT_EDGE_BAD
            if (isXSquaresAwayFromCorner(square, 1)) {
                return WEIGHT_EDGE_BAD;
            }

            //if the edge square is two squares from the corner away return WEIGHT_EDGE_GOOD
            if (isXSquaresAwayFromCorner(square, 2)) {
                return WEIGHT_EDGE_GOOD;
            }

            //if the edge square is an ordinary edge square return the default value
            return WEIGHT_EDGE_DEFAULT;
        }

        //now only inner board squares are left
        //if the inner square is one square away from the corner it is an bad inner square
        if (isXSquaresAwayFromCorner(square, 1)) {
            return WEIGHT_BOARD_EDGE_BAD;
        }

        //if the inner square is two squares away from the corner it is an good inner square
        if (isXSquaresAwayFromCorner(square, 2)) {
            return WEIGHT_BOARD_GOOD;
        }

        //the bad inner board squares, because with them a player can reach the edge.
        if (isXSquaresAwayFromEdge(square, 1)) {
            return WEIGHT_BOARD_BAD;
        }

        //if it is only an ordinary inner square return the default value
        return WEIGHT_BOARD_DEFAULT;
    }

    /**
     * This method is used to check if a square is a given number of squares from the corner away. The direction can
     * either be horizontal, vertical or diagonal.
     *
     * @param square   The square that should be checked
     * @param distance How far the square should be away from the corner.
     * @return true if the square is exactly the given number of squares away - false if otherwise
     */
    private boolean isXSquaresAwayFromCorner(Square square, int distance) {
        //result is by default false
        boolean result = false;

        //Calculate the corners only and then check the x and y Difference
        for (int x = 0; x < boardWidth; x += boardWidth - 1) {
            int xDiff = Math.abs(square.getXPosition() - x);

            for (int y = 0; y < boardHeight; y += boardHeight - 1) {
                int yDiff = Math.abs(square.getYPosition() - y);

                //if the difference is the given distance true otherwise false
                result |= (xDiff == distance && yDiff == 0) ||
                        (xDiff == 0 && yDiff == distance) ||
                        (xDiff == distance && yDiff == distance);
            }
        }

        return result;
    }

    /**
     * This method is used to check if a square is a given number of square from the edge away. The direction can either
     * be horizontal or vertical.
     *
     * @param square   The square that should be checked.
     * @param distance How far the square should be away from the edge.
     * @return true if the square is exactly the given number of squares away - false if otherwise
     */
    private boolean isXSquaresAwayFromEdge(Square square, int distance) {
        //result should be by default false
        boolean result = false;

        //calculate all edge squares and then check the x and y difference
        for (int x = 0; x < boardWidth; x += boardWidth - 1) {
            int xDiff = Math.abs(square.getXPosition() - x);

            for (int y = 0; y < boardHeight; y += boardHeight - 1) {
                int yDiff = Math.abs(square.getYPosition() - y);

                //if the difference is the given distance this will be true otherwise false
                result |= (xDiff == distance) || (yDiff == distance);
            }
        }

        return result;
    }
}
