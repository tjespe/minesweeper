package minesweeper.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

public class ReadAndWriteHighscoreList implements ReadAndWriteFile<HighscoreList> {
	private final String filePath = ReadAndWriteFile.getAppDataDir() + "/highscore-state.mswph";

	@Override
	public HighscoreList readFromFile(String path) throws IOException {
		Scanner scanner = new Scanner(new File(path));
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		return new HighscoreList(content);
	}

	@Override
	public HighscoreList readFromFile() throws IOException {
		return this.readFromFile(filePath);
	}

	@Override
	public void writeToFile(HighscoreList highscoreList, String path) throws IOException {
		if (highscoreList == null)
			throw new IllegalStateException("No HighscoreList loaded");
		File file = new File(path);
		file.getParentFile().mkdirs();
		String data = highscoreList.getSerializedHighscores();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"))) {
			writer.write(data);
		}
	}

	@Override
	public void writeToFile(HighscoreList highscoreList) throws IOException {
		this.writeToFile(highscoreList, filePath);
	}
}
