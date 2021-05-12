package minesweeper.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReadAndWriteBoardTest {

	private static String directory = System.getProperty("java.io.tmpdir");

	@Test
	void testReadFromFile() throws IOException {
		Board board = new Board(1, 1, 1);
		String path = directory + "/game-state-read-test.mswp";
		new ReadAndWriteBoard().writeToFile(board, path);
		Board savedGame = new ReadAndWriteBoard().readFromFile(path);
		Assertions.assertEquals(board.toString(), savedGame.toString());

	}

	@Test
	void testReadNotValidFile() {
		Assertions.assertThrows(Exception.class,
				() -> new ReadAndWriteBoard().readFromFile("/some/path/that/does/not/exist"),
				"Loading an invalid file should throw an exception");
	}

	@Test
	void testWriteToFile() throws IOException {
		Board board = new Board(1, 1, 1);
		String path = directory + "/game-state-write-test.mswp";
		new ReadAndWriteBoard().writeToFile(board, path);
		byte[] game = Files.readAllBytes(Path.of(path));
		Assertions.assertNotNull(game);
	}
}
