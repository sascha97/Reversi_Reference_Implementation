/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.board;

/**
 * This class Represents a GameMove made by any player.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public final class GameMove {
    //The Square where the Player wants to move to.
    private final Square square;

    /**
     * Constructor of GameMove.
     *
     * @param square The square of the game move.
     */
    public GameMove(Square square) {
        this.square = square;
    }

    /**
     * This method returns the square where the player wants to move to.
     *
     * @return
     */
    public Square getSquare() {
        return square;
    }
}
