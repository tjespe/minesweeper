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

	private HighscoreList highscoreList;
	private static String path;

	@BeforeAll
	static void setUp() throws Exception {
		path = System.getProperty("java.io.tmpdir");
		File file = new File(path);
		file.mkdir();
		if (!file.exists()) {
			throw new Exception("Couldn't create directory for tests");
		}
		System.setProperty("user.home", path);
	}

	@Test
	public void testReadFromFile() throws IOException {
		highscoreList = new HighscoreList();
		highscoreList.addScore(new Score("Kristoffer", "01:30", DifficultyLevel.NORMAL));
		new ReadAndWriteHighscoreList().writeToFile(highscoreList);
		HighscoreList savedHighscoreList;
		savedHighscoreList = new ReadAndWriteHighscoreList().readFromFile();
		Assertions.assertEquals(highscoreList.getAllhighScores().size(), savedHighscoreList.getAllhighScores().size());
		Assertions.assertTrue(highscoreList.getAllhighScores().stream().allMatch(score -> savedHighscoreList
				.getAllhighScores().stream().anyMatch(savedScore -> savedScore.equals(score))));
	}

	@Test
	public void testReadNotValidFile() {
		System.setProperty("user.home", "/some/path/that/does/not/exist");
		Assertions.assertThrows(Exception.class, () -> highscoreList = new ReadAndWriteHighscoreList().readFromFile(),
				"Loading an invalid file should throw an exception");
		System.setProperty("user.home", path);
	}

	@Test
	public void testWriteToFile() throws IOException {
		highscoreList = new HighscoreList();
		new ReadAndWriteHighscoreList().writeToFile(highscoreList);
		byte[] game = null;
		game = Files.readAllBytes(Path.of(System.getProperty("user.home") + "/game-state.mswp"));
		Assertions.assertNotNull(game);
	}

	@AfterAll
	static void tearDown() {
		System.clearProperty("user.home");
	}
}
