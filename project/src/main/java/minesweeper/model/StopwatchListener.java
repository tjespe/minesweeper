package minesweeper.model;

public interface StopwatchListener {
	void timeChanged(String newTimeValue);

	void timeIsUp();
}
