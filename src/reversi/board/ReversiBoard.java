/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.board;

import reversi.game.ReversiGameConfiguration;
import reversi.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public class ReversiBoard implements Board {
    private final int BOARD_SIZE;

    private Square[][] squares;

    public ReversiBoard() {
        ReversiGameConfiguration configuration = ReversiGameConfiguration.getInstance();

        String sSize = configuration.getProperty(ReversiGameConfiguration.BOARD_SIZE, "8");

        BOARD_SIZE = Integer.parseInt(sSize);

        squares = new Square[BOARD_SIZE][BOARD_SIZE];

        initializeBoard();
        setUpStartPosition();
    }

    private void initializeBoard() {
        for (int x = 0; x < squares.length; x++) {
            for (int y = 0; y < squares[x].length; y++) {
                Square square = new Square(x, y, SquareState.EMPTY);
                squares[x][y] = square;
            }
        }
    }

    private void setUpStartPosition() {
        int x = (BOARD_SIZE / 2) - 1;
        int y = (BOARD_SIZE / 2) - 1;

        squares[x][y].setSquareState(SquareState.WHITE);
        squares[x][y + 1].setSquareState(SquareState.BLACK);
        squares[x + 1][y].setSquareState(SquareState.BLACK);
        squares[x + 1][y + 1].setSquareState(SquareState.WHITE);
    }

    @Override
    public int getBoardHeight() {
        return BOARD_SIZE;
    }

    @Override
    public int getBoardWidth() {
        return BOARD_SIZE;
    }

    @Override
    public int countDifference(Player player) {
        int ownPieces = countPieces(player);
        int opponentPieces = countPieces(player.getOpponent());

        return ownPieces - opponentPieces;
    }

    @Override
    public int countPieces(Player player) {
        SquareState squareState = player.getSquareState();

        int numberOfPieces = 0;
        for (int x = 0; x < squares.length; x++) {
            for (int y = 0; y < squares[x].length; y++) {
                Square square = squares[x][y];

                if (square.getSquareState() == squareState) {
                    numberOfPieces = numberOfPieces + 1;
                }
            }
        }

        return numberOfPieces;
    }

    @Override
    public SquareState getSquareState(int xPosition, int yPosition) {
        return getSquare(xPosition, yPosition).getSquareState();
    }

    @Override
    public boolean hasAnyLegalMoves(Player player) {
        boolean result = false;

        for (int x = 0; x < squares.length; x++) {
            for (int y = 0; y < squares[x].length; y++) {
                Square square = squares[x][y];
                GameMove gameMove = new GameMove(square);

                if (isMoveLegal(gameMove, player)) {
                    result = true;
                    break;
                }
            }

            if (result) {
                break;
            }
        }

        return result;
    }

    @Override
    public boolean hasAnyPlayerAnyLegalMoves() {
        boolean result = false;
        for (Player player : Player.values()) {
            if (hasAnyLegalMoves(player)) {
                result = true;
                break;
            }
        }

        return result;
    }

    @Override
    public boolean isMoveLegal(GameMove gameMove, Player player) {
        if (gameMove == null || gameMove.getSquare() == null) {
            return false;
        }

        Square square = gameMove.getSquare();

        //If square is not empty move is illegal
        if (!isEmptySquare(square)) {
            return false;
        }

        boolean result = encapsulatesOpponentsSquare(square, player);

        return result;
    }

    @Override
    public List<GameMove> getAllLegalMoves(Player player) {
        List<GameMove> legalMoveList = new ArrayList<>();

        for (int x = 0; x < squares.length; x++) {
            for (int y = 0; y < squares[x].length; y++) {
                Square square = squares[x][y];

                GameMove gameMove = new GameMove(square);
                if (isMoveLegal(gameMove, player)) {
                    legalMoveList.add(gameMove);
                }
            }
        }

        return Collections.unmodifiableList(legalMoveList);
    }

    @Override
    public GamePosition makeMove(GameMove move, Player player) {
        //clone the board
        ReversiBoard newBoard = new ReversiBoard();

        Player currentPlayer = player;

        //Set the SquareStates to the new board
        for (int x = 0; x < newBoard.squares.length; x++) {
            for (int y = 0; y < newBoard.squares[x].length; y++) {
                newBoard.squares[x][y].setSquareState(squares[x][y].getSquareState());
            }
        }

        if (isMoveLegal(move, player)) {
            Square square = move.getSquare();
            int x = square.getXPosition();
            int y = square.getYPosition();

            //Set the square to the square of the new player
            newBoard.squares[x][y].setSquareState(player.getSquareState());

            //flip the disks
            flipPieces(square, player, newBoard);

            //change the player
            currentPlayer = player.getOpponent();
            boolean changePlayer = newBoard.hasAnyLegalMoves(currentPlayer);
            if (!changePlayer) {
                currentPlayer = currentPlayer.getOpponent();
            }
        }

        return new GamePosition(newBoard, currentPlayer);
    }

    @Override
    public Square getSquare(int xPosition, int yPosition) {
        Square square = null;

        try {
            square = squares[xPosition][yPosition];
        } catch (Exception e) {
            //Nothing that can be done here
        }

        return square;
    }

    private boolean encapsulatesOpponentsSquare(Square square, Player player) {
        int xPosition = square.getXPosition();
        int yPosition = square.getYPosition();

        boolean result = false;
        //from left to right
        result = result || encapsulatesOpponentSquareInDirection(xPosition, yPosition, 0, 1, player);
        //from right to left
        result = result || encapsulatesOpponentSquareInDirection(xPosition, yPosition, 0, -1, player);
        //downwards
        result = result || encapsulatesOpponentSquareInDirection(xPosition, yPosition, +1, 0, player);
        //upwards
        result = result || encapsulatesOpponentSquareInDirection(xPosition, yPosition, -1, 0, player);
        //diagonal down right
        result = result || encapsulatesOpponentSquareInDirection(xPosition, yPosition, +1, +1, player);
        //diagonal down left
        result = result || encapsulatesOpponentSquareInDirection(xPosition, yPosition, +1, -1, player);
        //diagonal up right
        result = result || encapsulatesOpponentSquareInDirection(xPosition, yPosition, -1, +1, player);
        //diagonal up left
        result = result || encapsulatesOpponentSquareInDirection(xPosition, yPosition, -1, -1, player);

        return result;
    }

    private boolean encapsulatesOpponentSquareInDirection(int xPosition, int yPosition, int dx, int dy, Player player) {
        boolean result = false;
        boolean opponentSquare = false;
        Square square = null;
        while ((square = getSquare(xPosition + dx, yPosition + dy)) != null) {
            if (isEmptySquare(square)) {
                break;
            }
            if (isOpponentsSquare(square, player)) {
                opponentSquare = true;
            } else if (isOwnSquare(square, player)) {
                if (opponentSquare) {
                    result = true;
                }

                break;
            }

            xPosition = xPosition + dx;
            yPosition = yPosition + dy;
        }

        return result;
    }

    private void flipPieces(Square square, Player player, ReversiBoard reversiBoard) {
        int xPosition = square.getXPosition();
        int yPosition = square.getYPosition();

        if (encapsulatesOpponentSquareInDirection(xPosition, yPosition, 0, 1, player)) {
            flipOpponentsPiecesInDirection(xPosition, yPosition, 0, 1, player, reversiBoard);
        }

        if (encapsulatesOpponentSquareInDirection(xPosition, yPosition, 0, -1, player)) {
            flipOpponentsPiecesInDirection(xPosition, yPosition, 0, -1, player, reversiBoard);
        }
        if (encapsulatesOpponentSquareInDirection(xPosition, yPosition, +1, 0, player)) {
            flipOpponentsPiecesInDirection(xPosition, yPosition, +1, 0, player, reversiBoard);
        }
        if (encapsulatesOpponentSquareInDirection(xPosition, yPosition, -1, 0, player)) {
            flipOpponentsPiecesInDirection(xPosition, yPosition, -1, 0, player, reversiBoard);
        }
        //diagonal down right
        if (encapsulatesOpponentSquareInDirection(xPosition, yPosition, +1, +1, player)) {
            flipOpponentsPiecesInDirection(xPosition, yPosition, +1, +1, player, reversiBoard);
        }
        //diagonal down left
        if (encapsulatesOpponentSquareInDirection(xPosition, yPosition, +1, -1, player)) {
            flipOpponentsPiecesInDirection(xPosition, yPosition, +1, -1, player, reversiBoard);
        }
        //diagonal up right
        if (encapsulatesOpponentSquareInDirection(xPosition, yPosition, -1, +1, player)) {
            flipOpponentsPiecesInDirection(xPosition, yPosition, -1, +1, player, reversiBoard);
        }
        //diagonal up left
        if (encapsulatesOpponentSquareInDirection(xPosition, yPosition, -1, -1, player)) {
            flipOpponentsPiecesInDirection(xPosition, yPosition, -1, -1, player, reversiBoard);
        }
    }

    private void flipOpponentsPiecesInDirection(int xPosition, int yPosition, int dx, int dy,
                                                Player player, ReversiBoard reversiBoard) {
        Square square = null;
        while ((square = reversiBoard.getSquare(xPosition + dx, yPosition + dy)).getSquareState()
                != player.getSquareState()) {
            square.setSquareState(player.getSquareState());

            xPosition = xPosition + dx;
            yPosition = yPosition + dy;
        }
    }

    private boolean isEmptySquare(Square square) {
        if (square == null)
            return false;

        return square.getSquareState() == SquareState.EMPTY;
    }

    private boolean isOwnSquare(Square square, Player player) {
        return isOpponentsSquare(square, player.getOpponent());
    }

    private boolean isOpponentsSquare(Square square, Player player) {
        if (square == null) {
            return false;
        }

        Player opponent = player.getOpponent();
        return square.getSquareState() == opponent.getSquareState();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReversiBoard that = (ReversiBoard) o;

        return Arrays.deepEquals(squares, that.squares);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}