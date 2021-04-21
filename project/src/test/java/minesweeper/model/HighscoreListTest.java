package minesweeper.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HighscoreListTest {
	
	
	@Test
	public void testConstuctionFromSerializedHighscoreList() {
		String serializedHighscoreList = "Kristoffer,01:30\n"
				//
				+
				"Kristoffer,01:30\n"
				//
				+
				"Tord,10:30\n"
				//
				+
				"Kristoffer,11:30\n";
		HighscoreList highscoreList = new HighscoreList(serializedHighscoreList);
		Assertions.assertEquals(4, highscoreList.getAllhighScores().size());				
	}
	@Test
	public void getSerializedHighscoresTest() {
		
	}
	
	@Test
	public void addScoreTest() {
		String serializedHighscoreList = "Kristoffer,01:30\n"
				//
				+
				"Kristoffer,01:30\n";
		HighscoreList highscoreList = new HighscoreList(serializedHighscoreList);
		highscoreList.addScore(new Score("Henrik", "00:37"));
		Assertions.assertEquals(3, highscoreList.getAllhighScores().size());	
	}
	
	@Test
	public void getBestScoresTest() {
		//TODO 
	}
	
	
	
	
	
	

}
