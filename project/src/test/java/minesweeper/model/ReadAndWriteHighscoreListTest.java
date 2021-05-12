package minesweeper.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReadAndWriteHighscoreListTest {

	private static String directory = System.getProperty("java.io.tmpdir");

	@Test
	public void testReadFromFile() throws IOException {
		HighscoreList highscoreList = new HighscoreList();
		highscoreList.addScore(new Score("Kristoffer", "01:30", DifficultyLevel.NORMAL));
		String filePath = this.directory + "/read-highscore-list-test.mswph";
		new ReadAndWriteHighscoreList().writeToFile(highscoreList, filePath);
		HighscoreList savedHighscoreList;
		savedHighscoreList = new ReadAndWriteHighscoreList().readFromFile(filePath);
		Assertions.assertEquals(highscoreList.getAllhighScores().size(), savedHighscoreList.getAllhighScores().size());
		Assertions.assertTrue(highscoreList.getAllhighScores().stream().allMatch(score -> savedHighscoreList
				.getAllhighScores().stream().anyMatch(savedScore -> savedScore.equals(score))));
	}

	@Test
	public void testReadNotValidFile() {
		Assertions.assertThrows(Exception.class,
				() -> new ReadAndWriteHighscoreList().readFromFile("/some/path/that/does/not/exist"),
				"Loading an invalid file should throw an exception");
	}

	@Test
	public void testWriteToFile() throws IOException {
		HighscoreList highscoreList = new HighscoreList();
		String filePath = this.directory + "/write-highscore-list-test.mswph";
		new ReadAndWriteHighscoreList().writeToFile(highscoreList, filePath);
		byte[] game = Files.readAllBytes(Path.of(filePath));
		Assertions.assertNotNull(game);
	}
}
