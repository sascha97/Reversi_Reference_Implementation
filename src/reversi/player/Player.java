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
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public enum Player {
    BLACK(SquareState.BLACK),
    WHITE(SquareState.WHITE);

    private SquareState squareState;

    Player(SquareState squareState) {
        this.squareState = squareState;
    }

    public SquareState getSquareState() {
        return squareState;
    }

    public Player getOpponent() {
        return (this == Player.BLACK) ? Player.WHITE : Player.BLACK;
    }
}

