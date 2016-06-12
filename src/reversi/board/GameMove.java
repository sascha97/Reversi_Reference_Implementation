/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.board;

import reversi.board.Square;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public final class GameMove {
    private final Square square;

    public GameMove(Square square) {
        this.square = square;
    }

    public Square getSquare() {
        return square;
    }
}
