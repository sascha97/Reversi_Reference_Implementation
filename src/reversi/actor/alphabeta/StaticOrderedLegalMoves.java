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

package reversi.actor.alphabeta;

import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.Square;
import reversi.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This is one legal move sort algorithm, the moves are sorted by their weight value.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 23. July 2016
 */
public class StaticOrderedLegalMoves implements OrderedLegalMoves {
    //the constants that represent the weight of the square
    private static final int WEIGHT_CORNER = 120;
    private static final int WEIGHT_EDGE_BAD = -20;
    private static final int WEIGHT_EDGE_GOOD = 20;
    private static final int WEIGHT_EDGE_DEFAULT = 5;
    private static final int WEIGHT_BOARD_EDGE_BAD = -40;
    private static final int WEIGHT_BOARD_BAD = -5;
    private static final int WEIGHT_BOARD_GOOD = 15;
    private static final int WEIGHT_BOARD_DEFAULT = 3;
    //an int array representing the weights of the squares
    private int[][] SQUARE_WEIGHTS;
    /**
     * This comparator is used to compare two game moves with each other.
     */
    private final Comparator<GameMove> GAME_MOVE_COMPARATOR = new Comparator<GameMove>() {
        @Override
        public int compare(GameMove gameMove1, GameMove gameMove2) {
            //get the squares
            Square square1 = gameMove1.getSquare();
            Square square2 = gameMove2.getSquare();

            //get the square weights
            int weigh1 = weighSquare(square1);
            int weigh2 = weighSquare(square2);

            return Integer.compare(weigh2, weigh1);
        }
    };
    //boolean flag to indicate if weights is already initialized
    private boolean weightsInitialized = false;

    @Override
    public List<GameMove> getSortedList(Board board, Player player) {
        //if weights is not initialized initialize weight
        if (!weightsInitialized) {
            initializeSquareWeights(board);
        }

        //Get all legal moves
        List<GameMove> legalMoveList = board.getAllLegalMoves(player);

        //Insert them into a modifiable list
        List<GameMove> sortable = new ArrayList<>();
        sortable.addAll(legalMoveList);

        //Sort them using the game move comparator
        Collections.sort(sortable, GAME_MOVE_COMPARATOR);
        return sortable;
    }

    /**
     * This method is used to initialize the square weights
     */
    private void initializeSquareWeights(Board board) {
        //get the height and width from the board
        int boardWidth = board.getBoardWidth();
        int boardHeight = board.getBoardHeight();

        //create the weights array
        SQUARE_WEIGHTS = new int[boardWidth][boardHeight];

        //initialize the array with the right values
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                SQUARE_WEIGHTS[x][y] = getSquareWeight(board.getSquare(x, y), boardWidth, boardHeight);
            }
        }

        //set weights initialized true
        weightsInitialized = true;
    }

    /**
     * This method calculates the square weight for each square of the board.
     *
     * @param square      The square for which the value should be calculated.
     * @param boardWidth  The width of the board
     * @param boardHeight The height of the board
     * @return The weighted value of the given square
     */
    private int getSquareWeight(Square square, int boardWidth, int boardHeight) {
        //if square is in corner
        if (square.isCornerSquare()) {
            //get the corner weight value
            return WEIGHT_CORNER;
        }

        //if square is an edge square but no corner square
        if (square.isEdgeSquare()) {
            //if the edge square is one square away from the corner then return WEIGHT_EDGE_BAD
            if (isXSquaresAwayFromCorner(square, 1, boardWidth, boardHeight)) {
                return WEIGHT_EDGE_BAD;
            }

            //if the edge square is two squares from the corner away return WEIGHT_EDGE_GOOD
            if (isXSquaresAwayFromCorner(square, 2, boardWidth, boardHeight)) {
                return WEIGHT_EDGE_GOOD;
            }

            //if the edge square is an ordinary edge square return the default value
            return WEIGHT_EDGE_DEFAULT;
        }

        //now only inner board squares are left
        //if the inner square is one square away from the corner it is an bad inner square
        if (isXSquaresAwayFromCorner(square, 1, boardWidth, boardHeight)) {
            return WEIGHT_BOARD_EDGE_BAD;
        }

        //if the inner square is two squares away from the corner it is an good inner square
        if (isXSquaresAwayFromCorner(square, 2, boardWidth, boardHeight)) {
            return WEIGHT_BOARD_GOOD;
        }

        //the bad inner board squares, because with them a player can reach the edge.
        if (isXSquaresAwayFromEdge(square, 1, boardWidth, boardHeight)) {
            return WEIGHT_BOARD_BAD;
        }

        //if it is only an ordinary inner square return the default value
        return WEIGHT_BOARD_DEFAULT;
    }

    /**
     * This method is used to check if a square is a given number of squares from the corner away. The direction can
     * either be horizontal, vertical or diagonal.
     *
     * @param square      The square that should be checked
     * @param distance    How far the square should be away from the corner.
     * @param boardWidth  The width of the board
     * @param boardHeight The height of the board
     * @return true if the square is exactly the given number of squares away - false if otherwise
     */
    private boolean isXSquaresAwayFromCorner(Square square, int distance, int boardWidth, int boardHeight) {
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
     * @param square      The square that should be checked.
     * @param distance    How far the square should be away from the edge.
     * @param boardWidth  The width of the board.
     * @param boardHeight The height of the board.
     * @return true if the square is exactly the given number of squares away - false if otherwise
     */
    private boolean isXSquaresAwayFromEdge(Square square, int distance, int boardWidth, int boardHeight) {
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

    /**
     * This method is used to get the square weight of a given square
     *
     * @param square The square whose weight is needed.
     * @return The value of the weight of the square.
     */
    private int weighSquare(Square square) {
        //get the x and y position of the square
        int x = square.getXPosition();
        int y = square.getYPosition();

        //return the value stored in the array
        return SQUARE_WEIGHTS[x][y];
    }
}
