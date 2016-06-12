/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.board;

import reversi.player.Player;

import java.util.List;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public interface Board {
    int getBoardHeight();

    int getBoardWidth();

    int countDifference(Player player);

    int countPieces(Player player);

    SquareState getSquareState(int xPosition, int yPosition);

    boolean hasAnyLegalMoves(Player player);

    boolean hasAnyPlayerAnyLegalMoves();

    boolean isMoveLegal(GameMove gameMove, Player player);

    List<GameMove> getAllLegalMoves(Player player);

    GamePosition makeMove(GameMove move, Player player);

    Square getSquare(int xPosition, int yPosition);
}