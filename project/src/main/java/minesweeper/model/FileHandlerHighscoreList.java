package minesweeper.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

public class FileHandlerHighscoreList implements ReadAndWriteFile<HighscoreList>{

	private static final String FILE_PATH = System.getProperty("user.home") + "/highscore-state.mswp";
	
	@Override
	public HighscoreList readFromFile() throws IOException {
		Scanner scanner = new Scanner(new File(FILE_PATH));
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		return new HighscoreList(content);
	}

	@Override
	public void writeToFile(HighscoreList highscoreList) throws IOException {
		if (highscoreList == null)
			throw new IllegalStateException("No HighscoreList loaded");
		String data = highscoreList.getSerializedHighscores();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH), "utf-8"))) {
			writer.write(data);
		}
	}

	
}
