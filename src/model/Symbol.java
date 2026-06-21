package model;

public enum Symbol {
    X('X'),
    O('O'),
    EMPTY(' ');

    private final char displayChar;

    Symbol(char displayChar) {
        this.displayChar = displayChar;
    }

    public char getDisplayChar() {
        return displayChar;
    }

    public Symbol getOpposite() {
        if (this == X) return O;
        if (this == O) return X;
        return EMPTY;
    }
}