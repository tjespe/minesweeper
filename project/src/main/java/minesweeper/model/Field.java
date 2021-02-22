package minesweeper.model;

public class Field {
    private boolean hasBomb;
    private boolean isOpened;
    private boolean isFlagged;

    public Field(boolean bomb) {
        this.hasBomb = bomb;
        this.isOpened = false;
        this.isFlagged = false;
    }

    public void toggleFlag() {
        this.isFlagged = !this.isFlagged;
    }

    public void open() throws IllegalStateException {
        if (this.isOpened)
            throw new IllegalStateException("This field has already been opened");
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
            return Integer.toString(adjacentBombs);
        return " ";
    }

}
