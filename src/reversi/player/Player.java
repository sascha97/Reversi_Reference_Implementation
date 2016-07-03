/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.player;

import reversi.board.SquareState;

/**
 * An enumeration of all players of the game.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public enum Player {
    BLACK(SquareState.BLACK),
    WHITE(SquareState.WHITE);

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
        return (this == Player.BLACK) ? Player.WHITE : Player.BLACK;
    }
}

