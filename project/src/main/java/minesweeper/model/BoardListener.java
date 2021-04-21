package minesweeper.model;

/**
 * This interface can be implemented by classes that want to listen to changes
 * on a Board.
 */
public interface BoardListener {
    public void boardChanged(Board board);
}
