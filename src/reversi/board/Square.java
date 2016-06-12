/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.board;

/**
 * This class represents a square on the reversi board
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public final class Square {
    private int xPosition;
    private int yPosition;
    private SquareState squareState;
    private boolean cornerSquare;

    Square(int xPosition, int yPosition) {
        this(xPosition, yPosition, SquareState.EMPTY);
    }

    Square(int xPosition, int yPosition, SquareState squareState) {
        this(xPosition, yPosition, squareState, false);
    }

    Square(int xPosition, int yPosition, SquareState squareState, boolean cornerSquare) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.squareState = squareState;
        this.cornerSquare = cornerSquare;
    }

    public String getSquareName() {
        char character = (char) ('A' + this.xPosition);
        int number = yPosition + 1;

        String result = character + "" + number;

        return result;
    }

    public SquareState getSquareState() {
        return squareState;
    }

    public boolean isCornerSquare() {
        return cornerSquare;
    }

    public int getXPosition() {
        return this.xPosition;
    }

    public int getYPosition() {
        return this.yPosition;
    }

    void setSquareState(SquareState squareState) {
        this.squareState = squareState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Square square = (Square) o;

        if (xPosition != square.xPosition) return false;
        if (yPosition != square.yPosition) return false;
        return squareState == square.squareState;

    }

    @Override
    public int hashCode() {
        int result = xPosition;
        result = 31 * result + yPosition;
        result = 31 * result + squareState.hashCode();
        return result;
    }
}
