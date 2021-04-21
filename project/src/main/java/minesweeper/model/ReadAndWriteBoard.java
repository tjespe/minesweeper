package minesweeper.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

public class ReadAndWriteBoard implements ReadAndWriteFile<Board> {
	private static final String FILE_PATH = System.getProperty("user.home") + "/game-state.mswp";

	@Override
	public Board readFromFile() throws IOException {
		Scanner scanner = new Scanner(new File(FILE_PATH));
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		return new Board(content);
	}

	@Override
	public void writeToFile(Board board) throws IOException {
		if (board == null)
			throw new IllegalStateException("No Board loaded");
		System.out.println(FILE_PATH);
		String data = board.getSerializedState();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH), "utf-8"))) {
			writer.write(data);
		}
	}

}
