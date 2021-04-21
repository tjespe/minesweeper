package minesweeper.model;

public class Field {
    private boolean hasBomb;
    private boolean isOpened;
    private boolean isFlagged;

    public static final char UNOPENED = 'u';
    public static final char UNOPENED_WITH_BOMB = 'U';
    public static final char FLAGGED = 'f';
    public static final char FLAGGED_WITH_BOMB = 'F';
    public static final char OPENED = 'o';
    public static final char BOMB = 'b';

    public Field(boolean bomb) {
        this.hasBomb = bomb;
        this.isOpened = false;
        this.isFlagged = false;
    }

    public Field(char status) {
        this.isFlagged = status == FLAGGED || status == FLAGGED_WITH_BOMB;
        this.hasBomb = status == BOMB || status == FLAGGED_WITH_BOMB || status == UNOPENED_WITH_BOMB;
        this.isOpened = status == OPENED;
    }

    public char getStatus() {
        if (!isOpened) {
            if (isFlagged)
                return hasBomb ? FLAGGED_WITH_BOMB : FLAGGED;
            if (hasBomb)
                return UNOPENED_WITH_BOMB;
            return UNOPENED;
        }
        if (hasBomb)
            return BOMB;
        return OPENED;
    }

    public void toggleFlag() {
        if (this.isOpened)
            throw new IllegalStateException("You cannot flag an opened field");
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
