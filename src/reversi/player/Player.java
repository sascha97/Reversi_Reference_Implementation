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
package reversi.player;

import reversi.board.SquareState;

/**
 * An enumeration of all players of the game.
 *
 * @author Sascha Lutzenberger
 * @version 1.1 - 23. July 2016
 */
public enum Player {
    BLACK(SquareState.BLACK),
    WHITE(SquareState.WHITE),
    NONE(SquareState.EMPTY);

    //The SquareState of the player.
    private final SquareState squareState;

    /**
     * Creates a new Player with a given squareState.
     *
     * @param squareState The squareState of a player
     */
    Player(SquareState squareState) {
        this.squareState = squareState;
    }

    /**
     * This method returns the SquareState of a player.
     *
     * @return The squareState of a player.
     */
    public SquareState getSquareState() {
        return squareState;
    }

    /**
     * This method returns the opponent of the player.
     *
     * @return The opponent of the player.
     */
    public Player getOpponent() {
        if (this == Player.NONE) {
            return Player.NONE;
        }

        return (this == Player.BLACK) ? Player.WHITE : Player.BLACK;
    }
}

