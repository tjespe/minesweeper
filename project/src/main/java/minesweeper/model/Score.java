package minesweeper.model;

public class Score implements Comparable<Score> {
	private String name;
	private String time;
	
	public Score(String name, String time) {
		this.name = name;
		this.time = time;
	}
	public String getName() {
		return name;
	}
	
	public String getTime() {
		return time;
	}
	
	@Override
	public int compareTo(Score score) {
		return this.getTime().compareTo(score.getTime());
	}
	
	@Override
	public String toString() {
		return getName() + "," + getTime();
	}
}
