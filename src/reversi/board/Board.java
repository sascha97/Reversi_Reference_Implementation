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

import reversi.player.Player;

import java.util.List;

/**
 * This Interface represents all methods that any board should have.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public interface Board {
    /**
     * This method returns the height of the Board.
     *
     * @return Height of the Board.
     */
    int getBoardHeight();

    /**
     * This method returns the width of the Board.
     *
     * @return Width of the Board.
     */
    int getBoardWidth();

    /**
     * This method is calculating the difference of the pieces on the board in the view of a player.
     *
     * @param player The player's view from which the Board should be evaluated.
     * @return The difference of pieces from the player's view.
     */
    int countDifference(Player player);

    /**
     * This method counts the pieces of a player.
     *
     * @param player The player whose pieces should be counted.
     *
     * @return The number of pieces of the given player.
     */
    int countPieces(Player player);

    /**
     * This method returns the SquareState of a square at a given position.
     * The position is dependent by an x and y value.
     *
     * @param xPosition The x-Position of the Square which state should be returned.
     * @param yPosition The y-Position of the Square which state should be returned.
     *
     * @return The SquareState of a Square at a given position.
     */
    SquareState getSquareState(int xPosition, int yPosition);

    /**
     * This method checks if a player has at least one valid move.
     *
     * @param player The player that should be checked.
     *
     * @return true if the player has at least one legal move - false if the player has no moves.
     */
    boolean hasAnyLegalMoves(Player player);

    /**
     * This method checks if any player of the game has at least one legal move to do.
     *
     * @return true if there is a move available - false if there are no more moves available.
     */
    boolean hasAnyPlayerAnyLegalMoves();

    /**
     * This method should check if a move of a player is valid.
     *
     * @param gameMove The move that should be checked.
     * @param player The player whose move should be checked.
     *
     * @return true if the move is valid - false if the move is not valid.
     */
    boolean isMoveLegal(GameMove gameMove, Player player);

    /**
     * This method should return a List containing all the legal moves of a player.
     *
     * @param player The player whose legal moves should be returned.
     *
     * @return A list containing all the legal moves of the given player.
     */
    List<GameMove> getAllLegalMoves(Player player);

    /**
     * This method is responsible for making a move in the game.
     * It should return a new GamePosition representing the new state of the game.
     *
     * @param move The move that should be made.
     * @param player The player whose turn it is.
     *
     * @return A new GamePosition representing the new state of the game.
     */
    GamePosition makeMove(GameMove move, Player player);

    /**
     * This method returns a square at a given position.
     *
     * @param xPosition The x-Position of a square.
     * @param yPosition The y-Position of a square.
     *
     * @return The Square at a given position, null if there is no square at the given position.
     */
    Square getSquare(int xPosition, int yPosition);
}