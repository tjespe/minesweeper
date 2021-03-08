package minesweeper.model;

public class Field {
    private boolean hasBomb;
    private boolean isOpened;
    private boolean isFlagged;

    protected static final char UNOPENED = 'u';
    protected static final char FLAGGED = 'f';
    protected static final char OPENED = 'o';
    protected static final char BOMB = 'b';

    public Field(boolean bomb) {
        this.hasBomb = bomb;
        this.isOpened = false;
        this.isFlagged = false;
    }

    public char getStatus() {
        if (!isOpened) {
            if (isFlagged)
                return FLAGGED;
            return UNOPENED;
        }
        if (hasBomb)
            return BOMB;
        return OPENED;
    }

    public void toggleFlag() {
        this.isFlagged = !this.isFlagged;
    }

    public void open() throws IllegalStateException {
        if (this.isOpened)
            throw new IllegalStateException("This field has already been opened");
        if (this.isFlagged)
            throw new IllegalStateException("You cannot open a flagged field");
        this.isOpened = true;
    }

    public boolean getHasBomb() {
        return this.hasBomb;
    }

    public boolean getIsFlagged() {
        return this.isFlagged;
    }

    public boolean getIsOpened() {
        return this.isOpened;
    }

    public String toString(int adjacentBombs) {
        if (!isOpened) {
            if (isFlagged)
                return "⛳";
            return "⬜️";
        }
        if (hasBomb)
            return "💣";
        if (adjacentBombs > 0)
            return " " + Integer.toString(adjacentBombs);
        return "  ";
    }

}
