/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GamePosition position = (GamePosition) o;

        if (board != null ? !board.equals(position.board) : position.board != null) return false;
        return currentPlayer == position.currentPlayer;
    }

    @Override
    public int hashCode() {
        int result = board != null ? board.hashCode() : 0;
        result = 31 * result + (currentPlayer != null ? currentPlayer.hashCode() : 0);
        return result;
    }
}
