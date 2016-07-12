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
 *   for using the code in any commerical projects from the author.
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

/**
 * This class represents a state of the game.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public class GamePosition {
    //The current board containing the state of the game
    private final Board board;
    //The player whose turn it is.
    private final Player currentPlayer;

    /**
     * Constructor of GamePosition
     *
     * @param board         The board containing the current state of the game.
     * @param currentPlayer The current player whose turn it is.
     */
    public GamePosition(Board board, Player currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
    }

    /**
     * This method returns the board that contains the current state of the game.
     *
     * @return The board containing the current state of the game.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * This method returns the current player whose turn it is.
     *
     * @return The player whose turn it is.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Checks if a GamePosition is equal. Any GamePosition can just be equal if the board and the currentPlayer are
     * equal.
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

        GamePosition position = (GamePosition) o;

        if (board != null ? !board.equals(position.board) : position.board != null) {
            return false;
        }
        return currentPlayer == position.currentPlayer;
    }

    @Override
    public int hashCode() {
        int result = board != null ? board.hashCode() : 0;
        result = 31 * result + (currentPlayer != null ? currentPlayer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GamePosition{" +
                "board=" + board +
                ", currentPlayer=" + currentPlayer +
                '}';
    }
}
