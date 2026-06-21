package model;

import java.io.Serializable;

public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Symbol[][] grid;
    private int moveCount;

    public Board() {
        this.grid = new Symbol[3][3];
        reset();
    }

    public void reset() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                grid[row][col] = Symbol.EMPTY;
            }
        }
        moveCount = 0;
    }

    public boolean placeSymbol(int row, int col, Symbol symbol) {
        if (!isValidPosition(row, col) || !isEmpty(row, col)) {
            return false;
        }
        grid[row][col] = symbol;
        moveCount++;
        return true;
    }

    public boolean isEmpty(int row, int col) {
        return grid[row][col] == Symbol.EMPTY;
    }

    public Symbol getSymbolAt(int row, int col) {
        return grid[row][col];
    }

    public boolean isFull() {
        return moveCount == 9;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public Symbol checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (checkLine(grid[i][0], grid[i][1], grid[i][2])) {
                return grid[i][0];
            }
            if (checkLine(grid[0][i], grid[1][i], grid[2][i])) {
                return grid[0][i];
            }
        }
        if (checkLine(grid[0][0], grid[1][1], grid[2][2])) {
            return grid[0][0];
        }
        if (checkLine(grid[0][2], grid[1][1], grid[2][0])) {
            return grid[0][2];
        }
        return null;
    }

    private boolean checkLine(Symbol a, Symbol b, Symbol c) {
        return a != Symbol.EMPTY && a == b && b == c;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }

    public Board copy() {
        Board copy = new Board();
        for (int row = 0; row < 3; row++) {
            System.arraycopy(grid[row], 0, copy.grid[row], 0, 3);
        }
        copy.moveCount = this.moveCount;
        return copy;
    }
}