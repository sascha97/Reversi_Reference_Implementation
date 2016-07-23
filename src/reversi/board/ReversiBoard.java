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

import reversi.game.ReversiGameConfiguration;
import reversi.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This is the implementation of the Board for a Reversi Game.
 *
 * All the rules of how to make a move in a reversi game are implemented in here.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public class ReversiBoard implements Board {
    private final static int BOARD_SIZE;

    static {
        //Load board size from configuration
        ReversiGameConfiguration configuration = ReversiGameConfiguration.getInstance();
        //Load a board size from the configuration, if no value is available use 8 as default size
        String sBoardSize = configuration.getProperty(ReversiGameConfiguration.BOARD_SIZE, "8");
        BOARD_SIZE = Integer.parseInt(sBoardSize);
    }

    //The squares of the board
    private final Square[][] squares;

    public ReversiBoard() {
        //initialize the array
        squares = new Square[BOARD_SIZE][BOARD_SIZE];

        //create all squares as empty squares
        initializeBoard();
        //set the start position to the board
        setUpStartPosition();
    }

    /**
     * This method is initializing the ReversiBoard so that every Square of the ReversiBoard contains at least an
     * empty square.
     */
    private void initializeBoard() {
        //iterates over the array and initializes each element with an empty square
        for (int x = 0; x < squares.length; x++) {
            for (int y = 0; y < squares[x].length; y++) {
                //Create a new Square and assign it to the ReversiBoard
                Square square = new Square(x, y, SquareState.EMPTY);
                squares[x][y] = square;
            }
        }
    }

    /**
     * This method is setting up the start position of any reversi game.
     */
    private void setUpStartPosition() {
        //calculates the coordinates of the center of the board
        int x = (BOARD_SIZE / 2) - 1;
        int y = (BOARD_SIZE / 2) - 1;

        //set the four center squares of the board to the Reversi start position
        squares[x][y].setSquareState(SquareState.WHITE);
        squares[x][y + 1].setSquareState(SquareState.BLACK);
        squares[x + 1][y].setSquareState(SquareState.BLACK);
        squares[x + 1][y + 1].setSquareState(SquareState.WHITE);
    }

    /**
     * This method returns the width of the ReversiBoard.
     *
     * @return Width of the ReversiBoard
     */
    @Override
    public int getBoardHeight() {
        return BOARD_SIZE;
    }

    /**
     * This method returns the height of the ReversiBoard
     *
     * @return Height of the ReversiBoard
     */
    @Override
    public int getBoardWidth() {
        return BOARD_SIZE;
    }

    /**
     * This method is calculating the difference of the pieces on the board in the view of a player.
     *
     * @param player The player's view from which the ReversiBoard should be evaluated.
     * @return The difference of pieces from the player's view.
     */
    @Override
    public int countDifference(Player player) {
        //number of own pieces
        int ownPieces = countPieces(player);
        //number of opponents pieces
        int opponentPieces = countPieces(player.getOpponent());

        //return the difference
        return ownPieces - opponentPieces;
    }

    /**
     * This method counts the pieces of a player.
     *
     * @param player The player whose pieces should be counted.
     *
     * @return The number of pieces of the given player.
     */
    @Override
    public int countPieces(Player player) {
        //Get the square state of the player
        SquareState squareState = player.getSquareState();

        //count the pieces of the player by iterating over all elements
        int numberOfPieces = 0;
        for (int x = 0; x < squares.length; x++) {
            for (int y = 0; y < squares[x].length; y++) {
                Square square = squares[x][y];

                //if SquareState matches the SquareState of the player increase the numberOfPieces by 1
                if (square.getSquareState() == squareState) {
                    numberOfPieces = numberOfPieces + 1;
                }
            }
        }

        return numberOfPieces;
    }

    /**
     * This method returns the SquareState of a square at a given position.
     * The position is dependent by an x and y value.
     *
     * @param xPosition The x-Position of the Square which state should be returned.
     * @param yPosition The y-Position of the Square which state should be returned.
     *
     * @return The SquareState of a Square at a given position.
     */
    @Override
    public SquareState getSquareState(int xPosition, int yPosition) {
        return getSquare(xPosition, yPosition).getSquareState();
    }

    /**
     * This method checks if a player has any legal moves to do.
     *
     * @param player The player that should be checked.
     *
     * @return true if the player has at least one legal move - false if the player has no moves.
     */
    @Override
    public boolean hasAnyLegalMoves(Player player) {
        //gets all legal moves for a player and then checks if list is not empty
        List<GameMove> legalMoves = getAllLegalMoves(player);

        return !(legalMoves.isEmpty());
    }

    /**
     * This method checks if any player has any legal moves.
     *
     * @return true if any player has at least one legal move - false if no player has any legal move.
     */
    @Override
    public boolean hasAnyPlayerAnyLegalMoves() {
        boolean result = false;

        //Iterate over all players.
        for (Player player : Player.values()) {
            //Check if a player has any legal moves.
            if (hasAnyLegalMoves(player)) {
                //If any player has at least one legal move end the loop by setting result to true.
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * This method checks if a given game move of a given player is legal.
     *
     * @param gameMove The GameMove which should be checked.
     * @param player The Player whose move should be checked.
     *
     * @return true if the move is legal - false if the move is not legal.
     */
    @Override
    public boolean isMoveLegal(GameMove gameMove, Player player) {
        //If game move or the square of the game move is null move can not be legal. Same for the player.
        if (gameMove == null || gameMove.getSquare() == null || player == null) {
            return false;
        }

        //Get the square of the game move.
        Square square = gameMove.getSquare();

        //If square is not an empty square move is illegal.
        if (!isEmptySquare(square)) {
            return false;
        }

        //checks if the move would encapsulate at least one piece of the opponent.
        return encapsulatesOpponentsSquare(square, player);
    }

    /**
     * This method returns a List<GameMove> of all the legal moves that can be made by a given player.
     *
     * @param player The player whose legal moves should be returned.
     *
     * @return A List<GameMove> containing all the legal moves of a player.
     */
    @Override
    public List<GameMove> getAllLegalMoves(Player player) {
        List<GameMove> legalMoveList = new ArrayList<>();

        //Iterate over all squares to check if there is any legal move.
        for (int x = 0; x < squares.length; x++) {
            for (int y = 0; y < squares[x].length; y++) {
                //The square of the board.
                Square square = squares[x][y];

                //Make sure only the empty squares are checked.
                if (!isEmptySquare(square)) {
                    continue;
                }

                //If the GameMove is legal add it to the list otherwise do not add the GameMove to the list.
                GameMove gameMove = new GameMove(square);
                if (isMoveLegal(gameMove, player)) {
                    legalMoveList.add(gameMove);
                }
            }
        }

        return Collections.unmodifiableList(legalMoveList);
    }

    /**
     * This method makes any move and returns a new GamePosition.
     *
     * @param move The move that should be made.
     * @param player The player whose move should be made
     *
     * @return A new GamePosition representing the new state of the game.
     */
    @Override
    public GamePosition makeMove(GameMove move, Player player) {
        //The next Board
        ReversiBoard nextBoard = this;
        //The next player whose turn it is.
        Player nextPlayer = player;

        //Check if move is valid
        if (isMoveLegal(move, player)) {
            //Create a new board because any ReversiBoard should be immutable.
            nextBoard = new ReversiBoard();

            //Set the SquareStates to the new board
            for (int x = 0; x < nextBoard.squares.length; x++) {
                for (int y = 0; y < nextBoard.squares[x].length; y++) {
                    //Set the same square state to the new board.
                    nextBoard.squares[x][y].setSquareState(squares[x][y].getSquareState());
                }
            }

            //Get the Square of the GameMove and then the x and y Position of the square
            Square square = move.getSquare();
            int x = square.getXPosition();
            int y = square.getYPosition();

            //Set the square to the square of the new player
            nextBoard.squares[x][y].setSquareState(player.getSquareState());

            //flip the disks
            flipPieces(square, player, nextBoard);

            //change the player
            nextPlayer = player.getOpponent();
            boolean changePlayer = nextBoard.hasAnyLegalMoves(nextPlayer);
            if (!changePlayer) {
                nextPlayer = nextPlayer.getOpponent();
            }
        }

        return new GamePosition(nextBoard, nextPlayer);
    }

    /**
     * This method returns a square at a given position.
     *
     * @param xPosition The x-Position of a square.
     * @param yPosition The y-Position of a square.
     *
     * @return The Square at a given position. null if no square is available at that position.
     */
    @Override
    public Square getSquare(int xPosition, int yPosition) {
        Square square = null;

        try {
            square = squares[xPosition][yPosition];
        } catch (IndexOutOfBoundsException e) {
            //Nothing that can be done here
        }

        return square;
    }

    /**
     * This method checks if an opponents square is encapsulated.
     *
     * @param square The square that should encapsulate an opponents square.
     * @param player The player whose turn it is.
     *
     * @return true if the square encapsulates an opponents square - false if not
     */
    private boolean encapsulatesOpponentsSquare(Square square, Player player) {
        //get the x and y position of the square
        int xPosition = square.getXPosition();
        int yPosition = square.getYPosition();

        boolean result = false;

        //This loop checks in all directions
        //dx is  the change in x direction
        for (int dx = -1; dx <= 1; dx++) {
            //dy is the change in y direction
            for (int dy = -1; dy <= 1; dy++) {
                //if there is no change continue the loop
                if (dx == 0 && dy == 0) {
                    continue;
                }

                //In one direction an opponent square has to be encapsulated.
                result = result || encapsulatesOpponentSquareInDirection(xPosition, yPosition, dx, dy, player);
            }
        }

        return result;
    }

    /**
     * This method flips all the pieces if an opponents square is encapsulated.
     *
     * @param square The square that encapsulates the opponents squares.
     * @param player The player whose turn it is.
     * @param reversiBoard The board where the pieces should be flipped
     */
    private void flipPieces(Square square, Player player, ReversiBoard reversiBoard) {
        //get the x and y position of the square
        int xPosition = square.getXPosition();
        int yPosition = square.getYPosition();

        //This loop checks in all directions
        //dx is the change in the x direction
        for (int dx = -1; dx <= 1; dx++) {
            //dy is the change in the y direction
            for (int dy = -1; dy <= 1; dy++) {
                //if there is no change continue the loop
                if (dx == 0 && dy == 0) {
                    continue;
                }

                //If an opponents square is encapsulated in this direction than flip the pieces in that direction
                if (encapsulatesOpponentSquareInDirection(xPosition, yPosition, dx, dy, player)) {
                    flipOpponentsPiecesInDirection(xPosition, yPosition, dx, dy, player, reversiBoard);
                }
            }
        }
    }

    /**
     * This method checks if an opponents square encapsulates opponent squares at a given position.
     *
     * @param xPosition The x-Position of the square.
     * @param yPosition The y-Position of the square.
     * @param dx        The change in the x direction.
     * @param dy        The change in the y direction.
     * @param player    The player whose turn it is.
     * @return true if an opponents square is encapsulated in that direction.
     */
    private boolean encapsulatesOpponentSquareInDirection(int xPosition, int yPosition, int dx, int dy, Player player) {
        //result must be false in the beginning
        boolean result = false;
        //no opponent square can be encapsulated in the beginning
        boolean opponentSquare = false;
        //The current square
        Square square;
        //Iterate over all squares in that direction if an square is null end the loop
        while ((square = getSquare(xPosition + dx, yPosition + dy)) != null) {
            //If the square is empty end the loop because then no opponents square will be encapsulated
            if (isEmptySquare(square)) {
                break;
            }
            //If the square is an opponents square set opponents square true
            if (isOpponentsSquare(square, player)) {
                opponentSquare = true;
                //if the square is then an own square result can only be true if an opponents square was found earlier.
                //and the loop will be left here because after that square no further encapsulation is possible.
            } else if (isOwnSquare(square, player)) {
                if (opponentSquare) {
                    result = true;
                }

                break;
            }

            //Change the xPosition and the yPosition so that the next square can be evaluated.
            xPosition = xPosition + dx;
            yPosition = yPosition + dy;
        }

        return result;
    }

    /**
     * This method flips all the pieces in a given direction to the pieces of the given player. There won't be any
     * check if this is legal because encapsulatesOpponentsSquareInDirection(int, int, int, int, Player) should be
     * called first to check if that is legal.
     *
     * @param xPosition The x-Position of the square.
     * @param yPosition The y-Position of the square.
     * @param dx The change in the x direction.
     * @param dy The change in the y direction.
     * @param player The player whose turn it is.
     * @param reversiBoard The board where the pieces should be flipped.
     */
    private void flipOpponentsPiecesInDirection(int xPosition, int yPosition, int dx, int dy,
                                                Player player, ReversiBoard reversiBoard) {
        Square square;
        //While the square states of the squares do not match the current player's square state change them to the
        //current player's square state
        while ((square = reversiBoard.getSquare(xPosition + dx, yPosition + dy)).getSquareState()
                != player.getSquareState()) {
            //Change the Square state to the own square state
            square.setSquareState(player.getSquareState());

            //Change the xPosition and the yPosition, so that the next square can be evaluated.
            xPosition = xPosition + dx;
            yPosition = yPosition + dy;
        }
    }

    /**
     * This method checks if an square is an empty square.
     *
     * @param square The square that should be checked.
     *
     * @return true if the square is an empty square - false if the square is not an empty square.
     */
    private boolean isEmptySquare(Square square) {
        //if square is null square can not be empty
        if (square == null) {
            return false;
        }

        return square.getSquareState() == SquareState.EMPTY;
    }

    /**
     * This method checks if an square is an own square.
     *
     * @param square The square that should be checked.
     * @param player The current player from whose view it should be checked.
     *
     * @return true if the square is an own square - false if the square is not an own square
     */
    private boolean isOwnSquare(Square square, Player player) {
        //get the opponent and check if the square is an opponents square (i.e. check if it is an own square)
        return isOpponentsSquare(square, player.getOpponent());
    }

    /**
     * This method checks if a square is an opponents square.
     *
     * @param square The square that should be checked.
     * @param player The current player from whose view it should be checked.
     *
     * @return true if the square is an opponents square - false if the square is not an opponents square.
     */
    private boolean isOpponentsSquare(Square square, Player player) {
        //If square or player is null square can not be an opponents square
        if (square == null || player == null) {
            return false;
        }

        //Get the opponent and then check if the square state matches.
        Player opponent = player.getOpponent();
        return square.getSquareState() == opponent.getSquareState();
    }

    /**
     * Checks if the board is equal. Any board can just be equal if the squares array is equal.
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

        ReversiBoard that = (ReversiBoard) o;

        //Deep equals check is only true of Square.equals(square) returns true.
        return Arrays.deepEquals(squares, that.squares);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ReversiBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}