package minesweeper.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HighscoreListTest {
	
	@Test
	public void testConstuctionFromSerializedHighscoreList() {
		//create text serializedscore
		String serializedHighscoreList = 
				"Kristoffer,01:30,Normal\n"
				+ "Kristoffer,01:30,Easy\n"
				+ "Tord,10:30,Hard\n"
				+ "Kristoffer,11:30,Normal\n";
		
		//check constuctor with serializedscore to see if the correct amount of score is added to highscoreList
		HighscoreList highscoreList = new HighscoreList(serializedHighscoreList);
		Assertions.assertEquals(4, highscoreList.getAllhighScores().size());	
		
		
		List<Score> highscoreListFull = new ArrayList<>();
		highscoreListFull.add(new Score("Kristoffer", "01:30", DifficultyLevel.NORMAL));
		highscoreListFull.add(new Score("Kristoffer", "01:30", DifficultyLevel.EASY));
		highscoreListFull.add(new Score("Tord", "10:30", DifficultyLevel.HARD));
		highscoreListFull.add(new Score("Kristoffer", "11:30", DifficultyLevel.NORMAL));
		//check if the scores added is correct
		int i=0;
		for (Score score : highscoreList.getAllhighScores()) {
			Assertions.assertEquals(score.getName(), highscoreListFull.get(i).getName());
			Assertions.assertEquals(score.getTime(), highscoreListFull.get(i).getTime());
			Assertions.assertEquals(score.getDifficultyLevel() , highscoreListFull.get(i).getDifficultyLevel());
			i++;
		}
	}
	
	@Test
	public void testGetSerializedHighscores() {
		HighscoreList highscoreList = new HighscoreList();
		highscoreList.addScore(new Score("Kristoffer", "01:30", DifficultyLevel.NORMAL));
		highscoreList.addScore(new Score("Kristoff", "01:30", DifficultyLevel.EASY));
		highscoreList.addScore(new Score("Tord", "10:30", DifficultyLevel.HARD));
		
		String serializedHighscores = highscoreList.getSerializedHighscores();
		String[] lines = serializedHighscores.split("\\n");
		
		//check that the first line is what was excpected
		String[] line1 = lines[0].split(",");
		Assertions.assertEquals("Kristoffer", line1[0]);
		Assertions.assertEquals("01:30", line1[1]);
		Assertions.assertEquals(DifficultyLevel.NORMAL.getLabel(), line1[2]);
		
		//check that the second line is what was excpected
		String[] line2 = lines[1].split(",");
		Assertions.assertEquals("Kristoff", line2[0]);
		Assertions.assertEquals("01:30", line2[1]);
		Assertions.assertEquals(DifficultyLevel.EASY.getLabel(), line2[2]);
		
		//check that the third line is what was excpected
		String[] line3 = lines[2].split(",");
		Assertions.assertEquals("Tord", line3[0]);
		Assertions.assertEquals("10:30", line3[1]);
		Assertions.assertEquals(DifficultyLevel.HARD.getLabel(), line3[2]);
	}
	
	@Test
	public void testAddScore() {
		String serializedHighscoreList = 
				"Kristoffer,01:30,Normal\n"
				+ "Kristoffer,07:30,Easy\n";
		HighscoreList highscoreList = new HighscoreList(serializedHighscoreList);
		highscoreList.addScore(new Score("Henrik", "00:37", DifficultyLevel.NORMAL));
		Assertions.assertEquals(3, highscoreList.getAllhighScores().size());	
	}
	
	@Test
	public void testGetBestScores() {
		String serializedHighscoreList = 
				"Kristoffer,10:20,Normal\n"  
				+"Fred,02:23,Normal\n"  
				+"Nic,01:20,Easy\n"  
				+"Kjartan,02:28,Normal\n"  
				+"Sup,10:20,Normal\n"  
				+"Tord,02:26,Normal\n" 
				+"Alex,04:33,Hard\n"  
				+"Richard,02:24,Normal\n" 
				+"Ole,05:20,Normal\n"  
				+"Gunnar,20:23,Easy\n";
		HighscoreList highscoreList = new HighscoreList(serializedHighscoreList);
		
		//check if lenght of bestscore for level EASY is correct
		Collection<Score>  bestScoresEasy = highscoreList.getBestScores(DifficultyLevel.EASY);
		Assertions.assertTrue(bestScoresEasy.size() == 2);
				
		//check if lenght of bestscore for level NORMAL is correct
		Collection<Score>  bestScoresNormal = highscoreList.getBestScores(DifficultyLevel.NORMAL);
		Assertions.assertTrue(bestScoresNormal.size() == 5);
		
		//check if lenght of bestscore for level HARD is correct
		Collection<Score>  bestScoresHard = highscoreList.getBestScores(DifficultyLevel.HARD);
		Assertions.assertTrue(bestScoresHard.size() == 1);
		
		//check that bestscores list in level Easy is correct
		List<Score> correctBestScoresEasy = new ArrayList<>();
		correctBestScoresEasy.add(new Score("Nic", "01:20", DifficultyLevel.EASY));
		correctBestScoresEasy.add(new Score("Gunnar", "20:23", DifficultyLevel.EASY));
		int i = 0;
		for (Score score : bestScoresEasy) {
			Assertions.assertEquals(score.getName(), correctBestScoresEasy.get(i).getName());
			Assertions.assertEquals(score.getTime(), correctBestScoresEasy.get(i).getTime());
			Assertions.assertEquals(score.getDifficultyLevel() ,correctBestScoresEasy.get(i).getDifficultyLevel());
			i++;
		}
		
		//check that bestscores list in level Normal is correct
		List<Score> correctBestScoresNormal = new ArrayList<>();
		correctBestScoresNormal.add(new Score("Fred", "02:23", DifficultyLevel.NORMAL));
		correctBestScoresNormal.add(new Score("Richard", "02:24", DifficultyLevel.NORMAL));
		correctBestScoresNormal.add(new Score("Tord", "02:26", DifficultyLevel.NORMAL));
		correctBestScoresNormal.add(new Score("Kjartan", "02:28", DifficultyLevel.NORMAL));
		correctBestScoresNormal.add(new Score("Ole", "05:20", DifficultyLevel.NORMAL));
		int j = 0;
		for (Score score : bestScoresNormal) {
			Assertions.assertEquals(score.getName(), correctBestScoresNormal.get(j).getName());
			Assertions.assertEquals(score.getTime(), correctBestScoresNormal.get(j).getTime());
			Assertions.assertEquals(score.getDifficultyLevel() , correctBestScoresNormal.get(j).getDifficultyLevel());
			j++;
		}
		
		//check that bestscores list in level hard is correct
		List<Score> correctBestScoresHard = new ArrayList<>();
		correctBestScoresHard.add(new Score("Alex", "04:33", DifficultyLevel.HARD));
		for (Score score : bestScoresHard) {
			Assertions.assertEquals(score.getName(), correctBestScoresHard.get(0).getName());
			Assertions.assertEquals(score.getTime(), correctBestScoresHard.get(0).getTime());
			Assertions.assertEquals(score.getDifficultyLevel() , correctBestScoresHard.get(0).getDifficultyLevel());
		}
	}
}
