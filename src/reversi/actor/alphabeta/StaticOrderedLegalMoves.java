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
import reversi.evaluation.SquareWeigher;
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
    private SquareWeigher squareWeigher;
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
            int weigh1 = squareWeigher.getWeighValue(square1);
            int weigh2 = squareWeigher.getWeighValue(square2);

            return Integer.compare(weigh2, weigh1);
        }
    };

    /**
     * This method is used to get a sorted list of game moves
     *
     * @param board  The board from which the moves should be gotten
     * @param player The player whose turn it is
     * @return A sorted list containing all valid moves
     */
    @Override
    public List<GameMove> getSortedList(Board board, Player player) {
        squareWeigher = SquareWeigher.getInstance(board);

        //Get all legal moves
        List<GameMove> legalMoveList = board.getAllLegalMoves(player);

        //Insert them into a modifiable list
        List<GameMove> sortable = new ArrayList<>();
        sortable.addAll(legalMoveList);

        //Sort them using the game move comparator
        Collections.sort(sortable, GAME_MOVE_COMPARATOR);
        return sortable;
    }
}
