package minesweeper.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Scanner;

public class ReadAndWriteBoard implements ReadAndWriteFile<Board> {
	private final String filePath = ReadAndWriteFile.getAppDataDir() + "/game-state.mswp";

	@Override
	public Board readFromFile(String path) throws IOException {
		Scanner scanner = new Scanner(new File(path));
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		return new Board(content);
	}

	@Override
	public Board readFromFile() throws IOException {
		return this.readFromFile(filePath);
	}

	@Override
	public void writeToFile(Board board, String path) throws IOException {
		if (board == null)
			throw new IllegalStateException("No Board loaded");
		File file = new File(path);
		file.getParentFile().mkdirs();
		String data = board.getSerializedState();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"))) {
			writer.write(data);
		}
	}

	public void writeToFile(Board board) throws IOException {
		this.writeToFile(board, filePath);
	}

	public void deleteFile() throws IOException {
		Files.delete(FileSystems.getDefault().getPath(filePath));
	}
}
