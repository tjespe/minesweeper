package minesweeper.model;

public class Score implements Comparable<Score> {
	private String name;
	private String time;
	private DifficultyLevel level;

	public Score(String name, String time, DifficultyLevel level) {
		this.name = name;
		this.time = time;
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}
	
	public DifficultyLevel getDifficultyLevel() {
		return level;
	}

	@Override
	public int compareTo(Score score) {
		return this.getTime().compareTo(score.getTime());
	}

	@Override
	public String toString() {
		return getName() + "," + getTime() + "," + (this.level != null ? this.level.getLabel() : "Unknown");
	}
}
