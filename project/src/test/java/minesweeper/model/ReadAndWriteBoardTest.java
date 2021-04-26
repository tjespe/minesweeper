package minesweeper.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReadAndWriteBoardTest {

	private Board board;
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
		board = new Board(1, 1, 1);
		new ReadAndWriteBoard().writeToFile(board);
		Board savedGame;
		savedGame = new ReadAndWriteBoard().readFromFile();
		Assertions.assertEquals(board.toString(), savedGame.toString());

	}

	@Test
	public void testReadNotValidFile() {
		System.setProperty("user.home", "/some/path/that/does/not/exist");
		Assertions.assertThrows(Exception.class, () -> board = new ReadAndWriteBoard().readFromFile(),
				"Loading an invalid file should throw an exception");
		System.setProperty("user.home", path);
	}

	@Test
	public void testWriteToFile() throws IOException {
		board = new Board(1, 1, 1);
		new ReadAndWriteBoard().writeToFile(board);
		byte[] game = null;
		game = Files.readAllBytes(Path.of(System.getProperty("user.home") + "/game-state.mswp"));
		Assertions.assertNotNull(game);
	}

	@AfterAll
	static void tearDown() {
		System.clearProperty("user.home");
	}
}
