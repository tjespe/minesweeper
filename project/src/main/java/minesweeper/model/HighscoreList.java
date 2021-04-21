package minesweeper.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HighscoreList {

	private List<Score> highScores = new ArrayList<>();
	
	public HighscoreList(String serializedHigscores) {
		String[] lines = serializedHigscores.split("\\n");
		for (String line : lines) {
			String name = line.split(",")[0];
			String time = line.split(",")[1];
			highScores.add(new Score(name, time));
		}
	}
	
	public HighscoreList() {
	}
	
	public Collection<Score> getAllhighScores() {
		return highScores.stream().collect(Collectors.toList());
	}

	public String getSerializedHighscores() {
		StringBuilder bld  = new StringBuilder(); 
		for (Score score : highScores) {
			bld.append(score);
			bld.append("\n");
		}
		return bld.toString();
	}
	
	public void addScore(Score score) {
		highScores.add(score);
	}
	
	public Collection<Score> getBestScores() {
		Collection<Score> bestScores = new ArrayList<>();
		Collections.sort(highScores);
		bestScores = highScores.subList(0, 4);
		return bestScores;
	}
	
	@Override
	public String toString() {
		return ;
	}
}
